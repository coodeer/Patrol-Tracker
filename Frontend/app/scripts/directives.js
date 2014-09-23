define('directives',['jquery', 'services'], function($, services){
  'use strict';

  angular.module('directives',[])
    .directive('googleMap', [function(){
      var directiveDefinition = {
        restrict:'C',
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

            scope.$apply(function(){
              scope.map = new google.maps.Map(element[0], mapOptions);
            });
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
          scope.map = null;
          scope.markers = [];

          function placeMarker(latitude, longitude) {
            var location = new google.maps.LatLng(latitude, longitude);
            var marker = new google.maps.Marker({
                position: location,
                map: scope.map
            });

            scope.map.setCenter(location);
          }

          function init(){

            // scope.$watch('markers',function(){
            //   if(scope.markers){
            //
            //   }
            // });

            scope.getData(
              {
                callback: function(data){
                  scope.markers = data;
                  for (var i = 0; i < scope.markers.length; i++) {
                    var position = scope.markers[i].currentPosition;
                    placeMarker(position.latitude, position.longitude);
                  }
                }
              }
            );

          }

          // init only when map is ready
          scope.$watch('map', function(oldValue, newValue){
            if(oldValue === null && !newValue){
              init();
            }
          })

        }
      };
      return directiveDefinition;
    }]);

});
