/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gvsig.topology.lib.api;

/**
 *
 * @author jjdelcerro
 */
public class ExecuteTopologyRuleException extends RuntimeException {
    public ExecuteTopologyRuleException(Exception ex) {
        this.initCause(ex);
    }
        
}
