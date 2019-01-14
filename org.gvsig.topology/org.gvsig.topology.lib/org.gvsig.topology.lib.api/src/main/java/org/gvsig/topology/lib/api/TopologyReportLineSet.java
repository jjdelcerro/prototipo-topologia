package org.gvsig.topology.lib.api;

import java.util.List;
import org.gvsig.tools.swing.api.ChangeListenerSupport;

/**
 *
 * @author jjdelcerro
 */
public interface TopologyReportLineSet extends ChangeListenerSupport {

    public List<TopologyReportLine> getLines();

    public boolean isComplete();

    public int size();

    public TopologyReportLine get(int index);
    
}
