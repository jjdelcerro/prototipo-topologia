package org.gvsig.topology.lib.api;

import org.json.JSONObject;

/**
 *
 * @author jjdelcerro
 */
public interface SerializableJSON {
    public JSONObject toJSON();
    
    public void fromJSON(JSONObject json);
    
    public void fromJSON(String json);
    
}
