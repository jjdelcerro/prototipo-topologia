package org.gvsig.topology.lib.api;

import org.gvsig.fmap.dal.exception.DataException;

/**
 *
 * @author jjdelcerro
 */
public class CancelOperationException extends DataException {

    public CancelOperationException(String msg) {
        super("_CancelOperationException", msg, 0);
    }
}
