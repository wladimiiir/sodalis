
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.function.Function;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.update.UpdateManager;
import sk.magiksoft.sodalis.core.update.updater.UpdaterManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;

/**
 *
 * @author wladimiiir
 */
public class UpdateFromFileAction extends AbstractAction{

    private JFileChooser fileChooser;

    public UpdateFromFileAction() {
        super(LocaleManager.getString("updateFromFile"));
    }

    @Override
    public void actionPerformed(ActionEvent event){
        if(fileChooser==null){
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(LocaleManager.getString("updateFiles"), "upd", "jar"));
        }

        if(fileChooser.showOpenDialog(SodalisApplication.get().getMainFrame())!=JFileChooser.APPROVE_OPTION || fileChooser.getSelectedFile()==null){
            return;
        }

        if(fileChooser.getSelectedFile().getName().endsWith(".jar")){
            SodalisApplication.get().runProgress(LocaleManager.getString("processingUpdaters"), new Function() {
                @Override public void apply() throws Exception{
                    UpdaterManager.getInstance().proceedUpdaters(fileChooser.getSelectedFile());
                }
            }, LocaleManager.getString("processingUpdatersError"));
            try {

            } catch (Exception e) {
                LoggerManager.getInstance().error(getClass(), e);
            }
        }else{
            UpdateManager.getInstance().doUpdate(fileChooser.getSelectedFile());
        }
    }
}