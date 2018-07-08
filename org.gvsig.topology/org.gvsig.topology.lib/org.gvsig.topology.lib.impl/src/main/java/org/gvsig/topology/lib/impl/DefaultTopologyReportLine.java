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

import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.lib.api.TopologyReportLine;
import org.gvsig.topology.lib.api.TopologyRule;

/**
 *
 * @author jjdelcerro
 */
public class DefaultTopologyReportLine implements TopologyReportLine {
    
    private final TopologyReport report;
    private final TopologyRule rule;
    private final TopologyDataSet dataSet1;
    private final TopologyDataSet dataSet2;
    private final Geometry geometry;
    private final FeatureReference feature1;
    private final FeatureReference feature2;
    private boolean exception;
    private final String description;
    
    DefaultTopologyReportLine(DefaultTopologyReport report, 
            TopologyRule rule, 
            TopologyDataSet dataSet1, 
            TopologyDataSet dataSet2, 
            Geometry geometry,
            FeatureReference feature1, 
            FeatureReference feature2, 
            boolean exception,
            String description
        ) {
        this.report = report;
        this.rule = rule;
        this.dataSet1 = dataSet1;
        this.dataSet2 = dataSet2;
        this.geometry = geometry;
        this.feature1 = feature1;
        this.feature2 = feature2;
        this.exception = exception;
        this.description = description;
    }

    @Override
    public TopologyRule getRule() {
        return this.rule;
    }

    @Override
    public TopologyDataSet getDataSet1() {
        return this.dataSet1;
    }

    @Override
    public TopologyDataSet getDataSet2() {
        return this.dataSet2;
    }

    @Override
    public Geometry getGeometry() {
        return this.geometry;
    }

    @Override
    public FeatureReference getFeature1() {
        return this.feature1;
    }

    @Override
    public FeatureReference getFeature2() {
        return this.feature2;
    }

    @Override
    public boolean isException() {
        return this.exception;
    }
    
    public void setException(boolean exception) {
        this.exception = exception;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
