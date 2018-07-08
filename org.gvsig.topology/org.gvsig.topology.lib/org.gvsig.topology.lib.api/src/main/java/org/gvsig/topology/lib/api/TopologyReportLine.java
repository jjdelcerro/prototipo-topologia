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

import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.geom.Geometry;

/**
 *
 * @author jjdelcerro
 */
public interface TopologyReportLine {
    
    public TopologyRule getRule();
    
    public TopologyDataSet getDataSet1();
    
    public TopologyDataSet getDataSet2();
    
    public Geometry getGeometry();
    
    public FeatureReference getFeature1();

    public FeatureReference getFeature2();
    
    public boolean isException();
    
    public String getDescription();

}
