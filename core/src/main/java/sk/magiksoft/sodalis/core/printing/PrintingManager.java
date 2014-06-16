
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.printing;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.*;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.DJGroupVariable;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author wladimiiir
 */
public class PrintingManager {

    public static String GROUP_FIELD_NAME_PREFIX = "group.";
    private static final String TEMP_PAGE_HEADER_IMAGE_FILENAME = "data/temp/pageHeader.bmp";
    public static final int PAGE_HEADER_HEIGHT = 150;
    public static final int A4_WIDTH = 595;
    public static final int A4_HEIGHT = 842;
    private static PrintingManager instance = null;

    private PrintingManager() {
        instance = this;
    }

    public static synchronized PrintingManager getInstance() {
        if (instance == null) {
            new PrintingManager();
        }

        return instance;
    }

    public void viewReport(final String jrxmlFile, final JRDataSource dataSource) throws JRException {
        viewReport(JasperFillManager.fillReport(JasperCompileManager.compileReport(jrxmlFile), new HashMap(), dataSource));
    }

    public void viewReport(final JasperPrint jasperPrint) throws JRException {
        final JasperViewer viewer = new JasperViewer(jasperPrint, false);

        viewer.setTitle(LocaleManager.getString("JasperViewer.title"));
        viewer.setIconImage(((ImageIcon) IconFactory.getInstance().getIcon("application")).getImage());
        viewer.setSize(870, 600);
        viewer.setLocationRelativeTo(null);
        viewer.setVisible(true);
        viewer.repaint();
    }


    private Style createDetailStyle() {
        final Style detailStyle = createDefaultStyle();
        detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        detailStyle.setBorderTop(Border.THIN);
        detailStyle.setBorderBottom(Border.THIN);
        return detailStyle;
    }

