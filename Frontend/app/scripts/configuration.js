define('configuration',[],function(){
  'use strict';

    var steps = [
        { url: '/', description: 'Patrullas en tiempo real', templateUrl: '/view/home.html', controller: 'HomeCtrl'}
    ];

  return{
    steps: steps
  }
});
