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

import java.awt.Image;
import java.util.List;

/**
 *
 * @author jjdelcerro
 */
public interface TopologyRuleFactory {
    
    public String getId();
    
    public String getName();
    
    public String getDescription();
    
    public List<Integer> getGeometryTypeDataSet1();
    
    public List<Integer> getGeometryTypeDataSet2();
    
    public TopologyRule createRule(TopologyPlan plan, String dataSet1, String dataSet2, double tolerance);

    public TopologyRule createRule(TopologyPlan plan);

    public boolean hasSecondaryDataSet();

    public boolean canApplyToDataSet(TopologyDataSet dataSet);
    
    public boolean canApplyToSecondaryDataSet(TopologyDataSet dataSet);

    
}
