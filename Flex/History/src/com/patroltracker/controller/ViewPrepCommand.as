package com.patroltracker.controller
{
    import com.patroltracker.ApplicationFacade;
    import com.patroltracker.model.*;
    import com.patroltracker.view.ApplicationMediator;
    import org.puremvc.as3.interfaces.*;
    import org.puremvc.as3.patterns.command.*;
    import org.puremvc.as3.patterns.observer.*;

    /**
     * Prepare the View for use.
     *
     * <P>
     * The <code>Notification</code> was sent by the <code>Application</code>,
     * and a reference to that view component was passed on the note body.
     * The <code>ApplicationMediator</code> will be created and registered using this
     * reference. The <code>ApplicationMediator</code> will then register
     * all the <code>Mediator</code>s for the components it created.</P>
     *
     */
    public class ViewPrepCommand extends SimpleCommand
    {
        override public function execute(note:INotification):void
        {
            facade.registerMediator(new ApplicationMediator(note.getBody() as History));

            sendNotification(ApplicationFacade.VIEW_SPLASH_SCREEN);
        }
    }
}
