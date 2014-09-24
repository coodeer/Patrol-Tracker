define('controllers',['services'], function(services){
  'use strict';

  angular
    .module('controllers',['services'])
    .controller('HomeCtrl',['$scope','trackableService', function(scope, service){

      scope.getData = function getData(data, callback){
        scope.trackeables =  service.getAllOnViewport(data, callback);
      };
    }])
    .controller('ZoneCtrl',['$scope', function(scope){

    }]);
});
