define('services',['angularResource','configuration','pubnub'],function(ngResource, configuration, pubnub){
  'use strict';

  angular.module('services',['ngResource'])
    .factory('dataContext', ['$resource', function(resource){

      var rs = resource(configuration.baseUrl + '/:controller/:action', { /* global params */ }, {
          getTrackeables: { method: 'GET', params: { controller: 'trackeable' }, isArray: true },
          change:{ method: 'PUT', params:{  controller:'trackeable', action: 'channel'}, isArray: false },
          searchAll:{method: 'GET', params:{ controller:'trackeable', action: 'search', search:'@search'}, isArray: true},
          createZone:{ method: 'POST', params:{ controller:'zone' }, isArray: false }
      });

      var assignZone = resource(configuration.baseUrl + '/trackeable/:idTrackable/zone/:idZone',{},{
        change:{ method: 'PUT', params:{ idTrackable: '@idTrackable', idZone: '@idZone' }, isArray: false}
      });

      var search = resource(configuration.baseUrl + '/trackeable/type/:type/search/:search',{},{
        byType:{ method: 'GET', params:{ type: '@type', search: '@search' }, isArray: true}
      });

      var search = resource(configuration.baseUrl + '/trackeable/type/:type/search/:search',{},{
        byType:{ method: 'GET', params:{ type: '@type', search: '@search' }, isArray: true}
      });

      var getViewport = resource(configuration.baseUrl + '/trackeable/:slng,:slat/:nlng,:nlat',{},{
        all:{ method: 'GET', params:{ slng: '@slng', slat:'@slat', nlng:'@nlng', nlat:'@nlat' }, isArray: true}
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
        rs.change(data);

        getViewport.all({
          slat: data.southWest.latitude,
          slng: data.southWest.longitude,
          nlat: data.northEast.latitude,
          nlng: data.northEast.longitude
          }, callback);

        pubnub.subscribe({
          channel : "patrol-positions",
          message : callback,
          error: errCallback
        });

        return function(){ pubnub.unsubscribe({ channel: "patrol-positions" }); };
      };

      return {
        getAllTrackeables: rs.getTrackeables,
        subscribeToViewport: subscribeToViewport,
        subscribeToNotifications: subscribeToNotifications,
        searchAll: rs.searchAll,
        searchByType: search.byType,
        createZone: rs.createZone,
        assignZone: assignZone.change
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

        var createZone = function createZone(data, callback, errCallback){

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

          return dataContext.createZone(data,success,error);
        };

        var assignZone = function assignZone(data, callback, errCallback){

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

          return dataContext.assignZone(data,success,error);
        };

        var subscribeToViewport = function(bounds, callback, errCallback){
          return dataContext.subscribeToViewport(bounds, callback, errCallback);
        };

        var subscribeToNotifications = function(callback, errCallback){
          return dataContext.subscribeToNotifications(callback, errCallback);
        };

        var search = function search(data, callback, errCallback){
          function success(responseData){
            if(angular.isFunction(callback)){
              callback(responseData);
            }
          }

          function error(responseData){
            if(angular.isFunction(errCallback)){
              errCallback(responseData);
            }
          }

          if(data.type === 'All'){
            return dataContext.searchAll({ search: data.search }, success, error);
          }
          else{
            return dataContext.searchByType(data, success, error);
          }
        };

        return{
          getAll: getAll,
          subscribeToViewport: subscribeToViewport,
          subscribeToNotifications: subscribeToNotifications,
          search: search,
          createZone: createZone,
          assignZone: assignZone
        };
      }
    ]);
});
