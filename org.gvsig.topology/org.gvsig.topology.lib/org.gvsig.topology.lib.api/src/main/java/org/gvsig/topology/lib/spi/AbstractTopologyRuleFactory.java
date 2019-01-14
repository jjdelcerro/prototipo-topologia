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
package org.gvsig.topology.lib.spi;

import java.util.List;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.tools.util.ListBuilder;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyRuleFactory;
import org.json.JSONObject;

/**
 *
 * @author jjdelcerro
 */
public abstract class AbstractTopologyRuleFactory implements TopologyRuleFactory {
    private final String id;
    private String name;
    private String description;
    private final List<Integer> geometryTypeDataSet1;
    private final List<Integer> geometryTypeDataSet2;

    protected AbstractTopologyRuleFactory(
            String id,
            String name,
            String description,
            int geometryTypeDataSet1,
            int geometryTypeDataSet2
        ) {
        this(
                id, 
                name, 
                description, 
                ListBuilder.create(geometryTypeDataSet1), 
                ListBuilder.create(geometryTypeDataSet2)
        );
    }

    protected AbstractTopologyRuleFactory(
            String id,
            String name,
            String description,
            List<Integer> geometryTypeDataSet1,
            int geometryTypeDataSet2
        ) {
        this(
                id, 
                name, 
                description, 
                geometryTypeDataSet1, 
                ListBuilder.create(geometryTypeDataSet2)
        );
    }

    protected AbstractTopologyRuleFactory(
            String id,
            String name,
            String description,
            int geometryTypeDataSet1,
            List<Integer> geometryTypeDataSet2
        ) {
        this(
                id, 
                name, 
                description, 
                ListBuilder.create(geometryTypeDataSet1), 
                geometryTypeDataSet2
        );
    }

    protected AbstractTopologyRuleFactory(
            String id,
            String name,
            String description,
            List<Integer> geometryTypeDataSet1,
            List<Integer> geometryTypeDataSet2
        ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.geometryTypeDataSet1 = geometryTypeDataSet1;
        this.geometryTypeDataSet2 = geometryTypeDataSet2;
        this.load_from_resource();
    }

    protected AbstractTopologyRuleFactory(
            String id,
            String name,
            String description,
            int geometryTypeDataSet1
        ) {
        this(id, name, description, geometryTypeDataSet1, Geometry.TYPES.NULL);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public List<Integer> getGeometryTypeDataSet1() {
        return this.geometryTypeDataSet1;
    }

    @Override
    public List<Integer> getGeometryTypeDataSet2() {
        return this.geometryTypeDataSet2;
    }

    @Override
    public boolean hasSecondaryDataSet() {
        return this.geometryTypeDataSet2 != null && !this.geometryTypeDataSet2.isEmpty();
    }

    @Override
    public boolean canApplyToDataSet(TopologyDataSet dataSet) {
        GeometryManager geomManager = GeometryLocator.getGeometryManager();
        for (Integer geometryType : geometryTypeDataSet1) {
            boolean canApply = geomManager.isSubtype(geometryType, dataSet.getGeometryType());
            if( canApply ) {
                return true;
            }
        }        
        return false;
    }

    @Override
    public boolean canApplyToSecondaryDataSet(TopologyDataSet dataSet) {
        if( !this.hasSecondaryDataSet() ) {
            return false;
        }
        GeometryManager geomManager = GeometryLocator.getGeometryManager();
        for (Integer geometryType : geometryTypeDataSet2) {
            boolean canApply = geomManager.isSubtype(geometryType, dataSet.getGeometryType() );
            if( canApply ) {
                return true;
            }
        }        
        return false;
    }
   
    private void load_from_resource() {
        JSONObject json = RuleResourceLoaderUtils.getRule(this.id);
        if( json == null ) {
            return;
        }
        if( json.has("name") ) {
            this.name = json.getString("name");
        }
        if( json.has("description") ) {
            this.description = RuleResourceLoaderUtils.getDescription(this.id, json);
        }
    }
}
