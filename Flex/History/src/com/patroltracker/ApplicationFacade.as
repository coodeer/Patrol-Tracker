package com.patroltracker
{
    import com.patroltracker.controller.*;
    import com.patroltracker.model.*;
    import com.patroltracker.view.*;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.facade.*;
    import org.puremvc.as3.patterns.observer.Notification;
    import org.puremvc.as3.patterns.proxy.*;

    /**
     * A concrete <code>Facade</code> for the <code>ApplicationSkeleton</code> application.
     * <P>
     * The main job of the <code>ApplicationFacade</code> is to act as a single
     * place for mediators, proxies and commands to access and communicate
     * with each other without having to interact with the Model, View, and
     * Controller classes directly. All this capability it inherits from
     * the PureMVC Facade class.</P>
     *
     * <P>
     * This concrete Facade subclass is also a central place to define
     * notification constants which will be shared among commands, proxies and
     * mediators, as well as initializing the controller with Command to
     * Notification mappings.</P>
     */
    public class ApplicationFacade extends Facade
    {

        // command
        public static const COMMAND_STARTUP_MONITOR:String = "StartupMonitorCommand";

        public static const SHUTDOWN:String = "shutdown";

        // Notification name constants
        // application
        public static const STARTUP:String = "startup";

        public static const VIEW_MAIN_SCREEN:String = "viewMainScreen";

        // view
        public static const VIEW_SPLASH_SCREEN:String = "viewSplashScreen";

        /**
         * Singleton ApplicationFacade Factory Method
         */
        public static function getInstance():ApplicationFacade
        {
            if (instance == null)
                instance = new ApplicationFacade();
            return instance as ApplicationFacade;
        }

        /**
         * Start the application
         */
        public function startup(app:History):void
        {
            sendNotification(STARTUP, app);
        }

        /**
         * Register Commands with the Controller
         */
        override protected function initializeController():void
        {
            super.initializeController();
            registerCommand(STARTUP, ApplicationStartupCommand);
        }
    }
}
