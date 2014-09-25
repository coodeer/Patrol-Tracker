package com.patroltracker.view
{
    import com.patroltracker.ApplicationFacade;
    import com.patroltracker.model.*;
    import com.patroltracker.view.components.*;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.mediator.Mediator;

    public class ApplicationMediator extends Mediator implements IMediator
    {
        public static const MAIN_SCREEN:Number = 2;

        public static const NAME:String = "ApplicationMediator";

        public static const SPLASH_SCREEN:Number = 1;

        /**
         * Constructor.
         *
         * <P>
         * On <code>applicationComplete</code> in the <code>Application</code>,
         * the <code>ApplicationFacade</code> is initialized and the
         * <code>ApplicationMediator</code> is created and registered.</P>
         *
         * <P>
         * The <code>ApplicationMediator</code> constructor also registers the
         * Mediators for the view components created by the application.</P>
         *
         * @param object the viewComponent (the ApplicationSkeleton instance in this case)
         */
        public function ApplicationMediator(viewComponent:History)
        {
            super(NAME, viewComponent);
        }

        /**
         * Handle all notifications this Mediator is interested in.
         * <P>
         * Called by the framework when a notification is sent that
         * this mediator expressed an interest in when registered
         * (see <code>listNotificationInterests</code>.</P>
         *
         * @param INotification a notification
         */
        override public function handleNotification(note:INotification):void
        {
            switch (note.getName())
            {
                case ApplicationFacade.VIEW_SPLASH_SCREEN:
                    app.vwStack.selectedIndex = SPLASH_SCREEN;
                    break;

                case ApplicationFacade.VIEW_MAIN_SCREEN:
                    app.vwStack.selectedIndex = MAIN_SCREEN;
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
            return [ ApplicationFacade.VIEW_SPLASH_SCREEN, ApplicationFacade.VIEW_MAIN_SCREEN ];
        }

        override public function onRegister():void
        {
            facade.registerMediator(new SplashScreenMediator(app.splashScreen));
            facade.registerMediator(new MainScreenMediator(app.mainScreen));
        }

        /**
         * Cast the viewComponent to its actual type.
         *
         * <P>
         * This is a useful idiom for mediators. The
         * PureMVC Mediator class defines a viewComponent
         * property of type Object. </P>
         *
         * <P>
         * Here, we cast the generic viewComponent to
         * its actual type in a protected mode. This
         * retains encapsulation, while allowing the instance
         * (and subclassed instance) access to a
         * strongly typed reference with a meaningful
         * name.</P>
         *
         * @return app the viewComponent cast to AppSkeleton
         */
        protected function get app():History
        {
            return viewComponent as History;
        }
    }
}
