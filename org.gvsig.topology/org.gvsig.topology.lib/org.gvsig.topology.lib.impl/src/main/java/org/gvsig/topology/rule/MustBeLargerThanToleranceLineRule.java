/**
 * gvSIG. Desktop Geographic Information System.
 *
 * Copyright (C) 2007-2013 gvSIG Association.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * For any additional information, do not hesitate to contact us
 * at info AT gvsig.com, or visit our website www.gvsig.com.
 */
package org.gvsig.topology.rule;

import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.topology.lib.api.AbstractTopologyRule;
import org.gvsig.topology.lib.api.AbstractTopologyRuleAction;
import org.gvsig.topology.lib.api.ExecuteTopologyRuleActionException;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.lib.api.TopologyReportLine;
import org.gvsig.topology.lib.api.TopologyRule;
import org.gvsig.topology.lib.api.TopologyRuleFactory;

/**
 *
 * @author jjdelcerro
 */
@SuppressWarnings("UseSpecificCatch")
public class MustBeLargerThanToleranceLineRule extends AbstractTopologyRule {

    private class DeleteAction extends AbstractTopologyRuleAction {

        public DeleteAction() {
            super(
                    "delete", 
                    "Delete", 
                    "Removes line features that would collapse during the validate process based on the topology's tolerance."
            );
        }
        
        @Override
        public DynObject createParameters() {
            return null;
        }

        @Override
        public void execute(TopologyRule rule, TopologyReportLine line, DynObject parameters) {
            try {
                TopologyDataSet dataset = line.getDataSet1();
                dataset.delete(line.getFeature1());
            } catch (Exception ex) {
                throw new ExecuteTopologyRuleActionException(ex);
            }
        }
        
    } 
    
    public MustBeLargerThanToleranceLineRule( 
            TopologyPlan plan,
            TopologyRuleFactory factory
    ) {
        super(plan, factory);
        this.actions.add(new DeleteAction());
    }
    
    public MustBeLargerThanToleranceLineRule( 
            TopologyPlan plan,
            TopologyRuleFactory factory,
            double tolerance,
            String dataSet1,
            String dataSet2

    ) {
        super(plan, factory, tolerance, dataSet1, dataSet2);
        this.actions.add(new DeleteAction());
    }
    
    @Override
    public void check(TopologyReport report, Feature feature) throws Exception {
        Geometry geom = feature.getDefaultGeometry();
        if( geom.perimeter()<this.getTolerance() ) {
            report.addLine(this, this.getDataSet1(), null, 
                    geom, feature.getReference(), null, false, 
                    "The length of the line is less than the specified tolerance"
            );
        }
    }


}
