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

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.topology.lib.api.AbstractTopologyRuleFactory;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyRule;

/**
 *
 * @author jjdelcerro
 */
public class MustBeLargerThanToleranceLineRuleFactory extends AbstractTopologyRuleFactory {

    public MustBeLargerThanToleranceLineRuleFactory() {
        super(
                "MustBeLargerThanToleranceLine", 
                "Must Be Larger Than Tolerance", 
                "Requires that a feature does not collapse during a validate process. This rule is mandatory for a topology and applies to all line feature classes. In instances where this rule is violated, the original geometry is left unchanged", 
                MustBeLargerThanToleranceLineRuleFactory.class.getResource("MustBeLargerThanToleranceLine.png"), 
                Geometry.TYPES.CURVE, 
                Geometry.TYPES.NULL
        );
    }
    
    @Override
    public TopologyRule createRule(TopologyPlan plan) {
        TopologyRule rule = new MustBeLargerThanToleranceLineRule(plan, this);
        return rule;
    }
    
    @Override
    public TopologyRule createRule(TopologyPlan plan, String dataSet1, String dataSet2, double tolerance) {
        TopologyRule rule = new MustBeLargerThanToleranceLineRule(plan, this, tolerance, dataSet1, dataSet2);
        return rule;
    }
    
}
