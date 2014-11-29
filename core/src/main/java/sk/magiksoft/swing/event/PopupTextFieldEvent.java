package sk.magiksoft.swing.event;

import java.awt.event.ActionEvent;

/**
 * @author wladimiiir
 */
public class PopupTextFieldEvent extends ActionEvent {

    public PopupTextFieldEvent(Object source, int id, String command) {
        super(source, id, command);
    }


}
