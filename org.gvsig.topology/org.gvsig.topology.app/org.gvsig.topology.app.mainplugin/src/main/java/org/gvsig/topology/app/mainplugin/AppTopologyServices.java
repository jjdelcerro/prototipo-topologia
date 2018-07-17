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

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.TreeModel;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.gvsig.app.ApplicationLocator;
import org.gvsig.app.ApplicationManager;
import org.gvsig.app.project.Project;
import org.gvsig.app.project.documents.Document;
import org.gvsig.app.project.documents.view.ViewDocument;
import org.gvsig.app.project.documents.view.ViewManager;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.visitor.VisitCanceledException;
import org.gvsig.tools.visitor.Visitor;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.swing.api.TopologySwingServices;

/**
 *
 * @author jjdelcerro
 */
public class AppTopologyServices implements TopologySwingServices {

    @Override
    public TreeModel getDataSetTreeModel() {
        ApplicationManager application = ApplicationLocator.getManager();
        ViewDocument view = (ViewDocument) application.getActiveDocument(ViewManager.TYPENAME);
        return new DataSetsTreeModel(this, view);
    }

    @Override
    public FeatureStore getFeatureStore(final TopologyDataSet dataSet) {
        final Mutable<FeatureStore> store = new MutableObject<>();
        store.setValue(null);
        
        ApplicationManager application = ApplicationLocator.getManager();
        Project project = application.getCurrentProject();
        
        List<Document> views = new ArrayList<>();
        views.add(project.getActiveDocument(ViewManager.TYPENAME));
//        views.addAll(project.getDocuments(ViewManager.TYPENAME));

        for (Document view : views) {
            if( view == null ) {
                continue;
            }
            FLayers layers = ((ViewDocument)view).getMapContext().getLayers();
            try {
                layers.accept(new Visitor() {
                    @Override
                    public void visit(Object o) throws VisitCanceledException, BaseException {
                        if( o instanceof FLyrVect ) {
                            FLyrVect layer = (FLyrVect) o;
                            if( dataSet.isThisStore(layer.getFeatureStore()) ) {
                                store.setValue(layer.getFeatureStore());
                                throw new VisitCanceledException();
                            }
                        }
                    }
                });
            } catch (VisitCanceledException ex) {
                break;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return store.getValue();
    }
    
}
