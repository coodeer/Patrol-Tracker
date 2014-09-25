package com.patroltracker.model
{
    import com.patroltracker.model.business.*;
    import com.patroltracker.model.helpers.*;
    import mx.rpc.IResponder;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.proxy.Proxy;

    /**
     * A proxy for read the resource file
     */
    public class LocaleProxy extends Proxy implements IProxy, IResponder
    {

        // Messages
        public static const ERROR_LOAD_FILE:String = "Could Not Load the Config File!"; // Error message

        public static const LOAD_FAILED:String = NAME + "loadFailed"; // Failed notification

        // Notifications constansts
        public static const LOAD_SUCCESSFUL:String = NAME + "loadSuccessful"; // Successful notification

        public static const NAME:String = "LocaleProxy"; // Proxy name

        private var startupMonitorProxy:StartupMonitorProxy; // StartupMonitorProxy instance

        public function LocaleProxy(data:Object = null)
        {
            super(NAME, data);
        }

        /*
         * This is called when the delegate receives a fault from the service
         *
         * @param rpcEvent
         */
        public function fault(rpcEvent:Object):void
        {
            // send the failed notification
            sendNotification(LocaleProxy.LOAD_FAILED, LocaleProxy.ERROR_LOAD_FILE);
        }

        /**
         * Get the localized text
         *
         * @param key the key to read
         * @return String the text value stored in internal object
         */
        public function getText(key:String):String
        {
            return data[key.toLowerCase()];
        }

        /*
         * Load the xml file, this method is called by StartupMonitorProxy
         */
        public function load():void
        {
            var configProxy:ConfigProxy = facade.retrieveProxy(ConfigProxy.NAME) as ConfigProxy;
            var language:String = configProxy.getValue('language');

            // check if the language is defined
            if (language && language != "")
            {
                var url:String = "assets/" + configProxy.getValue('language') + '.xml';

                // create a worker who will go get some data
                // pass it a reference to this proxy so the delegate knows where to return the data
                var delegate:LoadXMLDelegate = new LoadXMLDelegate(this, url);
                // make the delegate do some work
                delegate.load();
            }
            else
            {
                resourceLoaded();
            }
        }

        override public function onRegister():void
        {
            // retrieve the StartupMonitorProxy
            startupMonitorProxy = facade.retrieveProxy(StartupMonitorProxy.NAME) as StartupMonitorProxy;
            // add the resource to load
            startupMonitorProxy.addResource(LocaleProxy.NAME, true);

            // reset the data   
            setData(new Object());
        }

        /*
         * This is called when the delegate receives a result from the service
         *
         * @param rpcEvent
         */
        public function result(rpcEvent:Object):void
        {
            // call the helper class for parse the XML data
            XmlResource.parse(data, rpcEvent.result);

            resourceLoaded();
        }

        /*
         * Send the notifications when the resource is loaded
         */
        private function resourceLoaded():void
        {
            // call the StartupMonitorProxy for notify that the resource is loaded
            startupMonitorProxy.resourceComplete(LocaleProxy.NAME);

            // send the successful notification
            sendNotification(ConfigProxy.LOAD_SUCCESSFUL);
        }
    }
}
