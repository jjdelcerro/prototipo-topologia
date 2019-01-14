package org.gvsig.topology.swing.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.commons.lang3.StringUtils;
import org.gvsig.expressionevaluator.ExpressionBuilder;
import org.gvsig.expressionevaluator.ExpressionEvaluatorLocator;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynform.JDynForm;
import org.gvsig.tools.i18n.I18nManager;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.swing.api.ListElement;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.tools.swing.api.ToolsSwingManager;
import org.gvsig.tools.swing.api.task.TaskStatusController;
import org.gvsig.tools.task.TaskStatus;
import org.gvsig.topology.lib.api.TopologyLocator;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.lib.api.TopologyReportLine;
import org.gvsig.topology.lib.api.TopologyReportLineSet;
import org.gvsig.topology.lib.api.TopologyRule;
import org.gvsig.topology.lib.api.TopologyRuleAction;
import org.gvsig.topology.lib.api.TopologyRuleFactory;
import org.gvsig.topology.swing.api.JTopologyReport;
import org.gvsig.topology.swing.api.TopologySwingLocator;
import org.gvsig.topology.swing.api.TopologySwingServices;
import org.gvsig.topology.swing.api.TopologySwingServices.WorkingAreaChangedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjdelcerro
 */
@SuppressWarnings("UseSpecificCatch")
public class DefaultJTopologyReport
        extends DefaultJTopologyReportView
        implements JTopologyReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJTopologyReport.class);

    
    private class TopologyRuleActionParametersListener implements ActionListener {

        private final TopologyRule rule;
        private final TopologyReportLine line;
        private final TopologyRuleAction action;
        private final JDynForm form;

        public TopologyRuleActionParametersListener(
                TopologyRule rule,
                TopologyReportLine line,
                TopologyRuleAction action,
                JDynForm form
        ) {
            this.action = action;
            this.line = line;
            this.rule = rule;
            this.form = form;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doExecuteRuleAction(rule, line, action, form);
        }
    }


    private class TopologyRuleActionListener implements ActionListener {

        private final TopologyRule rule;
        private final TopologyReportLine line;
        private final TopologyRuleAction action;

        public TopologyRuleActionListener(
                TopologyRule rule,
                TopologyReportLine line,
                TopologyRuleAction action
        ) {
            this.action = action;
            this.line = line;
            this.rule = rule;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            if ( this.action.hasParameters() ) {
                doShowActionParametersPanel(this.rule, this.line, this.action);
//            } else {
//                doExecuteRuleAction(rule, line, action, null);
//            }
        }
    }
    
    private ReportTable linesModel;
    private final TopologySwingServices services;
    private TaskStatusController taskStatusController;
    private TopologyPlan plan;
    private WorkingAreaChangedListener workingAreaChangedListener;

    public DefaultJTopologyReport(TopologyPlan plan) {
        this.services = TopologySwingLocator.getTopologySwingManager().getDefaultServices();
        this.initComponents();
    }

    private void initComponents() {
        I18nManager i18n = ToolsLocator.getI18nManager();
        this.linesModel = new ReportTable();
        this.workingAreaChangedListener = new WorkingAreaChangedListener() {
            @Override
            public void workingAreaChanged(Envelope workingArea) {
                doUpdateFilter();
            }
        };
        this.tblErrors.setModel(this.linesModel);
        this.tblErrors.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        this.btnZoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doZoom();
            }
        });
        this.btnCenter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCenter();
            }
        });
        this.btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doExecutePlan();
            }
        });
        this.tblErrors.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                doRowSelected();
            }
        });
        this.btnActions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSelectAction();
            }
        });
        this.btnParametersCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabData.setEnabledAt(0, true);
                tabData.setEnabledAt(1, false);
                tabData.setSelectedIndex(0);
                pnlParameters.removeAll();
            }
        });
        this.btnShowErrors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doUpdateFilter();
            }
        });
        this.btnShowExceptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doUpdateFilter();
            }
        });
        this.btnVisibleExtentOnly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doUpdateFilter();
            }
        });
        DefaultComboBoxModel<ListElement<TopologyRuleFactory>> modelRules = new DefaultComboBoxModel<>();
        modelRules.addElement(new ListElement<>(i18n.getTranslation("_Any_rule"), (TopologyRuleFactory)null));
        List<TopologyRuleFactory> factories = TopologyLocator.getTopologyManager().getRuleFactories();
        for (TopologyRuleFactory factory : factories) {
            modelRules.addElement(new ListElement<>(factory.getName(), factory));
        }
        this.cboRules.setModel(modelRules);
        this.cboRules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llama al invokeLater para poder debugguear
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        doUpdateFilter();
                    }
                });
            }
        });
        this.taskStatusController = ToolsSwingLocator.getTaskStatusSwingManager().createTaskStatusController(
                null,
                this.lblTaskStatusTitle,
                this.lblTaskStatusMessage,
                this.pbTaskStatusProgress,
                this.btnTaskStatusCancel,
                null
        );
        this.translate();
        this.setPreferredSize(new Dimension(700, 200));

        this.btnShowErrors.setSelected(false);
        this.btnShowExceptions.setSelected(false);
        this.btnVisibleExtentOnly.setSelected(false);
        
        this.tabData.setEnabledAt(1, false);

    }

    private void translate() {
        ToolsSwingManager tsm = ToolsSwingLocator.getToolsSwingManager();
        tsm.translate(this.btnActions);
        tsm.translate(this.btnCenter);
        tsm.translate(this.btnRefresh);
        tsm.translate(this.btnShowErrors);
        tsm.translate(this.btnShowExceptions);
        tsm.translate(this.btnZoom);
        tsm.translate(this.btnVisibleExtentOnly);
        tsm.translate(this.btnParametersAccept);
        tsm.translate(this.btnParametersCancel);
        tsm.translate(this.lblShow);
        tsm.translate(this.tabData);
    }

    @Override
    public void put(TopologyPlan plan) {
        this.plan = plan;
        this.taskStatusController.bind(this.plan.getTaskStatus());
        this.plan.getTaskStatus().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object o1) {
                doTaskStatusUpdated(o, o1);
            }
        });
        this.linesModel.setReport(plan.getReport());
    }

    @Override
    public JComponent asJComponent() {
        return this;
    }

    private void doUpdateFilter() {
        this.linesModel.setFilter(this.getFilter());
    }

    private String getFilter() {
        ExpressionBuilder builder = ExpressionEvaluatorLocator.getManager().createExpressionBuilder();
        TopologyRuleFactory ruleFactory = (TopologyRuleFactory) ListElement.getSelected(cboRules);
        if( ruleFactory != null ) {
            builder.setValue(
                builder.eq(
                    builder.column(TopologyReport.RULE_ID),
                    builder.constant(ruleFactory.getId())
                )
            );
        }
        if( this.btnShowErrors.isSelected() ) {
            if( btnShowExceptions.isSelected()) {
                builder.and(
                        builder.or(
                            builder.column(TopologyReport.IS_ERROR), 
                            builder.column(TopologyReport.IS_EXCEPTION)
                        )
                );
            } else {
                builder.and(
                    builder.column(TopologyReport.IS_ERROR)
                );
            }
        } else if( btnShowExceptions.isSelected()) {
            builder.and(
                builder.column(TopologyReport.IS_EXCEPTION)
            );
        }
        if( this.btnVisibleExtentOnly.isSelected() ) {
            Envelope workingArea = this.services.getWorkingArea();
            if( workingArea!=null ) {
                builder.and(
                        builder.ST_Intersects(
                                builder.column(TopologyReport.GEOMETRY), 
                                builder.geometry(workingArea.getGeometry())
                        )
                );
            }
            this.services.addWorkingAreaChangedListener(this.workingAreaChangedListener);
        } else {
            this.services.removeWorkingAreaChangedListener(this.workingAreaChangedListener);
        }
        if( builder.getValue()==null ) {
            return null;
        }
        return builder.toString();
    }
    
    private void doExecutePlan() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                plan.execute();
            }
        }, "TopologyPlan-" + plan.getName());
        th.start();
    }

    private void doTaskStatusUpdated(final Observable observable, final Object notification) {
        if (observable != null && !(observable instanceof TaskStatus)) {
            return;
        }
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    doTaskStatusUpdated(observable, notification);
                }
            });
            return;
        }
        TaskStatus taskStatus = (TaskStatus) observable;
        if (!taskStatus.isRunning()) {
            this.lblTaskStatusMessage.setVisible(false);
            this.lblTaskStatusTitle.setVisible(false);
            this.pbTaskStatusProgress.setVisible(false);
            this.btnTaskStatusCancel.setVisible(false);
            this.btnRefresh.setEnabled(true);
            return;
        }
        if (!this.pbTaskStatusProgress.isVisible()) {
            this.lblTaskStatusMessage.setVisible(true);
            this.lblTaskStatusTitle.setVisible(true);
            this.pbTaskStatusProgress.setVisible(true);
            this.btnTaskStatusCancel.setVisible(true);
            this.btnRefresh.setEnabled(false);
        }
    }

    private void doSelectAction() {
        int n = this.tblErrors.getSelectedRow();
        if (n < 0) {
            return;
        }
        TopologyReportLine line = this.linesModel.getLine(n);
        TopologyRule rule = line.getRule();
        List<TopologyRuleAction> actions = rule.getActions();
        if (actions == null || actions.isEmpty()) {
            return;
        }
        JPopupMenu menu = new JPopupMenu();
        for (TopologyRuleAction action : actions) {
            JMenuItem item = new JMenuItem(action.getName());
            item.addActionListener(new TopologyRuleActionListener(rule, line, action));
            menu.add(item);
        }
        menu.show(this.btnActions, 0, this.btnActions.getHeight());
    }

    private void doExecuteRuleAction(
            TopologyRule rule,
            TopologyReportLine line,
            TopologyRuleAction action,
            JDynForm form
    ) {
//        DynObject parameters = null;
//        if( form!=null ) {
//            parameters = action.createParameters();
//            form.getValues(parameters);
//        }
//        action.execute(rule, line, parameters);
        tabData.setEnabledAt(0, true);
        tabData.setEnabledAt(1, false);
        tabData.setSelectedIndex(0);
        pnlParameters.removeAll();
    }

    private void doShowActionParametersPanel(
            TopologyRule rule,
            TopologyReportLine line,
            TopologyRuleAction action
    ) {
        I18nManager i18n = ToolsLocator.getI18nManager();
        this.tabData.setEnabledAt(0, false);
        this.tabData.setEnabledAt(1, true);
        this.tabData.setSelectedIndex(1);

        try {
            JDynForm form = null;
            this.lblActionTitle.setText(
                    "<html>" +
                    i18n.getTranslation("_Rule") + ": <b>" +
                    rule.getName() + "</b>, " +
                    i18n.getTranslation("_Action") + ": <b>" +
                    action.getName() + "</b></html>"
            );
            this.lblActionDescription.setText("<html>"+action.getShortDescription()+"</html>");
//            DynObject parameters = action.createParameters();
//            form = DynFormLocator.getDynFormManager().createJDynForm(parameters);
//            this.pnlParameters.setLayout(new BorderLayout());
//            this.pnlParameters.removeAll();
//            this.pnlParameters.add(form.asJComponent(), BorderLayout.CENTER);
//            this.pnlParameters.revalidate();
//            this.pnlParameters.repaint();
            this.btnParametersAccept.addActionListener(
                new TopologyRuleActionParametersListener(rule, line, action, form)
            );
        } catch (Exception ex) {
            LOGGER.warn("Can't show action parameters panel.",ex);
        }
    }

    private void doZoom() {
        int n = this.tblErrors.getSelectedRow();
        if (n < 0) {
            return;
        }
        TopologyReportLine line = this.linesModel.getLine(n);
        Geometry geom = line.getGeometry();
        this.services.zoomTo(geom.getEnvelope());
    }

    private void doCenter() {
        try {
            int n = this.tblErrors.getSelectedRow();
            if (n < 0) {
                return;
            }
            TopologyReportLine line = this.linesModel.getLine(n);
            Geometry geom = line.getGeometry();
            this.services.centerTo(geom.centroid());
        } catch (Exception ex) {
            LOGGER.warn("Can't center topology error", ex);
        }
    }

    private void doRowSelected() {
        try {
            int n = this.tblErrors.getSelectedRow();
            if (n < 0) {
                return;
            }
            TopologyReportLine line = this.linesModel.getLine(n);
            if (line.getFeature1() == null) {
                return;
            }
            FeatureSelection selection = line.getDataSet1().getStore().getFeatureSelection();
            selection.deselectAll();
            selection.select(line.getFeature1());
        } catch (Exception ex) {
            LOGGER.warn("Can't select topology error", ex);
        }
    }

    private static class ReportTable implements TableModel {

        private TopologyReport report;
        private TopologyReportLineSet lines;
        private final String[] columnNames;
        private final Class[] columnClasses;
        private final Set<TableModelListener> tableListeners;
        private final ChangeListener reportListener;
        private String lastFilter;

        public ReportTable() {
            I18nManager i18n = ToolsLocator.getI18nManager();
            this.report = null;
            this.lines = null;
            this.tableListeners = new HashSet<>();
            this.columnNames = new String[]{
                i18n.getTranslation("_Rule"),
                i18n.getTranslation("_Dataset1"),
                i18n.getTranslation("_Dataset2"),
                i18n.getTranslation("_Exception"),
                i18n.getTranslation("_Description")
            };
            this.columnClasses = new Class[]{
                String.class,
                String.class,
                String.class,
                Boolean.class,
                String.class
            };
            this.reportListener = new ChangeListener() {
                @Override
                public void stateChanged(final ChangeEvent e) {
                    if( !SwingUtilities.isEventDispatchThread() ) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                stateChanged(e);
                            }
                        });
                        return;
                    }
                    fireTableChanged();
                }
            };
        }

        public void setReport(TopologyReport report) {
            if (this.report != null) {
                this.report.removeChangeListener(this.reportListener);
            }
            this.report = report;
            this.lines = this.report;
            this.lines.addChangeListener(this.reportListener);
        }

        public void setFilter(String filter) {
            if( StringUtils.equals(filter, this.lastFilter) ) {
                return;
            }
            this.lines = this.report.getLineSet(filter);
            this.lines.addChangeListener(this.reportListener);
            this.lastFilter = filter;
            this.fireTableChanged();
        }
        
        public TopologyReport getReport() {
            return this.report;
        }

        @Override
        public int getRowCount() {
            if (this.lines == null) {
                return 0;
            }
            return this.lines.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return this.columnNames[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return this.columnClasses[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (this.lines == null) {
                return "";
            }
            TopologyReportLine line = this.lines.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return line.getRule().getName();
                case 1:
                    return line.getDataSet1().getName();
                case 2:
                    if (line.getDataSet2() == null) {
                        return "";
                    }
                    return line.getDataSet2().getName();
                case 3:
                    return line.isException();
                case 4:
                    return line.getDescription();
                default:
                    return "???";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            this.tableListeners.add(l);
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            this.tableListeners.remove(l);
        }

        private void fireTableChanged() {
            for (TableModelListener tableListener : this.tableListeners) {
                tableListener.tableChanged(new TableModelEvent(this));
            }
        }

        public TopologyReportLine getLine(int lineNum) {
            return this.lines.get(lineNum);
        }

    }
}
