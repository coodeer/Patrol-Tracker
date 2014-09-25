define('services',['angularResource','configuration','pubnub'],function(ngResource, configuration, pubnub){
  'use strict';

  angular.module('services',['ngResource'])
    .factory('dataContext', ['$resource', function(resource){

    var rs = resource(configuration.baseUrl + '/:controller/:action', { /* global params */ }, {
          getTrackeables: { method: 'GET', params: { controller: 'trackeable' }, isArray: true }
      });

      var trackeableOnViewport = resource(configuration.baseUrl + '/trackeable/:SWLng,:SWLat/:NELng,:NELat',{},{
        getAll:{ method: 'GET', params:{ SWLng: '@southWest.lng', SWLat:'@SWLat', NELng:'@NELng', NELat:'@NELat' }, isArray:true }
      });

      var pubnub = PUBNUB.init({
        publish_key:'pub-c-9240b235-1cbf-4c7f-b5b6-70227d80e339',
        subscribe_key:'sub-c-fe191c08-4426-11e4-b78c-02ee2ddab7fe'
      });

      pubnub.subscribe({
        channel : "patrol-notifications",
        message : function(m){
          console.log(m);
        }
      });

      pubnub.subscribe({
        channel : "patrol-positions",
        message : function(m){
          console.log(m);
        }
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
