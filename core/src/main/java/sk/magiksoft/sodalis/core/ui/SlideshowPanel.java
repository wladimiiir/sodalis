package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

/**
 * @author wladimiiir
 */
public class SlideshowPanel extends JPanel {

    private final NextPageAction nextPageAction = new NextPageAction();
    private final PreviousPageAction previousPageAction = new PreviousPageAction();
    private int currentPage = 0;
    private URL[] htmlPages;
    private JTextPane tpnPage;

    public SlideshowPanel(URL[] htmlPages) {
        this.htmlPages = htmlPages;
        initComponents();
        setPage(0);
    }

    protected void initComponents() {
        JPanel tpnPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        GridBagConstraints c = new GridBagConstraints();
        JButton nextButton = new JButton(nextPageAction);
        JButton previousButton = new JButton(previousPageAction);
        JScrollPane scrollPane = new JScrollPane(tpnPage = new JTextPane());

        setLayout(new BorderLayout());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        tpnPage.setEditable(false);
        tpnPage.setEditorKit(new HTMLEditorKit());
        tpnPage.setOpaque(false);
        tpnPage.setPreferredSize(new Dimension(700, 500));
//        tpnPage.setSize(new Dimension(640, 50));

        tpnPanel.setOpaque(false);
        tpnPanel.add(scrollPane, c);
        buttonPanel.setOpaque(false);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        nextButton.setFocusPainted(false);
        nextButton.setOpaque(false);
        nextButton.setBorderPainted(false);
        previousButton.setFocusPainted(false);
        previousButton.setOpaque(false);
        previousButton.setBorderPainted(false);

        add(tpnPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setPage(int page) {
        try {
            tpnPage.setPage(htmlPages[page]);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(getClass(), ex);
        }

        nextPageAction.setEnabled(currentPage + 1 < htmlPages.length);
        previousPageAction.setEnabled(currentPage > 0);
    }

    private class NextPageAction extends AbstractAction {

        public NextPageAction() {
            super("⊳");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setPage(++currentPage);
        }
    }

    private class PreviousPageAction extends AbstractAction {

        public PreviousPageAction() {
            super("⊲");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setPage(--currentPage);
        }
    }
}
