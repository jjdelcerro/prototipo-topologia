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
package org.gvsig.topology.lib.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyManager;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyRuleFactory;
import org.gvsig.topology.lib.api.TopologyServices;

/**
 *
 * @author jjdelcerro
 */
public class DefaultTopologyManager implements TopologyManager {

    private final List<TopologyRuleFactory> factories;
    private TopologyServices services;
    
    public DefaultTopologyManager() {
        this.factories = new ArrayList<>();
    }
    
    @Override
    public TopologyPlan createTopologyPlan() {
        TopologyPlan plan = new DefaultTopologyPlan(this, this.services);
        return plan;
    }

    @Override
    public List<TopologyRuleFactory> getRuleFactories() {
        return Collections.unmodifiableList(this.factories);
    }

    @Override
    public List<TopologyRuleFactory> getRuleFactories(TopologyDataSet dataSet) {
        List<TopologyRuleFactory>  f = new ArrayList<>();
        for (TopologyRuleFactory factory : this.factories) {
            if( factory.canApplyToDataSet(dataSet) ) {
                f.add(factory);
            }
        }
        return f;
    }

    @Override
    public TopologyRuleFactory getRulefactory(String id) {
        for (TopologyRuleFactory factory : this.factories) {
            if( StringUtils.equalsIgnoreCase(id, factory.getId()) ) {
                return factory;
            }
        }
        return null;
    }
    
    @Override
    public void addRuleFactories(TopologyRuleFactory factory) {
        this.factories.add(factory);
    }

    @Override
    public TopologyDataSet createDataSet(String name, FeatureStore store) {
        TopologyDataSet dataSet = new DefaultTopologyDataSet(this.services, name, store);
        return dataSet;
    }
    
    @Override
    public void setDefaultServices(TopologyServices services) {
        this.services = services;
    }

    @Override
    public TopologyServices getDefaultServices() {
        return this.services;
    }
    
}
