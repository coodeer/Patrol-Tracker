require.config({
    paths: {
        jquery: '../bower_components/jquery/jquery',
        bootstrap: 'vendor/bootstrap/bootstrap',
        angular:'../bower_components/angular/angular.min',
        fastclick:'vendor/fastclick',
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
        }
    }
});

require(['app', 'jquery', 'bootstrap','fastclick'], function (app, $, bs, fastclick) {
    'use strict';

    $(function() {
        FastClick.attach(document.body);
    });
});
