define('fullscreen',[],function(){
  'use strict';

  function isFullScreen() {
      return !!(document.webkitIsFullScreen || document.mozFullScreen || document.isFullScreen); // if any defined and true
  }

  function fullScreenElement() {
      return document.webkitFullScreenElement || document.webkitCurrentFullScreenElement || document.mozFullScreenElement || document.fullScreenElement;
  }

  function setOnFullScreenChange(callback){
    // TODO: Validate if it is a function
    document.onfullscreenchange = document.onwebkitfullscreenchange = document.onmozfullscreenchange = callback;
  }

  function requestFullScreen(el){
    if (isFullScreen()) {
        document.cancelFullScreen();
    } else{
      el.requestFullScreen = el.webkitRequestFullScreen || el.mozRequestFullScreen || el.requestFullScreen;
      if(el.webkitRequestFullScreen){
        document.body.requestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
      }
    }
  }

  function init(){
    document.cancelFullScreen = document.webkitCancelFullScreen || document.mozCancelFullScreen || document.cancelFullScreen;
    document.body.requestFullScreen = document.body.webkitRequestFullScreen || document.body.mozRequestFullScreen || document.body.requestFullScreen;

  }

  init();
  return {
    setOnFullScreenChange: setOnFullScreenChange,
    requestElementFullScreen: requestFullScreen,
    requestFullScreen: document.body.requestFullScreen
  };
});
