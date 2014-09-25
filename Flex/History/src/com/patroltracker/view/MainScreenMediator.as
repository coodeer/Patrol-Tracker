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
    import mx.collections.ArrayCollection;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.mediator.Mediator;

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

        public function callFromJavaScript(param1:Object):void
        {
            //todo parse params

            if (lc)
                lc.send("_myconnectionAir", "notify", param1);
        }

        override public function handleNotification(notification:INotification):void
        {
            switch (notification.getName())
            {
                /*		case MyFacade.SET_SELECTION:
                            setSelection(notification);
                            break;*/
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

        protected function clickHandler(event:MouseEvent):void
        {
            try
            {
                if (lc)
                    lc.send("_myconnectionAir", "notify", "TEST");
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
            ExternalInterface.addCallback("setNotification", callFromJavaScript);
            ExternalInterface.addCallback("showHistory", showHistory);

            mainScreen.button.addEventListener(MouseEvent.CLICK, clickHandler);
            lc = new LocalConnection();
            clientObject = new Object();
            clientObject.openMarker = function(param1:String):void
            {
                ExternalInterface.call("openMarker", param1);
                trace("openMarker called with one parameter: " + param1);
            }
            lc.client = clientObject;

        }
    }
}
