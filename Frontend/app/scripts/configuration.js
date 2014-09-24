define('configuration',[],function(){
  'use strict';

  var steps = [
      { url: '/', description: 'Patrullas en tiempo real', templateUrl: '/views/home.html', controller: 'HomeCtrl'},
      { url: '/zone', description:'Asignar Zonas', templateUrl:'/views/zone.html', controller:'ZoneCtrl'}
  ];

  return{
    steps: steps,
    baseUrl: 'http://promociongl.herokuapp.com'
  };
});
