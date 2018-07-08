
package org.gvsig.topology.swing.impl;

import javax.swing.JComponent;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.swing.api.JTopologyReport;

/**
 *
 * @author jjdelcerro
 */
public class DefaultJTopologyReport 
        extends DefaultJTopologyReportView 
        implements JTopologyReport
    {

    public DefaultJTopologyReport(TopologyPlan plan) {
        
    }
    
    @Override
    public void put(TopologyReport report) {

    }

    @Override
    public JComponent asJComponent() {
        return this;
    }
    
}
