
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.history;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.swing.ISTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class HistoryInfoPanel extends AbstractInfoPanel {

    protected JCheckBox chbCreate;
    protected JCheckBox chbUpdate;

    protected List<AbstractButton> controlPanelButtons;
    protected HistoryEventTableModel model;
    protected ISTable table;
    protected Historizable historizable;
    protected ActionListener buttonListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            initialized = false;
            initData();
        }
    };


    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Historizable)) {
            return;
        }

        historizable = (Historizable) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (initialized) {
            return;
        }

        model.removeAllObjects();
        for (HistoryEvent event : historizable.getHistoryEvents(null)) {
            if (!acceptHistoryEvent(event)) {
                continue;
            }
            model.addObject(event);
        }

        initialized = true;
    }

    @Override
    public boolean isWizardSupported() {
        return false;
    }

    @Override
    public boolean acceptObject(Object object) {
        return super.acceptObject(object)
                && object instanceof Historizable;
    }


    protected boolean acceptHistoryEvent(HistoryEvent event) {
        switch (event.getAction()) {
            case HistoryAction.CREATE:
                return chbCreate.isSelected();
            case HistoryAction.UPDATE:
                return chbUpdate.isSelected();
        }
        return true;
    }

    @Override
    public void setupObject(Object object) {
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("modification");
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new BorderLayout());
        final JScrollPane scrollPane = new JScrollPane(table = new ISTable(model = new HistoryEventTableModel()) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                final Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (vColIndex == 3) {
                    if (c instanceof JLabel) {
                        ((JLabel) c).setToolTipText(transform("data/xml/loginfo.xsl", ((JLabel) c).getText()));
                        ((JLabel) c).setText(transform("data/xml/loginfocomma.xsl", ((JLabel) c).getText()));
                    }
                }
                return c;
            }

            private String transform(String transformFilePath, String text) {
                final Reader reader = new StringReader(text);
                final StringWriter writer = new StringWriter();

                final Source xmlSource = new StreamSource(reader);
                final Source xsltSource = new StreamSource(new File(transformFilePath));

                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer;
                try {
                    transformer = factory.newTransformer(xsltSource);
                    transformer.transform(xmlSource, new StreamResult(writer));
                    writer.close();
                    return writer.toString();
                } catch (TransformerConfigurationException e) {
                    LoggerManager.getInstance().error(getClass(), e);
                } catch (TransformerException e) {
                    LoggerManager.getInstance().error(getClass(), e);
                } catch (IOException e) {
                    LoggerManager.getInstance().error(getClass(), e);
                }

                return "";
            }
        });

        scrollPane.getViewport().setBackground(Color.WHITE);
        layoutPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setPreferredSize(new Dimension(100, 50));
        initControlPanelButtons();

        return layoutPanel;
    }

    protected void initControlPanelButtons() {

        controlPanelButtons = new ArrayList<AbstractButton>();

        chbCreate = new JCheckBox(LocaleManager.getString("created"), true);
        chbUpdate = new JCheckBox(LocaleManager.getString("updated"), true);

        chbCreate.addActionListener(buttonListener);
        chbUpdate.addActionListener(buttonListener);

        controlPanelButtons.add(chbCreate);
        controlPanelButtons.add(chbUpdate);

    }

    @Override
    public List<AbstractButton> getControlPanelButtons() {
        return controlPanelButtons;
    }
}