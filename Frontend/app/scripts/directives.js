define('directives',['jquery', 'services', 'markerClusterer','fullscreen'], function($, services, markerClusterer, fullscreen){
  'use strict';

  angular.module('directives',[])
    .directive('googleMap', [function(){
      var directiveDefinition = {
        restrict:'C',
        scope:false,
        priority:1,
        link:function(scope, element, attrs){
          scope.centerLat = parseFloat(scope.centerLat);
          scope.centerLng = parseFloat(scope.centerLng);

          scope.init = function(){
            if(window.google && window.google.maps){
              window.initialize();
            }
            else{
              var script = document.createElement('script');
              script.type = 'text/javascript';
              script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&' +
                  'callback=initialize';
              document.body.appendChild(script);
            }
          }

          window.initialize = function init() {
            var mapOptions = {
              center: new google.maps.LatLng(scope.centerLat || -34.397, scope.centerLng  || 150.644),
              zoom: 8,
              mapTypeId: google.maps.MapTypeId.ROADMAP
            };

            require(['infobox']);

            if(scope.$root.$$phase !== '$apply' && scope.$root.$$phase !== '$digest') {
              scope.$apply(function(){
                scope.map = new google.maps.Map(element[0], mapOptions);
              });
            }
            else{
              scope.map = new google.maps.Map(element[0], mapOptions);
            }
          }

          scope.init();
        }
      }
      return directiveDefinition;
    }])
    .directive('trackableMap',[function(){
      var directiveDefinition = {
        restrict:'A',
        replace:true,
        templateUrl:'views/trackableMap.html',
        scope:{
          centerLat:'@',
          centerLng:'@',
          getData: '&'
        },
        link:function(scope, element, attrs){
            var initWatch,
              viewportChangedEvent,
              viewportSubscriptionToken,
              clusterer,
              clustererOptions = {
                gridSize: 50,
                maxZoom: 15,
                styles:[{
                  url: '/views/soldier35.png',
                  width: 35,
                  height: 54,
                  textColor: '#ffffff',
                  textSize: 12
                }, {
                  url: '/views/soldier45.png',
                  width: 45,
                  height: 69,
                  textColor: '#ffffff',
                  textSize: 13
                }, {
                  url: '/views/soldier55.png',
                  width: 55,
                  height: 84,
                  textColor: '#ffffff',
                  textSize: 14
                }]
              },
              infoBoxOptions = {
                content: '',
                disableAutoPan: false,
                maxWidth: 0,
                zIndex: null,
                boxStyle: {
                  opacity: 0.9,
                  width: "200px",
                },
                closeBoxMargin: "10px 2px 2px 2px",
                closeBoxURL: "http://www.google.com/intl/en_us/mapfiles/close.gif"
              },
              carIcon,
              soldierIcon,
              lastEventDate,
              boundsEventToken;

          scope.markers = [];

          function init(){
            // clear the watcher for map
            initWatch();

            // setup icons
            carIcon = new google.maps.MarkerImage('/views/car36.png', new google.maps.Size(36, 19));
            soldierIcon = new google.maps.MarkerImage('/views/soldier24.png', new google.maps.Size(24, 37));
            infoBoxOptions.infoBoxClearance = new google.maps.Size(1, 1);
            infoBoxOptions.pixelOffset = new google.maps.Size(-100, 0),


            // on viewport change get data
            google.maps.event.addListener(scope.map, 'bounds_changed', function(){
              if(!lastEventDate){
                // start waiting until event ends
                boundsEventToken = window.setInterval(checkIfBoundsEventsFinished, 300);
              }
              else{
                // update last event date
                lastEventDate = new Date().getTime();
              }
            });
          }

          function checkIfBoundsEventsFinished(){
            var timeStamp = new Date().getTime();
            if(lastEventDate && (lastEventDate + 300) <  timeStamp){
                onBoundsChanged();
                window.clearInterval(boundsEventToken);
                lastEventDate = null;
                boundsEventToken = null;
            }

            // if just started set lastEventDate
            if(!lastEventDate && boundsEventToken){
              lastEventDate = timeStamp;
            }
          }

          function onBoundsChanged(){

            // cancel current subscription
            if(viewportSubscriptionToken){
              viewportSubscriptionToken();
            }

            var northEast = scope.map.getBounds().getNorthEast();
            var southWest = scope.map.getBounds().getSouthWest();

            var bounds = {
              northEast:{
                latitude: northEast.lat(),
                longitude: northEast.lng()
              },
              southWest:{
                latitude: southWest.lat(),
                longitude: southWest.lng()
              }
            };

            subscribeToViewport(bounds);
          }

          function subscribeToViewport(bounds){
            viewportSubscriptionToken = scope.getData({  data: bounds, callback: updateMap });
          }

          function getTrackeableIndex(trackeable){
            var markerIndex = -1;
            for (var i = 0; i < scope.markers.length; i++) {
              if(scope.markers[i].trackeable._id === trackeable._id){
                markerIndex = i;
                break;
              }
            }
            return markerIndex;
          }

          function updateMap(data){

            if(!angular.isArray(data)){
              var m = {
                currentPosition: {
                  latitude: data.latitude,
                  longitude: data.longitude
                },
                _id : data.trackeable_id,
                velocity: data.velocity
              };

              data = [];
              data.push(m);
            }

            for (var i = 0; i < data.length; i++) {
              var location = new google.maps.LatLng(data[i].currentPosition.latitude, data[i].currentPosition.longitude);
              var markerIndex = getTrackeableIndex(data[i]);

              if(markerIndex !== -1){
                // update location
                scope.markers[markerIndex].setPosition(location);
              }
              else{
                var marker = new google.maps.Marker({
                    position: location,
                    icon: data[i].isVehicle ? carIcon : soldierIcon
                });

                marker.infobox = new InfoBox(infoBoxOptions);
                marker.trackeable = data[i];

                google.maps.event.addListener(marker, 'click', function(e){
                  this.infobox.setContent(getMarkerHtml(this.trackeable));
                  this.infobox.open(scope.map, this);
                });

                scope.markers.push(marker);
              }
            }

            if(clusterer){
              clusterer.clearMarkers();
            }
            clusterer = new MarkerClusterer(scope.map, scope.markers, clustererOptions);
          }

          function getMarkerHtml(trackeable){
            return '<div class="markerInfobox"><div class="markerArrow"></div><h5>'+ trackeable.name +'</h5><p>'+ trackeable.velocity +'</p></div>';
          }

          // init only when map is ready
          initWatch = scope.$watch('map', function(newValue, oldValue){
            if(newValue){
              init();
            }
          });

        }
      };
      return directiveDefinition;
    }])
    .directive('zoneMap',[
      function(){
        var directiveDefinition ={
          restrict: 'A',
          replace:true,
          template:'<div data-google-map="true" class="googleMap"></div>',
          scope:{
            centerLat:'@',
            centerLng:'@'
          },
          link:function(scope){
            var initWatch,
              coords = [],
              area;

            function init(){
              // clear the watcher for map
              initWatch();

              // Construct the polygon.
              area = new google.maps.Polygon({
                paths: coords,
                strokeColor: '#FF0000',
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: '#FF0000',
                fillOpacity: 0.35
              });

              area.setMap(scope.map);
              google.maps.event.addListener(scope.map, 'click', function(e){
                coords.push(e.latLng);
                area.setPaths(coords);
                area.setMap(scope.map);
              });

            }

            // init only when map is ready
            initWatch = scope.$watch('map', function(newValue, oldValue){
              if(newValue){
                init();
              }
            });


          }
        };
        return directiveDefinition;
      }
    ])
    .directive('fullScreen', [
      function(){
        var directiveDefinition ={
            restrict:'C',
            link:function(scope, element, attrs){
                element.on('click', function(){
                  fullscreen.requestElementFullScreen($('[data-ng-view]')[0]);
                });
            }
        };
        return directiveDefinition;
      }
    ])
    .directive('navbar',['$location',
      function(){
        var directiveDefinition = {
          restrict:'C',
          link:function(scope,element,attrs){
            scope.isHome = location.hash !== '#/zone';
            scope.isAssignZone = !scope.isHome;
            scope.location = location;
            scope.$watch('location.hash',function(){
              scope.isHome = location.hash !== '#/zone';
              scope.isAssignZone = !scope.isHome;
            });
          }
        };
        return directiveDefinition;
      }
    ])
    .directive('navbarForm',['trackableService',
      function(service){
        var directiveDefinition = {
          restrict: 'C',
          link: function(scope, element, attrs){
            scope.search = function(){
              service.search({ search: scope.searchText, type: 'All' }, function(data){
                scope.results = data;
              });
            };
          }
        };
        return directiveDefinition;
      }
    ]);
});