    private Style createHeaderStyle() {
        final Style headerStyle = createDefaultStyle();
        headerStyle.setFont((Font) Font.ARIAL_MEDIUM_BOLD.clone());
        headerStyle.getFont().setFontSize(12);
        headerStyle.getFont().setPdfFontEmbedded(true);
        headerStyle.getFont().setPdfFontEncoding(LocaleManager.getString("jasper.pdfEncoding"));
        headerStyle.setBackgroundColor(Color.gray);
        headerStyle.setTextColor(Color.white);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);
        headerStyle.setBorder(Border.THIN);
        return headerStyle;
    }

    private Style createGroupStyle() {
        final Style groupStyle = createDefaultStyle();
        groupStyle.setFont(new Font(15, Font._FONT_VERDANA, true));
        groupStyle.getFont().setPdfFontEmbedded(true);
        groupStyle.getFont().setPdfFontEncoding(LocaleManager.getString("jasper.pdfEncoding"));
        groupStyle.setHorizontalAlign(HorizontalAlign.LEFT);
        groupStyle.setTextColor(new Color(50, 50, 50));
        groupStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        return groupStyle;
    }

    public void printTableReport(final TablePrintDataSource dataSource, Map<String, String> groups) throws JRException {
        final DynamicReportBuilder drb = new DynamicReportBuilder();
        final Integer margin = 30;

        if (dataSource.getPageHeaderImage() != null) {
            saveImage(dataSource.getPageHeaderImage(), TEMP_PAGE_HEADER_IMAGE_FILENAME);
            drb.addFirstPageImageBanner(TEMP_PAGE_HEADER_IMAGE_FILENAME,
                    A4_WIDTH, dataSource.getPageHeaderImage().getHeight(),
                    ImageBanner.ALIGN_CENTER);
        } else if (dataSource.getPageHeaderComponent() != null) {
            saveImage(getComponentImage(dataSource.getPageHeaderComponent(), A4_WIDTH - 2 * margin, PAGE_HEADER_HEIGHT), TEMP_PAGE_HEADER_IMAGE_FILENAME);
            drb.addFirstPageImageBanner(TEMP_PAGE_HEADER_IMAGE_FILENAME,
                    A4_WIDTH - 2 * margin, PAGE_HEADER_HEIGHT,
                    ImageBanner.ALIGN_CENTER);
        }

        drb.setGrandTotalLegend("");
//        drb.setGrandTotalLegend(LocaleManager.getString("grandTotal"));
        drb.setGrandTotalLegendStyle(createGrandTotalStyle());
        drb.setGlobalFooterVariableHeight(30);
        drb.setAllowDetailSplit(false);

        final List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>(groups.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, String>>() {

            @Override
            public int compare(Entry<String, String> o1, Entry<String, String> o2) {
                return Collator.getInstance().compare(o1.getKey(), o2.getKey());
            }
        });
        for (int index = 0, entriesSize = entries.size(); index < entriesSize; index++) {
            final Entry<String, String> entry = entries.get(index);
            AbstractColumn groupColumn;

            try {
                final Style style = createGroupStyle();
                style.getFont().setFontSize(style.getFont().getFontSize() - index);
                style.setPaddingLeft(5 + (index * 10));
                style.setPaddingTop(index == 0 ? 10 : 5);
                style.setPaddingBottom(index == entriesSize - 1 ? 3 : 0);

                groupColumn = ColumnBuilder.getNew()
                        .setColumnProperty(entry.getKey(), String.class.getName())
                        .setTitle(entry.getValue()).setWidth(100)
                        .setStyle(style).setHeaderStyle(createHeaderStyle())
                        .build();

                drb.addColumn(groupColumn);

                final GroupBuilder gb = new GroupBuilder();
                gb.setCriteriaColumn((PropertyColumn) groupColumn)
                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
                        .setAllowSplitting(false, false);

                final DJGroup group = gb.build();
                group.setDefaulHeaderVariableStyle(style);
//                group.setHeaderHeight(group.getHeaderHeight());
                drb.addGroup(group);
            } catch (ColumnBuilderException ex) {
                LoggerManager.getInstance().error(getClass(), ex);
            }
        }
        for (final TableColumnWrapper wrapper : dataSource.getColumns()) {
            AbstractColumn column;
            final Style detailStyle = createDetailStyle();

            switch (wrapper.getAlignment()) {
                case LEFT:
                    detailStyle.setHorizontalAlign(HorizontalAlign.LEFT);
                    break;
                case CENTER:
                    detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
                    break;
                case RIGHT:
                    detailStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
                    break;
            }
            try {
                final ColumnProperty property = new ColumnProperty(wrapper.getKey(), String.class);
                property.addFieldProperty("markup", "html");
                property.addFieldProperty("isStyledText", "true");
                column = ColumnBuilder.getNew()
                        .setColumnProperty(property)
                        .setCustomExpression(new CustomExpression() {
                            @Override
                            public Object evaluate(Map fields, Map variables, Map parameters) {
                                return fields.get(wrapper.getKey());
                            }

                            @Override
                            public String getClassName() {
                                return String.class.getName();
                            }
                        })
                        .setCustomExpressionForCalculation(new CustomExpression() {
                            @Override
                            public Object evaluate(Map fields, Map variables, Map parameters) {
                                final Object value = fields.get(wrapper.getKey());

                                if (value instanceof Double) {
                                    return value;
                                } else {
                                    try {
                                        return Double.valueOf(value.toString().replace(",", "."));
                                    } catch (NumberFormatException e) {
                                        return 0d;
                                    }
                                }
                            }

                            @Override
                            public String getClassName() {
                                return Double.class.getName();
                            }
                        })
                        .setTitle(wrapper.getHeaderValue())
                        .setWidth(wrapper.getWidth())
                        .setStyle(detailStyle)
                        .setHeaderStyle(createHeaderStyle())
                        .build();
                if (wrapper.isSum()) {
                    final Style grandTotalStyle = createGrandTotalStyle();
                    grandTotalStyle.setHorizontalAlign(column.getStyle().getHorizontalAlign());
                    drb.addGlobalFooterVariable(column, DJCalculation.SUM, grandTotalStyle, new DJValueFormatter() {
                        @Override
                        public Object evaluate(Object value, Map fields, Map variables, Map parameters) {
                            return value instanceof Double ? dataSource.getDoubleFormatter().format(value) : value.toString();
                        }

                        @Override
                        public String getClassName() {
                            return String.class.getName();
                        }
                    });
                    for (int index = 0; index < groups.size(); index++) {
                        final Style style = createGrandTotalStyle();
                        style.setVerticalAlign(VerticalAlign.BOTTOM);
                        style.setPaddingBottom(drb.getGroup(index).getDefaulHeaderVariableStyle().getPaddingBottom());
                        style.setHorizontalAlign(column.getStyle().getHorizontalAlign());
                        drb.getGroup(index).addHeaderVariable(new DJGroupVariable(column, DJCalculation.SUM, style, new DJValueFormatter() {
                            @Override
                            public Object evaluate(Object value, Map fields, Map variables, Map parameters) {
                                return value instanceof Double ? dataSource.getDoubleFormatter().format(value) : value.toString();
                            }

                            @Override
                            public String getClassName() {
                                return String.class.getName();
                            }
                        }));
                    }
                }
                drb.addColumn(column);
            } catch (ColumnBuilderException ex) {
                LoggerManager.getInstance().error(PrintingManager.class, ex);
            }
        }

        if (dataSource.getTotalCountLabel() != null) {
            final Style style = createGrandTotalStyle();
            style.getFont().setItalic(true);
            style.setBorderBottom(Border.THIN);

            drb.addAutoText(dataSource.getTotalCountLabel() + ' ' + getItemCount(dataSource.getDataSource()),
                    AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, AutoText.WIDTH_NOT_SET, style);
            dataSource.getDataSource().moveFirst();
        }

        if (dataSource.isShowPageNumbers()) {
            drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_SLASH_Y, AutoText.POSITION_FOOTER, AutoText.ALIGMENT_CENTER, 50, 50);
        }

        drb.setUseFullPageWidth(true);
        drb.setAllowDetailSplit(true);
        drb.setIgnorePagination(false);
        drb.setMargins(margin, margin, margin, margin);

        viewReport(DynamicJasperHelper.generateJasperPrint(drb.build(), new ClassicLayoutManager(), dataSource.getDataSource()));
    }

    private int getItemCount(JRDataSource dataSource) {
        int count = 0;
        try {
            while (dataSource.next()) {
                count++;
            }
        } catch (JRException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
        return count;
    }

    private Style createGrandTotalStyle() {
        final Style style = createDefaultStyle();
        style.setVerticalAlign(VerticalAlign.MIDDLE);
        style.setFont(new Font(11, Font._FONT_ARIAL, true));
        style.setHorizontalAlign(HorizontalAlign.LEFT);
        return style;
    }

    private Style createDefaultStyle() {
        final Style style = new Style();

        style.setVerticalAlign(VerticalAlign.MIDDLE);
        style.setFont((Font) style.getFont().clone());
        style.getFont().setPdfFontEncoding(LocaleManager.getString("jasper.pdfEncoding"));

        return style;
    }

    private void saveImage(RenderedImage image, String filename) {
        try {
            ImageIO.write(image, "bmp", new File(filename));
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PrintingManager.class, ex);
        }
    }

    private BufferedImage getComponentImage(Component comp, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        comp.setSize(width, height);
        comp.setPreferredSize(new Dimension(width, height));
        comp.paint(image.getGraphics());

        return image;
    }

    public Map<String, String> getCommonFieldValues() {
        final HashMap<String, String> map = new HashMap<String, String>();

        map.put("summary", LocaleManager.getString("summary", String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));

        return map;
    }
}