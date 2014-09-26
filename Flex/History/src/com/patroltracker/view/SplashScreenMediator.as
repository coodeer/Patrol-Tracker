package com.patroltracker.view
{
    import com.patroltracker.ApplicationFacade;
    import com.patroltracker.model.*;
    import com.patroltracker.view.components.*;
    import flash.events.Event;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.mediator.Mediator;

    public class SplashScreenMediator extends Mediator implements IMediator
    {
        public static const NAME:String = "SplashScreenMediator";

        /**
         * Constructor.
         */
        public function SplashScreenMediator(viewComponent:SplashScreen)
        {
            super(NAME, viewComponent);

            splashScreen.addEventListener(SplashScreen.EFFECT_END, this.endEffect);
        }

        override public function handleNotification(note:INotification):void
        {
            switch (note.getName())
            {
                case StartupMonitorProxy.LOADING_STEP:
                    this.splashScreen.pb.setProgress(note.getBody() as int, 100);
                    break;

                case StartupMonitorProxy.LOADING_COMPLETE:
                    this.sendNotification(ApplicationFacade.VIEW_MAIN_SCREEN);
                    break;

                case ConfigProxy.LOAD_FAILED:
                case LocaleProxy.LOAD_FAILED:
                    this.splashScreen.errorText.text = note.getBody() as String;
                    this.splashScreen.errorBox.visible = true;
                    break;
            }
        }

        /**
         * List all notifications this Mediator is interested in.
         * <P>
         * Automatically called by the framework when the mediator
         * is registered with the view.</P>
         *
         * @return Array the list of Nofitication names
         */
        override public function listNotificationInterests():Array
        {
            return [ StartupMonitorProxy.LOADING_STEP, StartupMonitorProxy.LOADING_COMPLETE, ConfigProxy.LOAD_FAILED, LocaleProxy.LOAD_FAILED ];
        }

        protected function get splashScreen():SplashScreen
        {
            return viewComponent as SplashScreen;
        }

        /**
         * End effect event
         */
        private function endEffect(event:Event = null):void
        {
            var startupMonitorProxy:StartupMonitorProxy = facade.retrieveProxy(StartupMonitorProxy.NAME) as StartupMonitorProxy;
            startupMonitorProxy.loadResources();
        }
    }
}
