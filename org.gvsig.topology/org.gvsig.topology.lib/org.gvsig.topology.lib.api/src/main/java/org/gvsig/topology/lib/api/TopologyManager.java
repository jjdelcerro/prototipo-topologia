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
package org.gvsig.topology.lib.api;

import java.util.List;
import org.gvsig.fmap.dal.feature.FeatureStore;

/**
 *
 * @author jjdelcerro
 */
public interface TopologyManager {
    
    public TopologyPlan createTopologyPlan(TopologyServices callback);
    
    public List<TopologyRuleFactory> getRuleFactories();

    public TopologyRuleFactory getRulefactory(String id);
    
    public List<TopologyRuleFactory> getRuleFactories(TopologyDataSet dataSet);

    public void addRuleFactories(TopologyRuleFactory factory);

    public TopologyDataSet createDataSet(TopologyServices services, String name, FeatureStore store);
    
}
