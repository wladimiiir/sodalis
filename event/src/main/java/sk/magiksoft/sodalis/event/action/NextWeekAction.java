package sk.magiksoft.sodalis.event.action;

import sk.magiksoft.sodalis.event.EventContextManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class NextWeekAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        EventContextManager.getInstance().addToCalendar(Calendar.DATE, 7);
    }

}
