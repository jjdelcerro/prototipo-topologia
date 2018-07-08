package org.gvsig.topology.lib.api;

import org.gvsig.fmap.dal.exception.DataException;

/**
 *
 * @author jjdelcerro
 */
public class PerformOperationException extends DataException {

    public PerformOperationException(String msg) {
        super("_CancelOperationException", msg, 0);
    }

}
