define('services',['angularResource'],function(ngResource){
  'use strict';

  angular.module('services',['ngResource'])
    .factory('dataContext', '$resource', function(resource){

      var rs = resource('/api/:controller/:action', { /* global params */ }, {
          getVehicles: { method: 'GET', params: { controller: 'trackeables', param1: '@paramExample' }, isArray: true }
      });

      return {
        getAllVehicles: rs.getVehicles
      };
    })
    .factory('trackableService', ['dataContext'], function(dataContext){
      
    });
});
