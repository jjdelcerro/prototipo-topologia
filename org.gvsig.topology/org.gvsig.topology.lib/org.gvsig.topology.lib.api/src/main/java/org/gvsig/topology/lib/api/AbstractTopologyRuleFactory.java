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
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;

/**
 *
 * @author jjdelcerro
 */
public abstract class AbstractTopologyRuleFactory implements TopologyRuleFactory {
    private final String id;
    private final String name;
    private final String description;
    private final Image imageDescription;
    private final int geometryTypeDataSet1;
    private final int geometryTypeDataSet2;

    protected AbstractTopologyRuleFactory(
            String id,
            String name,
            String description,
            URL imageDescription,
            int geometryTypeDataSet1,
            int geometryTypeDataSet2
        ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.geometryTypeDataSet1 = geometryTypeDataSet1;
        this.geometryTypeDataSet2 = geometryTypeDataSet2;
        if( imageDescription == null ) {
            this.imageDescription = null;
        } else {
            try {
                this.imageDescription = ImageIO.read(imageDescription);
            } catch (IOException ex) {
                throw new IllegalArgumentException("Can't load image description.", ex);
            }
        } 
    }

    protected AbstractTopologyRuleFactory(
            String id,
            String name,
            String description,
            URL imageDescription,
            int geometryTypeDataSet1
        ) {
        this(id, name, description, imageDescription, geometryTypeDataSet1, Geometry.TYPES.NULL);
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
    public Image getImageDescription() {
        return this.imageDescription;
    }

    @Override
    public int getGeometryTypeDataSet1() {
        return this.geometryTypeDataSet1;
    }

    @Override
    public int getGeometryTypeDataSet2() {
        return this.geometryTypeDataSet2;
    }

    @Override
    public boolean hasSecondaryDataSet() {
        return this.geometryTypeDataSet2!=Geometry.TYPES.NULL;
    }

    @Override
    public boolean canApplyToDataSet(TopologyDataSet dataSet) {
        GeometryManager geomManager = GeometryLocator.getGeometryManager();
        boolean x = geomManager.isSubtype(dataSet.getGeometryType(), this.geometryTypeDataSet1);
        return x;
    }

    @Override
    public boolean canApplyToSecondaryDataSet(TopologyDataSet dataSet) {
        if( !this.hasSecondaryDataSet() ) {
            return false;
        }
        GeometryManager geomManager = GeometryLocator.getGeometryManager();
        boolean x = geomManager.isSubtype(dataSet.getGeometryType(), this.geometryTypeDataSet2);
        return x;
    }
}
