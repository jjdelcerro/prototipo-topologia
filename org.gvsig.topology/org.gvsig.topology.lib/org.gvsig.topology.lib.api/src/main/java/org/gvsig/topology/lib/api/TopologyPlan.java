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

import java.util.Collection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.tools.task.SimpleTaskStatus;

/**
 *
 * @author jjdelcerro
 */
public interface TopologyPlan extends SerializableJSON {

    
    public String getName();
    
    public void setName(String name);
    
    public double getTolerance();
    
    public void setTolerance(double tolerance);

    public void clear();

    public void setTopologyServices(TopologyServices services);

    public TopologyServices getTopologyServices();

    public SimpleTaskStatus getTaskStatus();
    
    public void execute();
    
    public TopologyDataSet addDataSet(String name, FeatureStore store);

    public TopologyDataSet addDataSet(TopologyDataSet dataSet);
    
    public void removeDataSet(TopologyDataSet dataSet);
    
    public boolean containsDataSet(String name);
 
    public TopologyDataSet getDataSet(String name);
    
    public Collection<TopologyDataSet> getDataSets();
    
    public Collection<TopologyDataSet> getSecondaryDataSets(TopologyRuleFactory ruleFactory);
    
    public TopologyRule addRule(String id, String dataSet1, String dataSet2, double tolerance);
    
    public TopologyRule addRule(TopologyRule rule);
    
    public void removeRule(TopologyRule rule);
 
    public Collection<TopologyRule> getRules();
    
    public TopologyReport getReport();

}
