define('controllers',['services'], function(services){
  'use strict';

  angular
    .module('controllers',['services'])
    .controller('HomeCtrl',['$scope','trackableService', function(scope, service){

      scope.getData = function getData(callback){
        scope.trackeables =  service.getAll(callback);
      };
    }])
    .controller('assignZoneCtrl',['$scope', function(scope){

    }]);
});
