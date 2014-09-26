define('services',['angularResource','configuration','pubnub'],function(ngResource, configuration, pubnub){
  'use strict';

  angular.module('services',['ngResource'])
    .factory('dataContext', ['$resource', function(resource){

    var rs = resource(configuration.baseUrl + '/:controller/:action', { /* global params */ }, {
          getTrackeables: { method: 'GET', params: { controller: 'trackeable' }, isArray: true }
      });

      var viewport = resource(configuration.baseUrl + '/trackeable/channel',{},{
        change:{ method: 'PUT', params:{}, headers:{ 'xsrf-token': 'aaabbbcccddd000111'}, isArray:false }
      });

      var pubnub = PUBNUB.init({
        publish_key:'pub-c-9240b235-1cbf-4c7f-b5b6-70227d80e339',
        subscribe_key:'sub-c-fe191c08-4426-11e4-b78c-02ee2ddab7fe'
      });

      var subscribeToNotifications = function(callback, errCallback){

        pubnub.subscribe({
          channel : "patrol-notifications",
          message : callback,
          error: errCallback
        });

        return function(){ pubnub.unsubscribe({ channel: "patrol-notifications" }); };
      };

      var subscribeToViewport = function(data, callback, errCallback){

        viewport.change(data);

        pubnub.subscribe({
          channel : "patrol-positions",
          message : callback,
          error: errCallback
        });

        return function(){ pubnub.unsubscribe({ channel: "patrol-positions" }); };
      };

      return {
        getAllTrackeables: rs.getTrackeables,
        changeViewport: viewport.change,
        subscribeToViewport: subscribeToViewport,
        subscribeToNotifications: subscribeToNotifications
      };
    }])
    .factory('trackableService', ['dataContext',
      function(dataContext){

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

        var subscribeToViewport = function(bounds, callback, errCallback){
          return dataContext.subscribeToViewport(bounds, callback, errCallback);
        };

        var subscribeToNotifications = function(callback, errCallback){
          return dataContext.subscribeToNotifications(callback, errCallback);
        };

        return{
          getAll: getAll,
          subscribeToViewport: subscribeToViewport,
          subscribeToNotifications: subscribeToNotifications
        };
      }
    ]);
});
