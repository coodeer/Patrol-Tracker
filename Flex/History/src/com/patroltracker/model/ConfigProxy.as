package com.patroltracker.model
{
    import com.patroltracker.model.business.LoadXMLDelegate;
    import com.patroltracker.model.helpers.XmlResource;
    import mx.rpc.IResponder;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.proxy.Proxy;

    /**
     * A proxy for read the config file
     */
    public class ConfigProxy extends Proxy implements IProxy, IResponder
    {

        // Messages
        public static const ERROR_LOAD_FILE:String = "Could Not Load the Config File!";

        public static const LOAD_FAILED:String = NAME + "loadFailed";

        // Notifications constansts
        public static const LOAD_SUCCESSFUL:String = NAME + "loadSuccessful";

        public static const NAME:String = "ConfigProxy";

        public static const SEPARATOR:String = "/";

        private var startupMonitorProxy:StartupMonitorProxy;

        public function ConfigProxy(data:Object = null)
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
            sendNotification(ConfigProxy.LOAD_FAILED, ConfigProxy.ERROR_LOAD_FILE);
        }

        /**
         * Get the config boolean value
         *
         * @param key the key to read
         * @return Boolean the key value stored in internal object
         */
        public function getBoolean(key:String):Boolean
        {
            return data[key.toLowerCase()] ? data[key.toLowerCase()].toLowerCase() == "true" : false;
        }

        /**
         * Get the config numeric value
         *
         * @param key the key to read
         * @return Number the key value stored in internal object
         */
        public function getNumber(key:String):Number
        {
            return Number(data[key.toLowerCase()]);
        }

        /**
         * Get the config value
         *
         * @param key the key to read
         * @return String the key value stored in internal object
         */
        public function getValue(key:String):String
        {
            return data[key.toLowerCase()];
        }

        /*
         * Load the xml file, this method is called by StartupMonitorProxy
         */
        public function load():void
        {
            var delegate:LoadXMLDelegate = new LoadXMLDelegate(this, 'assets/config.xml');
            delegate.load();
        }

        override public function onRegister():void
        {
            startupMonitorProxy = facade.retrieveProxy(StartupMonitorProxy.NAME) as StartupMonitorProxy;
            startupMonitorProxy.addResource(ConfigProxy.NAME, true);

            setData(new Object());
        }

        /*
         * This is called when the delegate receives a result from the service
         *
         * @param rpcEvent
         */
        public function result(rpcEvent:Object):void
        {
            XmlResource.parse(data, rpcEvent.result);

            startupMonitorProxy.resourceComplete(ConfigProxy.NAME);

            sendNotification(ConfigProxy.LOAD_SUCCESSFUL);
        }

        /**
         * Set the config value if isn't defined
         *
         * @param key the key to set
         * @param value the value
         */
        public function setDefaultValue(key:String, value:Object):void
        {
            if (!data[key.toLowerCase()])
            {
                data[key.toLowerCase()] = value;
            }
        }
    }
}
