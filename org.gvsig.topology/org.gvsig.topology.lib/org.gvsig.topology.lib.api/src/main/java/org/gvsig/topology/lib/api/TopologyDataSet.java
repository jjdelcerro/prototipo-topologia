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

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.tools.visitor.Visitor;

/**
 *
 * @author jjdelcerro
 */
public interface TopologyDataSet {

    public static interface Operation {
        public void run() throws DataException;
    }
    
    public void setName(String name);

    public String getName();
    
    public FeatureStore getStore();

    public boolean isThisStore(FeatureStore featureStore);

    public int getGeometryType() ;

    public void accept(Visitor visitor);

    public void edit() throws DataException;

    public void finishEditing() throws DataException;
    
    public EditableFeature createNewFeature() throws DataException;

    public void delete(FeatureReference feature) throws DataException;

    public void delete(Feature feature) throws DataException;

    public void insert(EditableFeature feature) throws DataException;

    public void update(EditableFeature feature) throws DataException;

    public long getSize();
}
