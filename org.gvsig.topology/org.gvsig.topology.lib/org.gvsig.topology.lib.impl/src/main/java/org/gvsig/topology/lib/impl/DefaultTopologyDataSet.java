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

import org.apache.commons.lang3.StringUtils;
import org.gvsig.fmap.dal.EditingNotification;
import org.gvsig.fmap.dal.EditingNotificationManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.swing.DALSwingLocator;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.visitor.Visitor;
import org.gvsig.topology.lib.api.CancelOperationException;
import org.gvsig.topology.lib.api.PerformOperationException;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyLocator;
import org.gvsig.topology.lib.api.TopologyManager;
import org.json.JSONObject;
import org.gvsig.topology.lib.api.TopologyServices;

/**
 *
 * @author jjdelcerro
 */
@SuppressWarnings({"EqualsAndHashcode","UseSpecificCatch"})
public class DefaultTopologyDataSet implements TopologyDataSet {

    private TopologyServices services;
    private String name;
    private FeatureStore store;
    private boolean needFinishEditing;
    private String fullName;

    public DefaultTopologyDataSet() {
        this.services = null;
        this.name = null;
        this.store = null;
        this.needFinishEditing = false;
        this.fullName = null;
    }

    public DefaultTopologyDataSet(TopologyServices services, String name, FeatureStore store) {
        this.services = services;
        this.name = name;
        this.store = store;
        this.needFinishEditing = false;
        if( store!=null ) {
            this.fullName = store.getFullName();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof DefaultTopologyDataSet) ) {
            return false;
        }
        DefaultTopologyDataSet other = (DefaultTopologyDataSet)obj;
        if( this.store != other.store ) {
            return false;
        }
        if( !StringUtils.equals(this.getName(), other.getName()) ) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        try {
            FeatureAttributeDescriptor attr = this.getStore().getDefaultFeatureType().getDefaultGeometryAttribute();
            String geomType = attr.getGeomType().getName();
            return this.name + " ("+ geomType + ")";
        } catch(Exception ex) {
            return this.name ;
        }
    }

    @Override
    public FeatureStore getStore() {
        if (this.store == null) {
            this.store = this.services.getFeatureStore(this);
        }
        return this.store;
    }

    @Override
    public long getSize() {
        try {
            long size = this.getStore().getFeatureCount();
            return size;
        } catch (DataException ex) {
            // TODO: mensage al log
            return 0;
        }
    }

    @Override
    public boolean isThisStore(FeatureStore store) {
        if( store == null ) {
            return false;
        }
        return StringUtils.equals(this.fullName, store.getFullName());
    }
    
    @Override
    public int getGeometryType() {
        try {
            FeatureStore theStore = this.getStore();
            FeatureType featureType = theStore.getDefaultFeatureType();
            FeatureAttributeDescriptor attr = featureType.getDefaultGeometryAttribute();
            GeometryType geomType = attr.getGeomType();
            return geomType.getType();
        } catch (Exception ex) {
            return Geometry.TYPES.GEOMETRY;
        }
    }

    @Override
    public void accept(Visitor visitor) {
        FeatureStore st = this.getStore();
        try {
            st.accept(visitor);
        } catch (BaseException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void edit() throws DataException {
        FeatureStore theStore = this.getStore();
        if (!theStore.isEditing()) {
            theStore.edit();
            this.needFinishEditing = true;
        }
    }

    @Override
    public void finishEditing() throws DataException {
        if (this.needFinishEditing) {
            this.getStore().finishEditing();
        }
    }

    @Override
    public EditableFeature createNewFeature() throws DataException {
        EditableFeature f = this.getStore().createNewFeature();
        return f;
    }

    public void perform(
            String operation,
            Feature feature
    ) throws DataException {
        this.edit();

        EditingNotificationManager editingNotificationManager
                = DALSwingLocator.getEditingNotificationManager();
        FeatureStore theStore = this.getStore();
        
        EditingNotification notification
                = editingNotificationManager.notifyObservers(this, // source
                        operation, // type
                        null,// document
                        null,// layer
                        theStore,// store
                        feature// feature
                );

        if (notification.isCanceled()) {
            String msg = String.format(
                    "Can't insert feature into %1$s, canceled by some observer.",
                    this.getName());
            throw new CancelOperationException(msg);
        }
        
        String after = null;
        if( operation.equalsIgnoreCase(EditingNotification.BEFORE_REMOVE_FEATURE) ) {
            theStore.delete(feature);
            after = EditingNotification.AFTER_REMOVE_FEATURE;
            
        } else {
            if (notification.shouldValidateTheFeature()) {
                if (!editingNotificationManager.validateFeature(feature)) {
                    String msg = String.format("%1$s is not valid", feature.toString());
                    throw new PerformOperationException(msg);
                }
            }
            switch(operation) {
                case EditingNotification.BEFORE_UPDATE_FEATURE:
                    theStore.update((EditableFeature) feature);
                    after = EditingNotification.AFTER_UPDATE_FEATURE;
                    break;

                case EditingNotification.BEFORE_INSERT_FEATURE:
                    theStore.insert((EditableFeature) feature);
                    after = EditingNotification.AFTER_INSERT_FEATURE;
                    break;
            }
        }

        editingNotificationManager.notifyObservers(this,
                after, null, null,
                theStore, feature);

    }

    @Override
    public void insert(final EditableFeature feature) throws DataException {
        perform(
            EditingNotification.BEFORE_INSERT_FEATURE,
            feature
        );
    }

    @Override
    public void update(final EditableFeature feature) throws DataException {
        perform(
            EditingNotification.BEFORE_UPDATE_FEATURE,
            feature
        );
    }

    @Override
    public void delete(final Feature feature) throws DataException {
        perform(
            EditingNotification.BEFORE_REMOVE_FEATURE,
            feature
        );
    }

    @Override
    public void delete(final FeatureReference feature) throws DataException {
        perform(
            EditingNotification.BEFORE_REMOVE_FEATURE,
            feature.getFeature()
        );
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonDataSet = new JSONObject();
        jsonDataSet.put("name", this.name);
        jsonDataSet.put("fullName", this.fullName);

        return jsonDataSet;
    }

    @Override
    public void fromJSON(String json) {
        this.fromJSON(new JSONObject(json));
    }

    @Override
    public void fromJSON(JSONObject json) {
        TopologyManager manager = TopologyLocator.getTopologyManager();
        this.name = json.getString("name");
        this.fullName = null;
        if( json.has("fullName") ) {
            this.fullName = json.getString("fullName");
        }
        this.store = null;
        this.needFinishEditing = false;
        this.services = manager.getDefaultServices();
    }

}
