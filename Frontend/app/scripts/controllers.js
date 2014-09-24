define('controllers',['services'], function(services){
  'use strict';

  angular
    .module('controllers',['services'])
    .controller('HomeCtrl',['$scope','trackableService', function(scope, service){

      scope.subscribeToViewport = function subscribeToViewport(data, callback){
        return service.subscribeToViewport(data, callback);
      };
    }])
    .controller('ZoneCtrl',['$scope', function(scope){

    }]);
});
