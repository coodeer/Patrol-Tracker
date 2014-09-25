package com.patroltracker.controller
{
    import com.patroltracker.model.*;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.command.*;
    import org.puremvc.as3.patterns.observer.*;

    public class ModelPrepCommand extends SimpleCommand
    {
        override public function execute(note:INotification):void
        {
            facade.registerProxy(new StartupMonitorProxy());
            facade.registerProxy(new ConfigProxy());
            facade.registerProxy(new LocaleProxy());
            facade.registerProxy(new HistoryProxy());
        }
    }
}
