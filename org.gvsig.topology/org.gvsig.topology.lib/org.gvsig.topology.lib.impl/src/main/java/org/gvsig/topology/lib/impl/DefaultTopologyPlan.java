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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.task.SimpleTaskStatus;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyManager;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.lib.api.TopologyRule;
import org.gvsig.topology.lib.api.TopologyRuleFactory;
import org.gvsig.topology.lib.api.TopologyServices;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jjdelcerro
 */
public class DefaultTopologyPlan implements TopologyPlan {

    private final TopologyManager manager;
    private final Map<String,TopologyDataSet> dataSets;
    private final List<TopologyRule> rules;

    private String name;
    private TopologyReport report;
    private TopologyServices services;
    private double tolerance;
    
    public DefaultTopologyPlan(TopologyManager manager, TopologyServices services) {
        this.manager = manager;
        this.services = services;
        this.dataSets = new HashMap<>();
        this.rules = new ArrayList<>();
        this.report = null;
        this.name = "TopologyPlan-" + String.format("%08X", new Date().getTime());
        this.tolerance = 0;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void clear() {
        this.name = "";
        this.tolerance = 0;
        this.services = null;
        this.report = null;
        this.dataSets.clear();
        this.rules.clear();
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public void setTopologyServices(TopologyServices services) {
        this.services = services;
    }
    
    @Override
    public TopologyServices getTopologyServices() {
        return this.services;
    }
    
    @Override
    public void execute() {
        SimpleTaskStatus taskStatus = ToolsLocator.getTaskStatusManager()
                .createDefaultSimpleTaskStatus(this.getName());
        
        taskStatus.message("Preparing the execution of the plan");
        taskStatus.setAutoremove(true);
        taskStatus.setIndeterminate();
        long steps = 0;
        for (TopologyRule rule : this.rules) {
            steps += rule.getSteps();
            steps++;
        }
        
        taskStatus.setRangeOfValues(0, steps);
        taskStatus.setCurValue(0);
        for (TopologyRule rule : this.rules) {
            taskStatus.message(rule.getName());
            rule.execute(taskStatus, this.getReport());
            taskStatus.incrementCurrentValue();
        }
    }

    @Override
    public TopologyDataSet addDataSet(String id, String name, FeatureStore store) {
        TopologyDataSet dataSet = manager.createDataSet(services, name, store);
        return this.addDataSet(dataSet);
    }

    @Override
    public TopologyDataSet addDataSet(TopologyDataSet dataSet) {
        if( this.dataSets.containsKey(dataSet.getName()) ) {
            throw new IllegalArgumentException("Already exists a dataSet with this name ("+dataSet.getName()+").");
        }
        this.dataSets.put(dataSet.getName(), dataSet);
        return dataSet;
    }

    @Override
    public TopologyDataSet getDataSet(String name) {
        return this.dataSets.get(name);
    }

    @Override
    public boolean containsDataSet(String name) {
        return this.dataSets.containsKey(name);
    }

    @Override
    public Collection<TopologyDataSet> getDataSets() {
        Collection<TopologyDataSet> x = dataSets.values();
        return Collections.unmodifiableCollection(x);
    }

    @Override
    public Collection<TopologyDataSet> getSecondaryDataSets(TopologyRuleFactory ruleFactory) {
        List<TopologyDataSet> secondaryDataSets = new ArrayList<>();
        for (TopologyDataSet dataSet : dataSets.values()) {
            if( ruleFactory.canApplyToSecondaryDataSet(dataSet) ) {
                secondaryDataSets.add(dataSet);
            }
        }
        return secondaryDataSets;
    }
    
    @Override
    public TopologyRule addRule(String id, String dataSet1, String dataSet2, double tolerance) {
        TopologyRuleFactory factory = this.manager.getRulefactory(id);
        if( ! this.canApplyRule(factory, dataSet1, dataSet2) ) {
            throw new IllegalArgumentException(
                    "Can't apply rule '"+factory.getName()+"' to the datasets '"+dataSet1+"/"+dataSet2+"'."
            );
        }
        TopologyRule rule = factory.createRule(this, dataSet1, dataSet2, tolerance);
        return this.addRule(rule);
    }

    @Override
    public TopologyRule addRule(TopologyRule rule) {
        this.rules.add(rule);
        return rule;
    }

    private boolean canApplyRule(TopologyRuleFactory factory, String dataSet1, String dataSet2) {
        try {
            GeometryManager geomManager = GeometryLocator.getGeometryManager();
            TopologyDataSet dataset = this.getDataSet(dataSet1);
            FeatureStore store = (FeatureStore) dataset.getStore();
            GeometryType gt = store.getDefaultFeatureType().getDefaultGeometryAttribute().getGeomType();
            if( !gt.isTypeOf(factory.getGeometryTypeDataSet1()) ) {
                return false;
            }
            if( factory.getGeometryTypeDataSet2()!=Geometry.TYPES.NULL ) {
                dataset = this.getDataSet(dataSet2);
                store = (FeatureStore) dataset.getStore();
                gt = store.getDefaultFeatureType().getDefaultGeometryAttribute().getGeomType();
                if( !gt.isTypeOf(factory.getGeometryTypeDataSet2()) ) {
                    return false;
                }
            }
            return true;
        } catch(Exception ex) {
            return false;
        }
    }    

    @Override
    public Collection<TopologyRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    @Override
    public TopologyReport getReport() {
        if( this.report == null ) {
            this.report = new DefaultTopologyReport();
        }
        return this.report;
    }


    @Override
    public String toJSON() {
        JSONObject me = new JSONObject();

        me.put("name", this.name);
        me.put("tolerance", this.tolerance);
        
        JSONArray jsonDataSets=  new JSONArray();
        for (TopologyDataSet dataSet : this.dataSets.values()) {
            jsonDataSets.put(((DefaultTopologyDataSet)dataSet).toJSON());
        }
        me.put("dataSets", jsonDataSets);
        
        JSONArray jsonRules = new JSONArray();
        for (TopologyRule rule : this.rules) {
            JSONObject jsonRule = new JSONObject();
            jsonRule.put("dataSet1", rule.getDataSet1().getName());
            jsonRule.put("dataSet2", (rule.getDataSet2()==null)? null:rule.getDataSet2().getName());
            jsonRule.put("tolerance", rule.getTolerance());
            jsonRules.put(jsonRule);
        }
        me.put("rules", jsonRules);
        
        return me.toString();
    }
    
    @Override
    public void fromJSON(String plan) {
        JSONObject jsonPlan = new JSONObject(plan);
        
        this.name = jsonPlan.getString("name");
        this.tolerance = jsonPlan.getDouble("tolerance");
        
        JSONArray jsonDataSets = jsonPlan.getJSONArray("dataSets");
        for (Object o : jsonDataSets) {
            TopologyDataSet dataSet = new DefaultTopologyDataSet(
                    this.services, (JSONObject) o
            );
            this.dataSets.put(dataSet.getName(),dataSet);
        }
        
        JSONArray jsonRules = jsonPlan.getJSONArray("rules");
        for (Object o : jsonRules) {
            JSONObject jsonRule = (JSONObject) o;
            this.addRule(
                    jsonRule.getString("id"), 
                    jsonRule.getString("dataSet1"), 
                    jsonRule.getString("dataSet2"),
                    jsonRule.getDouble("tolerance")
            );
        }        
    }

    @Override
    public void removeDataSet(TopologyDataSet dataSet) {
        this.dataSets.remove(dataSet.getName());
    }

    @Override
    public void removeRule(TopologyRule rule) {
        this.rules.remove(rule);
    }

    
}
