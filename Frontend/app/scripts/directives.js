define('directives',['jquery', 'services', 'markerClusterer'], function($, services, markerClusterer){
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
                  textSize: 10
                }, {
                  url: '/views/soldier45.png',
                  width: 45,
                  height: 69,
                  textColor: '#ffffff',
                  textSize: 11
                }, {
                  url: '/views/soldier55.png',
                  width: 55,
                  height: 84,
                  textColor: '#ffffff',
                  textSize: 12
                }]
              },
               carIcon,
               soldierIcon;

          scope.markers = [];

          function init(){
            // clear the watcher for map
            initWatch();

            // setup icons
            carIcon = new google.maps.MarkerImage('/views/car.png', new google.maps.Size(24, 13));
            soldierIcon = new google.maps.MarkerImage('/views/soldier24.png', new google.maps.Size(24, 37));

            // on viewport change get data
            google.maps.event.addListener(scope.map, 'bounds_changed', function(){
              // cancel current subscription
              if(viewportSubscriptionToken){
                viewportSubscriptionToken();
              }

              var northEast = scope.map.getBounds().getNorthEast();
              var southWest = scope.map.getBounds().getSouthWest();

              var bounds = {
                northEast:{
                  lat: northEast.lat(),
                  lng: northEast.lng()
                },
                southWest:{
                  lat: southWest.lat(),
                  lng: southWest.lng()
                }
              };

              subscribeToViewport(bounds);
            });
          }

          function subscribeToViewport(bounds){
            viewportSubscriptionToken = scope.getData({  data: bounds, callback: updateMap });
          }

          function isMarkerLoaded(trackeable){
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
            for (var i = 0; i < data.length; i++) {
              var location = new google.maps.LatLng(data[i].currentPosition.latitude, data[i].currentPosition.longitude);
              var markerIndex = isMarkerLoaded(data[i]);

              if(markerIndex !== -1){
                // update location
                scope.markers[markerIndex].setPosition(location);
              }
              else{
                var marker = new google.maps.Marker({
                    position: location,
                    icon: data[i].isVehicle ? carIcon : soldierIcon
                });
                marker.trackeable = data[i];
                scope.markers.push(marker);
              }
            }

            if(clusterer){
              clusterer.clearMarkers();
            }
            clusterer = new MarkerClusterer(scope.map, scope.markers, clustererOptions);
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

              // Construct the rectangle.

              /*var rectangle = new google.maps.Rectangle({
              strokeColor: '#FF0000',
              strokeOpacity: 0.8,
              strokeWeight: 2,
              fillColor: '#FF0000',
              fillOpacity: 0.35
            });

              rectangle.setMap(scope.map);
              google.maps.event.addListener(scope.map, 'click', function(e){
                debugger
                //bounds.push(e.latLng);

                bounds = new google.maps.LatLngBounds(
                 
                  new google.maps.LatLng(e.LatLng)
                );
                rectangle.setBounds(bounds);
                rectangle.setMap(scope.map);
              });*/

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
});
