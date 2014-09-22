define('directives',['jquery', 'services'], function($, services){
  'use strict';

  angular.module('directives',[])
    .directive('map', [], function(){
      var directiveDefinition = {
        restrict:'A',
        scope:true,
        link:function(scope){
          
        }
      }
    });

});
