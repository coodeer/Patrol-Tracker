package com.patroltracker.controller
{
    import com.patroltracker.ApplicationFacade;
    import com.patroltracker.model.*;
    import com.patroltracker.view.ApplicationMediator;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.command.*;
    import org.puremvc.as3.patterns.observer.*;

    public class ViewPrepCommand extends SimpleCommand
    {
        override public function execute(note:INotification):void
        {
            facade.registerMediator(new ApplicationMediator(note.getBody() as History));

            sendNotification(ApplicationFacade.VIEW_SPLASH_SCREEN);
        }
    }
}
