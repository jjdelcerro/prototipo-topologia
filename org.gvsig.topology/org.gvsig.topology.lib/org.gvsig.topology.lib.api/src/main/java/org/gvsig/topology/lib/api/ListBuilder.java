package org.gvsig.topology.lib.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author jjdelcerro
 */
public class ListBuilder<T> {

    public static List create(Object...elements) {
        ArrayList l = new ArrayList();
        l.addAll(Arrays.asList(elements));
        return l;
    }

    List<T> list;
    
    public ListBuilder() {
        this.list = new ArrayList();
    }
    
    public ListBuilder<T> add(T element) {
        this.list.add(element);
        return this;
    }
    
    public List<T> asList() {
        return this.list;
    }
}

