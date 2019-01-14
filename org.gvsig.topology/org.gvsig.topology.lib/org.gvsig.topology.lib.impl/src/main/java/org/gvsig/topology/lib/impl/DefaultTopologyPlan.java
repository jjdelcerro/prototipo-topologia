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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjdelcerro
 */
@SuppressWarnings("UseSpecificCatch")
public class DefaultTopologyPlan implements TopologyPlan {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTopologyPlan.class);
    
    private final TopologyManager manager;
    private final Map<String,TopologyDataSet> dataSets;
    private final List<TopologyRule> rules;

    private String name;
    private DefaultTopologyReport report;
    private TopologyServices services;
    private double tolerance;
    private SimpleTaskStatus taskStatus;
    
    public DefaultTopologyPlan(TopologyManager manager, TopologyServices services) {
        this.manager = manager;
        this.services = services;
        this.dataSets = new HashMap<>();
        this.rules = new ArrayList<>();
        this.report = null;
        this.name = "TopologyPlan-" + String.format("%08X", new Date().getTime());
        this.tolerance = 0.001;
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
    
    public SimpleTaskStatus getTaskStatus() {
        if( this.taskStatus == null ) {
            this.taskStatus = ToolsLocator.getTaskStatusManager()
                    .createDefaultSimpleTaskStatus(this.getName());
        }
        return this.taskStatus;
    }
    
    @Override
    public void execute() {
        SimpleTaskStatus theTaskStatus = this.getTaskStatus();
        try {
            theTaskStatus.restart();
            theTaskStatus.message("Preparing the execution of the plan");
            theTaskStatus.setAutoremove(true);
            theTaskStatus.setIndeterminate();
            this.getReport().removeAllLines();
            long steps = 0;
            for (TopologyRule rule : this.rules) {
                steps += rule.getSteps();
                steps++;
            }

            theTaskStatus.setRangeOfValues(0, steps);
            theTaskStatus.setCurValue(0);
            for (TopologyRule rule : this.rules) {
                if( theTaskStatus.isCancellationRequested() ) {
                    theTaskStatus.cancel();
                    break;
                }
                theTaskStatus.message(rule.getName());
                rule.execute(theTaskStatus, this.getReport());
                theTaskStatus.incrementCurrentValue();
            }
        } catch(Exception ex) {
            LOGGER.warn("Problems executing topology plan '"+this.getName()+"'.", ex);
            theTaskStatus.abort();
        } finally {
            if( theTaskStatus.isRunning() ) {
                theTaskStatus.terminate();
            }
            this.getReport().setCompleted(true);
        }
    }

    @Override
    public TopologyDataSet addDataSet(String name, FeatureStore store) {
        TopologyDataSet dataSet = manager.createDataSet(name, store);
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
        if( factory == null ) {
            throw new IllegalArgumentException("Can't locate factory for rule '"+id+"'.");
        }
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
            for (Integer geometryType1 : factory.getGeometryTypeDataSet1()) {
                if( gt.isTypeOf(geometryType1) ) {
                    if( factory.getGeometryTypeDataSet2()==null ) {
                        return true;
                    }
                    dataset = this.getDataSet(dataSet2);
                    store = (FeatureStore) dataset.getStore();
                    gt = store.getDefaultFeatureType().getDefaultGeometryAttribute().getGeomType();
                    for (Integer geometryType2 : factory.getGeometryTypeDataSet2()) {
                        if( gt.isTypeOf(geometryType2) ) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch(Exception ex) {
            return false;
        }
    }    

    @Override
    public Collection<TopologyRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    @Override
    public DefaultTopologyReport getReport() {
        if( this.report == null ) {
            this.report = new DefaultTopologyReport(this);
        }
        return this.report;
    }

    @Override
    public JSONObject toJSON() {
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
            JSONObject jsonRule = rule.toJSON();
            jsonRule.put("__factoryId", rule.getFactory().getId());
            jsonRules.put(jsonRule);
        }
        me.put("rules", jsonRules);
        
        return me;
    }

    @Override
    public void fromJSON(String json) {
        this.fromJSON(new JSONObject(json));
    }

    @Override
    public void fromJSON(JSONObject jsonPlan) {
        
        this.name = jsonPlan.getString("name");
        this.tolerance = jsonPlan.getDouble("tolerance");
        
        JSONArray jsonDataSets = jsonPlan.getJSONArray("dataSets");
        for (Object o : jsonDataSets) {
            TopologyDataSet dataSet = new DefaultTopologyDataSet();
            dataSet.fromJSON((JSONObject) o);
            this.dataSets.put(dataSet.getName(),dataSet);
        }
        
        JSONArray jsonRules = jsonPlan.getJSONArray("rules");
        for (Object o : jsonRules) {
            JSONObject jsonRule = (JSONObject) o;
            TopologyRuleFactory factory = this.manager.getRulefactory(jsonRule.getString("__factoryId"));
            TopologyRule rule = factory.createRule(this);
            rule.fromJSON(jsonRule);
            this.addRule(rule);
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
