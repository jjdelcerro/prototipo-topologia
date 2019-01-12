
package org.gvsig.topology.swing.impl;

import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.swing.api.JTopologyPlanProperties;
import org.gvsig.topology.swing.api.JTopologyReport;
import org.gvsig.topology.swing.api.TopologySwingManager;
import org.gvsig.topology.swing.api.TopologySwingServices;

/**
 *
 * @author jjdelcerro
 */
public class DefaultTopologySwingManager implements TopologySwingManager {

    private TopologySwingServices services;

    @Override
    public JTopologyPlanProperties createJTopologyPlan() {
        DefaultJTopologyPlanProperties x = new DefaultJTopologyPlanProperties();
        return x;
    }

    @Override
    public JTopologyReport createJTopologyReport(TopologyPlan plan) {
        DefaultJTopologyReport x = new DefaultJTopologyReport(plan);
        return x;
    }

    @Override
    public void setDefaultServices(TopologySwingServices services) {
        this.services = services;
    }

    @Override
    public TopologySwingServices getDefaultServices() {
        return this.services;
    }
    
}
