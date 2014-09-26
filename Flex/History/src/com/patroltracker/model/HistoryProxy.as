package com.patroltracker.model
{
    import com.patroltracker.model.business.LoadHistoryDelegate;
    import mx.rpc.IResponder;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.proxy.Proxy;

    /**
     * A proxy for get history
     */
    public class HistoryProxy extends Proxy implements IProxy, IResponder
    {
        public static const ERROR_LOAD_FILE:String = "Could Not Load History!";

        public static const LOAD_FAILED:String = NAME + "loadFailed";

        public static const LOAD_SUCCESSFUL:String = NAME + "loadSuccessful";

        public static const NAME:String = "HistoryProxy";

        public static const SEPARATOR:String = "/";

        public function HistoryProxy(data:Object = null)
        {
            super(NAME, data);
        }

        public function fault(rpcEvent:Object):void
        {
            // send the failed notification
            sendNotification(HistoryProxy.LOAD_FAILED, HistoryProxy.ERROR_LOAD_FILE);
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

        public function load(id:String = ''):void
        {
            var delegate:LoadHistoryDelegate = new LoadHistoryDelegate(this, 'http://positiontracker.herokuapp.com/history/' + id);
            delegate.load();
        }

        override public function onRegister():void
        {
            setData(new Object());
        }

        /*
         * This is called when the delegate receives a result from the service
         *
         * @param rpcEvent
         */
        public function result(rpcEvent:Object):void
        {
            sendNotification(HistoryProxy.LOAD_SUCCESSFUL, rpcEvent.result);
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
