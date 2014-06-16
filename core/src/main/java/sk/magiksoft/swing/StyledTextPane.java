
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.swing;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;

/**
 * @author wladimiiir
 */
public class StyledTextPane extends JTextPane {

    private DefaultStyledDocument doc = new DefaultStyledDocument();

    public StyledTextPane() {
        setDocument(doc);

    }

    private class StyledText {
        private String text;


    }
}