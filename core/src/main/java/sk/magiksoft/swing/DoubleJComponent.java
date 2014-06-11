
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

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JSpinner;

/**
 *
 * @author wladimiiir
 */
public class DoubleJComponent extends JComponent {

    private JComponent rendererComponent;
    private JComponent editorComponent;

    public DoubleJComponent(JComponent rendererComponent, JComponent editorComponent) {
        this.rendererComponent = rendererComponent;
        this.editorComponent = editorComponent;
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.add(rendererComponent, BorderLayout.CENTER);
        rendererComponent.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                removeAll();
                add(editorComponent, BorderLayout.CENTER);
                requestFocus();
                repaint();
                revalidate();
            }
        });
        rendererComponent.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                removeAll();
                add(editorComponent, BorderLayout.CENTER);
                requestFocus();
                repaint();
                revalidate();
            }
        });
        JComponent focusComponent;
        if(editorComponent instanceof JSpinner){
            focusComponent=((JSpinner)editorComponent).getEditor();
        }else{
            focusComponent=editorComponent;
        }
       
        focusComponent.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                removeAll();
                add(rendererComponent, BorderLayout.CENTER);
                repaint();
                revalidate();
            }
        });
    }

    public JComponent getEditorComponent() {
        return editorComponent;
    }

    public void setEditorComponent(JComponent editorComponent) {
        this.editorComponent = editorComponent;
    }

    public JComponent getRendererComponent() {
        return rendererComponent;
    }

    public void setRendererComponent(JComponent rendererComponent) {
        this.rendererComponent = rendererComponent;
    }
}