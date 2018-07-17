package org.gvsig.topology.lib.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author jjdelcerro
 */
public class ListBuilder {

    public static List create(Object...elements) {
        ArrayList l = new ArrayList();
        l.addAll(Arrays.asList(elements));
        return l;
    }
}
