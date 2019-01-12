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
import javax.swing.event.ChangeListener;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.swing.api.ChangeListenerHelper;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyReport;
import org.gvsig.topology.lib.api.TopologyReportLine;
import org.gvsig.topology.lib.api.TopologyRule;

/**
 *
 * @author jjdelcerro
 */
public class DefaultTopologyReport implements TopologyReport {

    // TODO: Habria que meter las lineas del report en disco
    private final List<TopologyReportLine> lines;
    private final ChangeListenerHelper changeListenerHelper;
    
    public DefaultTopologyReport() {
        this.lines = new ArrayList<>();
        this.changeListenerHelper = ToolsSwingLocator.getToolsSwingManager().createChangeListenerHelper();
    }

    @Override
    public TopologyReportLine addLine(TopologyRule rule, TopologyDataSet dataSet1,  
            TopologyDataSet dataSet2, Geometry geometry, 
            FeatureReference feature1, FeatureReference feature2, 
            boolean exception, String description) {
        TopologyReportLine line = new DefaultTopologyReportLine(
                this, rule, dataSet1, dataSet2, geometry, feature1, feature2, 
                exception, description
        );
        this.lines.add(line);
        this.changeListenerHelper.fireEvent();
        return line;
    }

    @Override
    public void removeAllLines() {
        this.lines.clear();
        this.changeListenerHelper.fireEvent();
    }

    @Override
    public List<TopologyReportLine> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

//    @Override
//    public List<TopologyReportLine> getLines(TopologyReportFilter filter) {
//        List<TopologyReportLine> ll = new ArrayList<>();
//        for (TopologyReportLine line : this.lines) {
//            if( filter.accept(line) ) {
//                ll.add(line);
//            }
//        }
//        return ll;
//    }

    @Override
    public void addChangeListener(ChangeListener cl) {
        this.changeListenerHelper.addChangeListener(cl);
    }

    @Override
    public ChangeListener[] getChangeListeners() {
        return this.changeListenerHelper.getChangeListeners();
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        this.changeListenerHelper.removeChangeListener(cl);
    }

    @Override
    public void removeAllChangeListener() {
        this.changeListenerHelper.removeAllChangeListener();
    }

    @Override
    public boolean hasChangeListeners() {
        return this.changeListenerHelper.hasChangeListeners();
    }
    
}
