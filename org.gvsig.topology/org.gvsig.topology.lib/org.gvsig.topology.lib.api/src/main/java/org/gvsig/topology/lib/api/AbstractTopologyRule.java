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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.task.SimpleTaskStatus;
import org.gvsig.tools.visitor.VisitCanceledException;
import org.gvsig.tools.visitor.Visitor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjdelcerro
 */
public abstract class AbstractTopologyRule implements TopologyRule {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTopologyRule.class);
    
    private final TopologyPlan plan;
    private final TopologyRuleFactory factory;
    private double tolerance;
    private String dataSet1;
    private String dataSet2;

    protected TopologyReport report;
    protected List<TopologyRuleAction> actions;

    protected AbstractTopologyRule(
            TopologyPlan plan,
            TopologyRuleFactory factory
    ) {
        this.plan = plan;
        this.factory = factory;

        this.tolerance = plan.getTolerance();
        this.dataSet1 = null;
        this.dataSet2 = null;
        this.report = null;
        this.actions = new ArrayList<>();
    }
    
    protected AbstractTopologyRule(
            TopologyPlan plan,
            TopologyRuleFactory factory,
            double tolerance,
            String dataSet1,
            String dataSet2
    ) {
        this(plan,factory);
        this.tolerance = tolerance;
        this.dataSet1 = dataSet1;
        this.dataSet2 = dataSet2;
    }

    protected TopologyPlan getPlan() {
        return this.plan;
    }

    @Override
    public TopologyRuleFactory getFactory() {
        return this.factory;
    }

    @Override
    public String getName() {
        return this.getFactory().getName();
    }

    @Override
    public String getId() {
        return this.getFactory().getId();
    }

    
    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof TopologyRule) ) {
            return false;
        }
        AbstractTopologyRule other = (AbstractTopologyRule)obj;
        if( !StringUtils.equals(this.getName(), other.getName()) ) {
            return false;
        }
        if( !StringUtils.equals(this.dataSet1, other.dataSet1) ) {
            return false;
        }
        if( !StringUtils.equals(this.dataSet2, other.dataSet2) ) {
            return false;
        }
        // Ojo con la comparacion de doubles
//        if( this.tolerance != other.tolerance ) {
//            return false;
//        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public TopologyDataSet getDataSet1() {
        return this.getPlan().getDataSet(dataSet1);
    }

    @Override
    public TopologyDataSet getDataSet2() {
        return this.getPlan().getDataSet(dataSet2);
    }

    @Override
    public double getTolerance() {
        return this.tolerance;
    }

    @Override
    public List<TopologyRuleAction> getActions() {
        return Collections.unmodifiableList(actions);
    }

    @Override
    public TopologyRuleAction getAction(String id) {
        for (TopologyRuleAction action : actions) {
            if (StringUtils.equalsIgnoreCase(id, action.getId())) {
                return action;
            }
        }
        return null;
    }

    @Override
    public long getSteps() {
        return this.getDataSet1().getSize();
    }

    @Override
    public void execute(final SimpleTaskStatus taskStatus, final TopologyReport report) {
        this.getDataSet1().accept(new Visitor() {
            @Override
            public void visit(final Object o1) throws VisitCanceledException, BaseException {
                taskStatus.incrementCurrentValue();
                try {
                    if (getDataSet2() == null) {
                        check(report, (Feature) o1);
                    } else {
                        check(report, (Feature) o1, getDataSet2());
                    }
                } catch (Exception ex) {
                    // ¿¿¿???
                }
            }
        });
    }

    protected void check(TopologyReport report, Feature feature) throws Exception {
        
    }

    protected  void check(TopologyReport report, Feature feature1, TopologyDataSet dataSet2 ) throws Exception {
        
    }

    @Override
    public JSONObject toJSON() {
        JSONObject me = new JSONObject();
        me.put("tolerance", this.tolerance);
        me.put("dataSet1", this.dataSet1);
        me.put("dataSet2", this.dataSet2);
        
        return me;
    }

    @Override
    public void fromJSON(String json) {
        this.fromJSON(new JSONObject(json));
    }
    
    @Override
    public void fromJSON(JSONObject json) {
        this.report = null;
        this.actions = new ArrayList<>();

        if( json.has("tolerance") ) {
            this.tolerance = json.getDouble("tolerance");
        }
        if( json.has("dataSet1") ) {
            this.dataSet1 = json.getString("dataSet1");
        }
        if( json.has("dataSet2") ) {
            this.dataSet2 = json.getString("dataSet2");
        }
    }

}
