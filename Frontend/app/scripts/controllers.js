define('controllers',['services'], function(services){
  'use strict';

  angular
    .module('controllers',['services'])
    .controller('HomeCtrl',['$scope','trackableService', function(scope, service){

      scope.subscribeToViewport = function subscribeToViewport(data, callback){
        return service.subscribeToViewport(data, callback);
      };

      // scope.sendNotification = function sendNotification(){
      //     window.setNotification({ _id: '9890ad8sf90a8', name: 'test', value: 90});
      // };

      service.subscribeToNotifications(function(data){
          window.setNotification(data);
      });

    }])
    .controller('ZoneCtrl',['$scope','trackableService', '$window', function(scope, service, $window){
        var coords = [],
        area;

        scope.$watch('shape', function(){
          if(scope.shape === 'circle'){
            area = null;
          }

          if(scope.shape === 'polygon'){
            coords = [];
            area = new google.maps.Polygon({
              paths: coords,
              strokeColor: '#FF0000',
              strokeOpacity: 0.8,
              strokeWeight: 2,
              fillColor: '#FF0000',
              fillOpacity: 0.35
            });
          }

          if(scope.shape === 'rectangle'){
            // Construct the rectangle.
            coords = new google.maps.LatLngBounds();
            area = new google.maps.Rectangle({
              strokeColor: '#FF0000',
              strokeOpacity: 0.8,
              strokeWeight: 2,
              fillColor: '#FF0000',
              fillOpacity: 0.35
            });

          }
        });

        $window.setTimeout(function(){
          google.maps.event.addListener(window.map, 'click', function(e){
            if(scope.shape === 'rectangle'){
              coords.extend(e.latLng);
              area.setBounds(coords);
            }

            if(scope.shape === 'polygon'){
              coords.push(e.latLng);
              area.setPaths(coords);
            }

            area.setMap(window.map);
          });

        }, 2000);




        scope.options = service.getAll();
        scope.assignZone = function assignZone(){
          var data = {
            points:[],
            radius:1,
            isCritic: false,
            shape:'BOX',
            _id:''
          };

          var assignData = {
            idTrackable: scope.trackableId,
            idZone:0
          };

          service.createZone(data, function(responseData){
            assignData.idZone = responseData._id;
            service.assignZone(assignData);
          });
        };
    }]);
});
