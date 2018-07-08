
package org.gvsig.topology.swing.impl;

import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.swing.api.JTopologyPlanProperties;
import org.gvsig.topology.swing.api.JTopologyReport;
import org.gvsig.topology.swing.api.TopologySwingManager;
import org.gvsig.topology.swing.api.TopologySwingServices;

/**
 *
 * @author jjdelcerro
 */
public class DefaultTopologySwingManager implements TopologySwingManager {

    @Override
    public JTopologyPlanProperties createJTopologyPlan(TopologySwingServices services) {
        DefaultJTopologyPlanProperties x = new DefaultJTopologyPlanProperties(services);
        return x;
    }

    @Override
    public JTopologyReport createJTopologyReport(TopologyPlan plan) {
        DefaultJTopologyReport x = new DefaultJTopologyReport(plan);
        return x;
    }
    
}
