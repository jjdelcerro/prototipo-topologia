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

import org.gvsig.expressionevaluator.Expression;
import org.gvsig.expressionevaluator.ExpressionBuilder;
import org.gvsig.expressionevaluator.ExpressionEvaluatorLocator;
import org.gvsig.expressionevaluator.ExpressionEvaluatorManager;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.topology.lib.spi.AbstractTopologyRule;
import org.gvsig.topology.lib.spi.AbstractTopologyRuleAction;
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
public class ContainsPointRule extends AbstractTopologyRule {

    private class CreateFetureAction extends AbstractTopologyRuleAction {

        public CreateFetureAction() {
            super(
                    "ContainsPoint",
                    "CreateFeature",
                    "Create Feature",
                    "The Create Feature fix creates a new point feature at the centroid of the polygon feature that is causing the error. The point feature that is created is guaranteed to be within the polygon feature."
            );
        }

        @Override
        public void execute(TopologyRule rule, TopologyReportLine line, DynObject parameters)  {
            try {
                Point point = line.getGeometry().centroid();
                TopologyDataSet dataSet = rule.getDataSet2();

                EditableFeature feature = dataSet.createNewFeature();
                feature.setDefaultGeometry(point);
                dataSet.insert(feature);
                
            } catch (Exception ex) {
                throw new ExecuteTopologyRuleActionException(ex);
            }
        }

    }

    private String geomName;
    private Expression expression = null;
    private ExpressionBuilder expressionBuilder = null;
    
    public ContainsPointRule(
            TopologyPlan plan,
            TopologyRuleFactory factory
    ) {
        super(plan, factory);
        this.actions.add(new CreateFetureAction());
    }
    
    public ContainsPointRule(
            TopologyPlan plan,
            TopologyRuleFactory factory,
            double tolerance,
            String dataSet1,
            String dataSet2
    ) {
        super(plan, factory, tolerance, dataSet1, dataSet2);
        this.actions.add(new CreateFetureAction());
    }

    @Override
    protected void check(TopologyReport report, Feature feature1, TopologyDataSet dataSet2) throws Exception {
        FeatureSet set = null;
        try {
            FeatureStore store2 = dataSet2.getStore();
            if( this.expression == null ) {
                ExpressionEvaluatorManager manager = ExpressionEvaluatorLocator.getManager();
                this.expression = manager.createExpression();
                this.expressionBuilder = manager.createExpressionBuilder();
                this.geomName = store2.getDefaultFeatureType().getDefaultGeometryAttributeName();
             }
            Geometry polygon = feature1.getDefaultGeometry();
            this.expression.setPhrase(
                this.expressionBuilder.ST_Intersects(
                    this.expressionBuilder.column(this.geomName),
                    this.expressionBuilder.geometry(polygon)
                ).toString()
            );
            Feature f = store2.findFirst(this.expression);
            if ( f==null ) {
                report.addLine(this,
                        this.getDataSet1(),
                        this.getDataSet2(),
                        polygon,
                        feature1.getReference(),
                        null,
                        false,
                        "The polygon is an error because it does not contain a point."
                );
            }
        } catch(Exception ex) {
            LOGGER.warn("Can't check feature.", ex);
        } finally {
            if( set!=null ) {
                set.dispose();
            }
        }
    }

}
