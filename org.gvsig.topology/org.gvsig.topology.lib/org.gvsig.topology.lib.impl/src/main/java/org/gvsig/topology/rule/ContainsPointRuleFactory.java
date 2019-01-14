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
import org.gvsig.tools.util.ListBuilder;
import org.gvsig.topology.lib.spi.AbstractTopologyRuleFactory;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyRule;

/**
 *
 * @author jjdelcerro
 */
public class ContainsPointRuleFactory extends AbstractTopologyRuleFactory {
    
    public ContainsPointRuleFactory() {
        super(
                "ContainsPoint", 
                "Contains Point", 
                "Requires that a polygon in one dataset contain at least one point from another dataset. Points must be within the polygon, not on the boundary. ", 
                new ListBuilder<Integer>()
                        .add(Geometry.TYPES.SURFACE)
                        .add(Geometry.TYPES.MULTISURFACE)
                        .asList(),
                Geometry.TYPES.POINT
        );
    }
    
    @Override
    public TopologyRule createRule(TopologyPlan plan, String dataSet1, String dataSet2, double tolerance) {
        TopologyRule rule = new ContainsPointRule(plan, this, tolerance, dataSet1, dataSet2);
        return rule;
    }    

    @Override
    public TopologyRule createRule(TopologyPlan plan) {
        TopologyRule rule = new ContainsPointRule(plan, this);
        return rule;
    }
}
