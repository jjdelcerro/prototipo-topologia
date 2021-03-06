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
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.geom.Geometry;

/**
 *
 * @author jjdelcerro
 */
public interface TopologyReport extends TopologyReportLineSet {

    public static String RULE_ID = "RULE_ID";
    public static String IS_ERROR = "IS_ERROR";
    public static String IS_EXCEPTION = "IS_EXCEPTION";
    public static String GEOMETRY = "GEOMETRY";

    
    public TopologyReportLine addLine(
            TopologyRule rule,
            TopologyDataSet dataSet1,
            TopologyDataSet dataSet2,
            Geometry geometry,
            FeatureReference feature1,
            FeatureReference feature2,
            boolean exception,
            String description
        );
    
    public void removeAllLines();
    
    public List<TopologyReportLine> getLines();

    public List<TopologyReportLine> getLines(String filter);
    
    public TopologyReportLineSet getLineSet(String filter);

}
