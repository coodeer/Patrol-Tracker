define('controllers',['services'], function(services){
  'use strict';

  angular
    .module('controllers',['services'])
    .controller('HomeCtrl',['$scope','trackableService', function(scope, service){

      scope.getData = function getData(){
        scope.trackeables =  service.getAll();
        return scope.trackeables;
      };
    }])
    .controller('assignZoneCtrl',['$scope', function(scope){

    }]);
});
