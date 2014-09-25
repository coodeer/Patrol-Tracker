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
                lc.send("_myconnectionAir", "doMethod2", "caca");
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
                    lc.send("_myconnectionAir", "doMethod2", "caca");
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

        /*********************************/
        /* events handler 				 */
        /*********************************/

        private function handleCreationComplete(evt:Event):void
        {
            ExternalInterface.addCallback("setNotification", callFromJavaScript);
            ExternalInterface.addCallback("showHistory", showHistory);

            mainScreen.button.addEventListener(MouseEvent.CLICK, clickHandler);
            lc = new LocalConnection();
            clientObject = new Object();
            clientObject.doMethod1 = function()
            {
                trace("doMethod1xxxx called.");
            }
            clientObject.doMethod2 = function(param1)
            {
                trace("doMethod2xxxxx called with one parameter: " + param1);
                trace("The squarexxxx of the parameter is: " + param1 * param1);
            }
            lc.client = clientObject;

            //lc.connect("mySwfConnection");

            // lc.send("_myconnectionAir", "doMethod2", "caca");

        /*            mainScreen.myText1 = localeProxy.getText(LocaleKeyEnum.HOW_TO_READ_CONFIG_VALUES);

                    var myHtmlText:String = '';
                    myHtmlText += '<b>simple value:</b> configProxy.getValue( ConfigKeyEnum.KEY_NAME ) = ' + configProxy.getValue(ConfigKeyEnum.OTHER_KEY_NAME) + '<br><br>';
                    myHtmlText += '<b>long text value:</b> configProxy.getValue( ConfigKeyEnum.OTHER_KEY_NAME ) = ' + configProxy.getValue(ConfigKeyEnum.OTHER_KEY_NAME) + '<br><br>';
                    myHtmlText += '<b>number value:</b> configProxy.getNumber( ConfigKeyEnum.NUMBER_TEST ) = ' + configProxy.getNumber(ConfigKeyEnum.NUMBER_TEST) + '<br><br>';
                    myHtmlText += '<b>boolean value:</b> configProxy.getBoolean( ConfigKeyEnum.BOOLEAN_TEST ) = ' + configProxy.getBoolean(ConfigKeyEnum.BOOLEAN_TEST) + '<br><br>';
                    myHtmlText += '<b>default value:</b> configProxy.getValue( ConfigKeyEnum.TEST_DEFAULT_VALUE ) = ' + configProxy.getValue(ConfigKeyEnum.TEST_DEFAULT_VALUE) + '<br><br>';
                    myHtmlText += '<b>value inside a group:</b> configProxy.getValue( ConfigKeyEnum.GROUP_NAME + ConfigProxy.SEPARATOR + ConfigKeyEnum.KEY_INSIDE_GROUP ) = ' + configProxy.getValue(ConfigKeyEnum.GROUP_NAME + ConfigProxy.SEPARATOR + ConfigKeyEnum.KEY_INSIDE_GROUP) + '<br><br>';
                    myHtmlText += '<b>value inside neested group:</b> configProxy.getValue( ConfigKeyEnum.GROUP_NAME + ConfigProxy.SEPARATOR + ConfigKeyEnum.SUBGROUP_NAME + ConfigProxy.SEPARATOR + ConfigKeyEnum.KEY_INSIDE_SUBGROUP  ) = ' + configProxy.getValue(ConfigKeyEnum.GROUP_NAME + ConfigProxy.SEPARATOR + ConfigKeyEnum.SUBGROUP_NAME + ConfigProxy.SEPARATOR + ConfigKeyEnum.KEY_INSIDE_SUBGROUP) + '<br><br>';
                    mainScreen.myText2 = myHtmlText;

                    mainScreen.myText3 = localeProxy.getText(LocaleKeyEnum.HOW_TO_READ_LOCALE_TEXT);

                    var myHtmlLocaleText:String = '';
                    myHtmlLocaleText += '<b>simple text resource:</b> localeProxy.getText( LocaleKeyEnum.HELLO_WORLD ) = ' + localeProxy.getText(LocaleKeyEnum.HELLO_WORLD) + '<br><br>';
                    myHtmlLocaleText += '<b>long text resource:</b> localeProxy.getText( LocaleKeyEnum.LONG_TEXT ) = ' + localeProxy.getText(LocaleKeyEnum.LONG_TEXT) + '<br>';
                    mainScreen.myText4 = myHtmlLocaleText;*/

        }
    }
}
