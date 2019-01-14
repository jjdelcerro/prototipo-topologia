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

import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.topology.lib.api.TopologyLocator;
import org.gvsig.topology.lib.api.TopologyRuleAction;
import org.gvsig.topology.lib.api.TopologyRuleFactory;
import org.json.JSONObject;

/**
 *
 * @author jjdelcerro
 */
public abstract class AbstractTopologyRuleAction implements TopologyRuleAction {

    private final String idAction;
    private final String idRule;
    private String name;
    private String shortDescription;

    protected AbstractTopologyRuleAction(
            String idRule,
            String idAction,
            String name,
            String shortDescription
        ) {
        this.idRule = idRule;
        this.idAction = idAction;
        this.name = name;
        this.shortDescription = shortDescription;
        this.load_from_resource();
    }
    
    @Override
    public String getId() {
        return this.idAction;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getShortDescription() {
        return this.shortDescription;
    }

    @Override
    public TopologyRuleFactory getRuleFactory() {
        TopologyRuleFactory f = TopologyLocator.getTopologyManager().getRulefactory(this.idRule);
        return f;
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public DynObject createParameters() {
        return null;
    }
    
    private void load_from_resource() {
        JSONObject rule = RuleResourceLoaderUtils.getRule(this.idRule);
        if( rule == null ) {
            return;
        }
        JSONObject action = RuleResourceLoaderUtils.getAction(rule, this.idAction);
        if( action.has("name") ) {
            this.name = action.getString("name");
        }
        if( action.has("description") ) {
            this.shortDescription = RuleResourceLoaderUtils.getDescription(this.idRule, action);
        }
    }

}
