package com.patroltracker.view
{
    import com.patroltracker.model.*;
    import com.patroltracker.model.enum.ConfigKeyEnum;
    import com.patroltracker.model.enum.LocaleKeyEnum;
    import com.patroltracker.view.components.*;
    import flash.events.Event;
    import flash.events.MouseEvent;
    import flash.external.ExternalInterface;
    import flash.net.LocalConnection;
    import flash.system.ApplicationDomain;
    import mx.collections.ArrayCollection;
    import mx.preloaders.DownloadProgressBar;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.mediator.Mediator;
    import spark.components.Application;

    /**
     * A Mediator for interacting with the MainScreen component.
     */
    public class MainScreenMediator extends Mediator implements IMediator
    {
        public static const NAME:String = "MainScreenMediator";

        public var clientObject:Object;

        public var lc:LocalConnection;

        private var configProxy:ConfigProxy;

        private var historyProxy:HistoryProxy;

        private var localeProxy:LocaleProxy;

        /**
         * Constructor.
         */
        public function MainScreenMediator(viewComponent:MainScreen)
        {
            super(NAME, viewComponent);
        }

        public function callFromJavaScript(str:String):void
        {
            var params:Array = str.split(',');

            if (lc)
                lc.send("_myconnectionAir", "notify", params[0], params[1], params[2], null, null);
        }

        override public function handleNotification(notification:INotification):void
        {
            switch (notification.getName())
            {
                case HistoryProxy.LOAD_SUCCESSFUL:
                    setDataProvider(notification);
                    break;
            }
        }

        override public function listNotificationInterests():Array
        {
            return [ HistoryProxy.LOAD_SUCCESSFUL ];
        }

        override public function onRegister():void
        {
            configProxy = facade.retrieveProxy(ConfigProxy.NAME) as ConfigProxy;
            localeProxy = facade.retrieveProxy(LocaleProxy.NAME) as LocaleProxy;
            historyProxy = facade.retrieveProxy(HistoryProxy.NAME) as HistoryProxy;
            mainScreen.addEventListener(MainScreen.CREATION_COMPLETE, handleCreationComplete);
        }

        public function showHistory(id:String, name:String):void
        {

            //Todo:manage name
            var historyProxy:HistoryProxy = facade.retrieveProxy(HistoryProxy.NAME) as HistoryProxy;
            historyProxy.load(id);
        }

        /**
         * TEST pourposes
         * @param event
         *
         */
        protected function clickHandler(event:MouseEvent):void
        {
            try
            {
                if (lc)
                    lc.send("_myconnectionAir", "notify", "TEST", "TEST", "TEST", "TEST", "TEST");
            }
            catch (error:ArgumentError)
            {
                trace(error)
            }

        }

        protected function get mainScreen():MainScreen
        {
            return viewComponent as MainScreen;
        }

        protected function setDataProvider(notification:INotification):void
        {
            mainScreen.dataGrid.dataProvider = new ArrayCollection(JSON.parse(notification.getBody().toString()) as Array);
        }

        private function handleCreationComplete(evt:Event):void
        {
            var dm:Object = ApplicationDomain.currentDomain;
            ExternalInterface.addCallback("setNotification", callFromJavaScript);
            ExternalInterface.addCallback("showHistory", showHistory);

            mainScreen.button.addEventListener(MouseEvent.CLICK, clickHandler);
            lc = new LocalConnection();
            lc.allowDomain("*");
            lc.connect('_swfConnection');
            clientObject = new Object();
            clientObject.openMarker = function(param1:String):void
            {
                ExternalInterface.call("openMarker", param1);
            }
            lc.client = clientObject;
        }
    }
}
