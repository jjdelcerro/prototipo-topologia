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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.gvsig.fmap.dal.ExpressionBuilder;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Point;
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
public class ContainsPointRule extends AbstractTopologyRule {

    private class CreateFetureAction extends AbstractTopologyRuleAction {

        public CreateFetureAction() {
            super(
                    "CreateFeature",
                    "Create Feature",
                    "The Create Feature fix creates a new point feature at the centroid of the polygon feature that is causing the error. The point feature that is created is guaranteed to be within the polygon feature."
            );
        }

        @Override
        public DynObject createParameters() {
            return null;
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
            Geometry polygon = feature1.getDefaultGeometry();
            FeatureStore store = dataSet2.getStore();
            String geomName = store.getDefaultFeatureType().getDefaultGeometryAttributeName();
            FeatureQuery query = store.createFeatureQuery();
            ExpressionBuilder sqlbuilder = store.createExpressionBuilder();
            sqlbuilder.ST_Intersects(
                    sqlbuilder.column(geomName),
                    sqlbuilder.geometry(polygon, polygon.getProjection())
            );
            query.addFilter(sqlbuilder.toString());
            set = store.getFeatureSet(query);
            if (set.isEmpty()) {
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
        } finally {
            if( set!=null ) {
                set.dispose();
            }
        }
    }

}
