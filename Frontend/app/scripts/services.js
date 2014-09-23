define('services',['angularResource'],function(ngResource){
  'use strict';

  angular.module('services',['ngResource'])
    .factory('dataContext', ['$resource', function(resource){

    var rs = resource('http://promociongl.herokuapp.com/:controller/:action', { /* global params */ }, {
          getVehicles: { method: 'GET', params: { controller: 'trackeable' }, isArray: true }
      });

      return {
        getAllVehicles: rs.getVehicles
      };
    }])
    .factory('trackableService', ['dataContext', function(dataContext){

        var getAll = function getAll(callback, errCallback){

          function success(responseData){
              // do something
              if(angular.isFunction(callback)){
                callback(responseData);
              }
          }

          function error(response){
            //do something
            if(angular.isFunction(errCallback)){
              errCallback(response);
            }
          }

          return dataContext.getAllVehicles({},success,error);
        };

        return{
          getAll: getAll
        }
    }]);
});
