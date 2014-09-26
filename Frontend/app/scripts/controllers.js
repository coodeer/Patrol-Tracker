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
    .controller('ZoneCtrl',['$scope','trackableService', function(scope, service){
        console.log(service.getAll());
    }]);
});
