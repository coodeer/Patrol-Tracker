define('app', ['jquery','angular','angularRoute','angularResource','controllers','directives','services','configuration','fastclick'],
  function ($, angular, ngRoute, ngResource, controllers, directives, services, configuration) {
    'use strict';

    angular
      .module('patrolTracker',['ngRoute','ngResource','controllers','directives','services'])
      .config(['$routeProvider', function($routeProvider){

          angular.forEach(configuration.steps, function (element) {
              $routeProvider.when(element.url, { templateUrl: element.templateUrl, controller: element.controller });
          });
          $routeProvider.otherwise({ redirectTo: configuration.steps[0].url });
      }])
      .run([function(){
        $(function() {
            FastClick.attach(document.body);
        });
      }]);
});
