package org.gvsig.topology.lib.impl;

import java.util.ArrayList;
import java.util.List;
import org.gvsig.expressionevaluator.spi.AbstractSymbolTable;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.lib.api.TopologyReportLine;

/**
 *
 * @author jjdelcerro
 */
class TopologyReportLineSymbolTable extends AbstractSymbolTable {
    
    private TopologyReportLine line = null;
    private final List<String> fieldNames;

    public TopologyReportLineSymbolTable() {
        this.fieldNames = new ArrayList<>();
        this.fieldNames.add(TopologyReport.RULE_ID);
        this.fieldNames.add(TopologyReport.IS_ERROR);
        this.fieldNames.add(TopologyReport.IS_EXCEPTION);
        this.fieldNames.add(TopologyReport.GEOMETRY);
    }

    public void setLine(TopologyReportLine line) {
        this.line = line;
    }

    @Override
    public boolean exists(String name) {
        if (name == null) {
            return false;
        }
        return this.fieldNames.contains(name.toUpperCase());
    }

    @Override
    public Object value(String name) {
        switch (name.toUpperCase()) {
            case TopologyReport.RULE_ID:
                return this.line.getRule().getId();
            case TopologyReport.IS_ERROR:
                return !this.line.isException();
            case TopologyReport.IS_EXCEPTION:
                return this.line.isException();
            case TopologyReport.GEOMETRY:
                return this.line.getGeometry();
            default:
                return null;
        }
    }
    
}
