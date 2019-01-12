package org.gvsig.topology.swing.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.lib.api.TopologyReportLine;
import org.gvsig.topology.swing.api.JTopologyReport;
import org.gvsig.topology.swing.api.TopologySwingLocator;
import org.gvsig.topology.swing.api.TopologySwingServices;

/**
 *
 * @author jjdelcerro
 */
@SuppressWarnings("UseSpecificCatch")
public class DefaultJTopologyReport
        extends DefaultJTopologyReportView
        implements JTopologyReport {

    private ReportTable linesModel;
    private final TopologySwingServices services;


    public DefaultJTopologyReport(TopologyPlan plan) {
        this.services = TopologySwingLocator.getTopologySwingManager().getDefaultServices();
        this.initComponents();
    }

    private void initComponents() {
        this.linesModel = new ReportTable();
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
        this.setPreferredSize(new Dimension(680, 270));
    }

    @Override
    public void put(TopologyReport report) {
        this.linesModel.setReport(report);
    }

    @Override
    public JComponent asJComponent() {
        return this;
    }

    private void doZoom() {
        int n = this.tblErrors.getSelectedRow();
        if( n<0 ) {
            return;
        }
        TopologyReportLine line = this.linesModel.getLine(n);
        Geometry geom = line.getGeometry();
        this.services.zoomTo(geom.getEnvelope());
    }
    
    private void doCenter() {
        try {
            int n = this.tblErrors.getSelectedRow();
            if( n<0 ) {
                return;
            }
            TopologyReportLine line = this.linesModel.getLine(n);
            Geometry geom = line.getGeometry();
            this.services.centerTo(geom.centroid());
        } catch (Exception ex) {
        }
    }
    
    private static class ReportTable implements TableModel {

        private TopologyReport report;
        private List<TopologyReportLine> lines;
        private final String[] columnNames;
        private final Class[] columnClasses;
        private final Set<TableModelListener> tableListeners;
        private final ChangeListener reportListener;

        public ReportTable() {
            this.report = null;
            this.lines = null;
            this.tableListeners = new HashSet<>();
            this.columnNames = new String[]{
                "Rule",
                "Dataset1",
                "Dataset2",
                "Exception",
                "Description"
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
                public void stateChanged(ChangeEvent e) {
                    fireTableChanged();
                }
            };
        }

        public void setReport(TopologyReport report) {
            if (this.report != null) {
                this.report.removeChangeListener(this.reportListener);
            }
            this.report = report;
            this.lines = this.report.getLines();
            this.report.addChangeListener(this.reportListener);
            this.fireTableChanged();
        }
        
        public TopologyReport getReport() {
            return this.report;
        }
        @Override
        public int getRowCount() {
            if( this.lines == null ) {
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
            if( this.lines == null ) {
                return "";
            }
            TopologyReportLine line = this.lines.get(rowIndex);
            switch(columnIndex) {
                case 0:
                    return line.getRule().getName();
                case 1:
                    return line.getDataSet1().getName();
                case 2:
                    if( line.getDataSet2()==null ) {
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
