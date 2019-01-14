/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gvsig.topology.lib.spi;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjdelcerro
 */
@SuppressWarnings("UseSpecificCatch")
public class RuleResourceLoaderUtils {
    
    private static Logger LOGGER = LoggerFactory.getLogger(RuleResourceLoaderUtils.class);
    
    public static URL getRuleURL(String idRule) {
        String lang = Locale.getDefault().getLanguage();
        URL url = RuleResourceLoaderUtils.class.getResource("/org/gvsig/topology/rules/"+lang+"/"+idRule+".json");
        if( url == null ) {
            url = RuleResourceLoaderUtils.class.getResource("/org/gvsig/topology/rules/en/"+idRule+".json");
            if( url == null ) {
                return null;
            }
        }
        return url;
    }
    
    public static JSONObject getRule(String idRule) {
        URL url = getRuleURL(idRule);
        if( url == null ) {
            return null;
        }
        InputStream is = null;
        JSONObject json;
        try {
            is = url.openStream();
            List<String> lines = IOUtils.readLines(is);
            json = new JSONObject(StringUtils.join(lines,  "\n"));
        } catch (Exception ex) {
            LOGGER.warn("Can't load resource from json file '"+url.toString()+"'.", ex);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return json;
    }

    public static String getDescription(String idRule, JSONObject json) {
        String description = null;
        
        if( json.has("description") ) {
            Object x = json.get("description");
            if( x instanceof String ) {
                description = (String) x;
            } else if( x instanceof JSONArray ) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < ((JSONArray)x).length(); i++) {
                    if( i>0 ) {
                        builder.append(" ");
                    }
                    builder.append(((JSONArray)x).getString(i));
                }
                description = builder.toString();
            } else {
                description = x.toString();
            }
            if( description.contains("@@@") ) {
                description = StringUtils.replace(
                        description, 
                        "@@@", 
                        getRuleURL(idRule).toString()
                );
            }
        }
        return description;
    }
    
    public static JSONObject getAction(JSONObject rule, String idAction) {
        if( !rule.has("actions") ) {
            return null;
        }
        if( !rule.getJSONObject("actions").has(idAction) ) {
            return null;
        }
        JSONObject action = rule.getJSONObject("actions").getJSONObject(idAction);
        return action;
    }

}
