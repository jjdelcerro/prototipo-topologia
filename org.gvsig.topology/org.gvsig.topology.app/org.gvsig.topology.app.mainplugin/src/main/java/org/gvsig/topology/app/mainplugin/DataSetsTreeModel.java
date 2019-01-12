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
package org.gvsig.topology.app.mainplugin;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.gvsig.app.project.documents.view.ViewDocument;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.CompoundLayersTreeModel;
import org.gvsig.fmap.mapcontrol.MapControlLocator;
import org.gvsig.topology.lib.api.TopologyLocator;
import org.gvsig.topology.swing.api.TopologySwingServices;

/**
 *
 * @author jjdelcerro
 */
public class DataSetsTreeModel implements TreeModel {

    private final ViewDocument view;
    private final CompoundLayersTreeModel deletaged;
    private final TopologySwingServices services;
    
    public DataSetsTreeModel(TopologySwingServices services, ViewDocument view) {
        this.services = services;
        this.view = view;
        CompoundLayersTreeModel model = (CompoundLayersTreeModel) MapControlLocator.getMapControlManager().createCompoundLayersTreeModel();
        model.addLayers(view.getMapContext().getLayers());
        this.deletaged = model;
    }
    
    @Override
    public Object getRoot() {
        return this.deletaged.getRoot();
    }

    @Override
    public Object getChild(Object parent, int index) {
        Object x = this.deletaged.getChild(parent, index);
        if( x instanceof FLyrVect ) {
            FLyrVect layer = (FLyrVect)x;
            FeatureStore store = layer.getFeatureStore();
            x = TopologyLocator.getTopologyManager().createDataSet(
                    layer.getName(),
                    store
            );
        }
        return x;
    }

    @Override
    public int getChildCount(Object parent) {
        return this.deletaged.getChildCount(parent);
    }

    @Override
    public boolean isLeaf(Object node) {
        return this.deletaged.isLeaf(node);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        this.deletaged.valueForPathChanged(path, newValue);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return this.deletaged.getIndexOfChild(parent, child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        this.deletaged.addTreeModelListener(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        this.deletaged.removeTreeModelListener(l);
    }
    
}
