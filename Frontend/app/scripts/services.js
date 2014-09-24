define('services',['angularResource','configuration'],function(ngResource, configuration){
  'use strict';

  angular.module('services',['ngResource'])
    .factory('dataContext', ['$resource', function(resource){

    var rs = resource(configuration.baseUrl + '/:controller/:action', { /* global params */ }, {
          getTrackeables: { method: 'GET', params: { controller: 'trackeable' }, isArray: true }
      });

      var trackeableOnViewport = resource(configuration.baseUrl + '/trackeable/:SWLng,:SWLat/:NELng,:NELat',{},{
        getAll:{ method: 'GET', params:{ SWLng: '@southWest.lng', SWLat:'@SWLat', NELng:'@NELng', NELat:'@NELat' }, isArray:true }
      });

      return {
        getAllTrackeables: rs.getTrackeables,
        getTrackeablesOnViewport: trackeableOnViewport.getAll
      };
    }])
    .factory('trackableService', ['dataContext', '$window',
      function(dataContext, $window){
        var pullFrequency = 100000;

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

          var requestData = {
            SWLat: data.southWest.lat,
            SWLng: data.southWest.lng,
            NELat: data.northEast.lat,
            NELng: data.northEast.lng
          };

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

          return dataContext.getTrackeablesOnViewport(requestData,success,error);
        };

        var subscribe = function subscribe(method, data, callback, errCallback){

          method(data, callback, errCallback);
          var intervalToken = $window.setInterval(function(){
              method(data, callback, errCallback);
          }, pullFrequency);

          var unsubscribe = function(){
            $window.clearInterval(intervalToken);
          };

          return unsubscribe;
        };

        var subscribeToViewport = function(bounds, callback, errCallback){
          return subscribe(getAllOnViewport, bounds, callback, errCallback);
        };

        return{
          getAll: getAll,
          subscribeToViewport: subscribeToViewport
        };
      }
    ]);
});