define('services',['angularResource','configuration'],function(ngResource, configuration){
  'use strict';

  angular.module('services',['ngResource'])
    .factory('dataContext', ['$resource', function(resource){

    var rs = resource(configuration.baseUrl + '/:controller/:action', { /* global params */ }, {
          getTrackeables: { method: 'GET', params: { controller: 'trackeable' }, isArray: true }
      });

      var trackeableOnViewport = resource(configuration.baseUrl + '/trackeable/:SWLng,:SWLat/:NELng,:NELat',{},{
        getAll:{ method: 'GET', params:{ SWLng: '@southWest.lng', SWLat:'@southWest.lat', NELng:'@northEast.lng', NELat:'@northEast.lat' }, isArray:true }
      });

      return {
        getAllTrackeables: rs.getTrackeables,
        getTrackeablesOnViewport: trackeableOnViewport.getAll
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

          return dataContext.getAllTrackeables({},success,error);
        };

        var getAllOnViewport = function getAllOnViewport(data, callback, errCallback){

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

          return dataContext.getTrackeablesOnViewport(data,success,error);
        };

        return{
          getAll: getAll,
          getAllOnViewport: getAllOnViewport
        }
    }]);
});
