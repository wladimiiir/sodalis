package sk.magiksoft.sodalis.core.filter.ui;

import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.filter.action.FilterEvent;
import sk.magiksoft.sodalis.core.filter.action.FilterListener;
import sk.magiksoft.sodalis.core.filter.element.ColumnComponent;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.swing.gradient.GradientPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class FilterPanel extends GradientPanel {

    private List<ColumnComponent> columnComponents;
    private JButton btnDoFilter;
    private JButton btnNoFilter;
    private JRadioButton rbtAll;
    private JRadioButton rbtShown;
    private List<FilterListener> listeners = new ArrayList<FilterListener>();
    private JScrollPane scrollPane;

    public FilterPanel(List<ColumnComponent> columnComponents) {
        this.columnComponents = columnComponents;
        initComponents();
        initListeners();
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        scrollPane.getViewport().getView().setPreferredSize(new Dimension(preferredSize.width - 2, preferredSize.height));
    }

    private void initComponents() {
        JPanel columnComponentPanel = new JPanel(new BorderLayout());

        scrollPane = new JScrollPane(columnComponentPanel);
        columnComponentPanel.setOpaque(false);
        scrollPane.setOpaque(false);
        columnComponentPanel.add(createColumnComponentPanel(), BorderLayout.NORTH);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "FILTER_ACTION");
        getActionMap().put("FILTER_ACTION", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnDoFilter.doClick();
            }
        });
    }

    private JPanel createColumnComponentPanel() {
        JPanel columnComponentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel label;

        c.gridx = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        //adding filter components to panel
        for (ColumnComponent columnComponent : columnComponents) {
            c.gridy++;
            c.insets = new Insets(3, 3, 1, 3);
            if (!columnComponent.getLabelText().isEmpty()) {
                label = new JLabel(columnComponent.getLabelText());
                columnComponentPanel.add(label, c);
                c.insets = new Insets(0, 3, 1, 3);
                c.gridy++;
            }
            columnComponentPanel.add(columnComponent.getComponent(), c);
        }
        return columnComponentPanel;
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel filterActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        ButtonGroup btnGroup = new ButtonGroup();

        btnDoFilter = new JButton(LocaleManager.getString("doFilter"),
                IconFactory.getInstance().getIcon("filter")) {
            @Override
            public Point getToolTipLocation(MouseEvent event) {
                return new Point(0, -60);
            }
        };
        btnDoFilter.setToolTipText(LocaleManager.getString("filterInfo"));
        btnNoFilter = new JButton(LocaleManager.getString("noFilter"),
                IconFactory.getInstance().getIcon("noFilter"));
        btnNoFilter.setToolTipText(LocaleManager.getString("showsAllRecords"));
        buttonPanel.add(btnDoFilter);
        buttonPanel.add(btnNoFilter);
        buttonPanel.setOpaque(false);

        rbtAll = new JRadioButton(LocaleManager.getString("filterAll"));
        rbtShown = new JRadioButton(LocaleManager.getString("filterShown"));
        btnGroup.add(rbtAll);
        btnGroup.add(rbtShown);
        rbtAll.setOpaque(false);
        rbtAll.setFocusPainted(false);
        rbtAll.setSelected(true);
        rbtShown.setOpaque(false);
        rbtShown.setFocusPainted(false);

        filterActionPanel.add(rbtAll);
        filterActionPanel.add(rbtShown);
        filterActionPanel.setOpaque(false);

        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        southPanel.add(filterActionPanel, BorderLayout.NORTH);
        southPanel.setBackground(new Color(215, 215, 215));

        return southPanel;
    }

    private void fireQueryChanged(FilterEvent event) {
        for (FilterListener filterListener : listeners) {
            filterListener.queryChanged(event);
        }
    }

    private void initListeners() {
        btnDoFilter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FilterEvent event = rbtAll.isSelected()
                        ? new FilterEvent(getSelectQuery(), getFromQuery(), getWhereQuery(), FilterEvent.ACTION_FILTER_ALL)
                        : new FilterEvent(getSelectQuery(), getFromQuery(), getWhereQuery(), FilterEvent.ACTION_FILTER_SELECTED);

                fireQueryChanged(event);
            }
        });
        btnNoFilter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FilterEvent event = new FilterEvent("", "", "", FilterEvent.ACTION_RESET);
                fireQueryChanged(event);
            }
        });
    }

    private String getSelectQuery() {
        StringBuilder selectQuery = new StringBuilder();
        String[] selects;

        for (ColumnComponent columnComponent : columnComponents) {
            if (!columnComponent.isIncluded()) {
                continue;
            }
            selects = columnComponent.getSelectQuery().split(",");
            for (String select : selects) {
                if (!selectQuery.toString().contains(select.trim())) {
                    if (selectQuery.length() > 0 && !select.trim().isEmpty()) {
                        selectQuery.append(", ");
                    }
                    selectQuery.append(select.trim());
                }
            }
        }

        return selectQuery.length() == 0 ? null : selectQuery.toString();
    }

    private String getFromQuery() {
        StringBuilder fromQuery = new StringBuilder();
        String from;
        String[] froms;

        for (ColumnComponent columnComponent : columnComponents) {
            if (!columnComponent.isIncluded()) {
                continue;
            }
            froms = columnComponent.getFromQuery().split(",");
            for (int i = 0; i < froms.length; i++) {
                from = froms[i].trim();
                if (!fromQuery.toString().contains(from)) {
                    if (fromQuery.length() > 0 && !from.isEmpty()) {
                        fromQuery.append(", ");
                    }
                    fromQuery.append(from);
                }
            }
        }

        return fromQuery.toString();
    }

    private String getWhereQuery() {
        StringBuilder whereQuery = new StringBuilder();
        String where;

        for (ColumnComponent columnComponent : columnComponents) {
            if (!columnComponent.isIncluded()) {
                continue;
            }
            where = columnComponent.getWhereQuery();
            if (whereQuery.length() > 0 && !where.trim().isEmpty()) {
                whereQuery.append(" AND ");
            }
            whereQuery.append("(" + where + ")");
        }

        return whereQuery.toString();
    }

    public void addFilterListener(FilterListener listener) {
        listeners.add(listener);
    }
}
