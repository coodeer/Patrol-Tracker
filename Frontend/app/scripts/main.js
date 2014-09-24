require.config({
    paths: {
        jquery: '../bower_components/jquery/jquery',
        bootstrap: 'vendor/bootstrap/bootstrap',
        angular:'../bower_components/angular/angular.min',
        angularRoute:'../bower_components/angular-route/angular-route.min',
        angularResource:'../bower_components/angular-resource/angular-resource.min',
        fastclick:'vendor/fastclick',
        markerClusterer: 'vendor/markerclusterer',
        fullscreen:'fullscreensnippet'
    },
    shim: {
        bootstrap: {
            deps: ['jquery'],
            exports: 'jquery'
        },
        angular:{
          deps:['jquery'],
          exports:'angular'
        },
        angularRoute:{
          deps:['angular'],
          exports:'angular'
        },
        angularResource:{
          deps:['angular'],
          exports:'angular'
        }
    }
});

require(['app'], function (app) {
  'use strict';

  angular.element(document).ready(function () {
      angular.bootstrap(angular.element('body'), ['patrolTracker']);
  });
});
