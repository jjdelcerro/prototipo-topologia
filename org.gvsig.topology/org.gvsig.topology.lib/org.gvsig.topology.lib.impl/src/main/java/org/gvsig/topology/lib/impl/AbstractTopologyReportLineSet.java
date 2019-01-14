package org.gvsig.topology.lib.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.gvsig.tools.swing.api.ChangeListenerHelper;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.topology.lib.api.TopologyReportLine;
import org.gvsig.topology.lib.api.TopologyReportLineSet;

/**
 *
 * @author jjdelcerro
 */
public abstract class AbstractTopologyReportLineSet implements TopologyReportLineSet {

    protected final List<TopologyReportLine> lines;
    protected final ChangeListenerHelper changeListenerHelper;
    protected boolean completed = false;

    public AbstractTopologyReportLineSet() {
        this.lines = new ArrayList<>();
        this.changeListenerHelper = ToolsSwingLocator.getToolsSwingManager().createChangeListenerHelper();
    }

    @Override
    public List<TopologyReportLine> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
        this.changeListenerHelper.addChangeListener(cl);
    }

    @Override
    public ChangeListener[] getChangeListeners() {
        return this.changeListenerHelper.getChangeListeners();
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        this.changeListenerHelper.removeChangeListener(cl);
    }

    @Override
    public void removeAllChangeListener() {
        this.changeListenerHelper.removeAllChangeListener();
    }

    @Override
    public boolean hasChangeListeners() {
        return this.changeListenerHelper.hasChangeListeners();
    }

    protected void fireChangeEvent() {
        this.changeListenerHelper.fireEvent();        
    }
    
    @Override
    public boolean isComplete() {
        return this.completed;
    }

    @Override
    public int size() {
        return this.lines.size();
    }

    @Override
    public TopologyReportLine get(int index) {
        return this.lines.get(index);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }    
}
