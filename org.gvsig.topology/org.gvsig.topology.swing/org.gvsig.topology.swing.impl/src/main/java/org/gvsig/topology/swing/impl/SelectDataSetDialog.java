/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gvsig.topology.swing.impl;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.commons.lang3.StringUtils;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.tools.swing.api.ToolsSwingManager;
import org.gvsig.tools.swing.api.windowmanager.Dialog;
import org.gvsig.tools.swing.api.windowmanager.WindowManager_v2;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.swing.api.TopologySwingServices;

/**
 *
 * @author jjdelcerro
 */
public class SelectDataSetDialog extends SelectDataSetDialogView {

    private final TopologySwingServices services;
    private Dialog dialog = null;
    
    SelectDataSetDialog(TopologySwingServices services) {
        this.services = services;
        this.initComponents();
    }
    
    private void initComponents() {
        this.treeDataSets.setModel(this.services.getDataSetTreeModel());
        this.treeDataSets.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                performDataSetSelected();
            }
        });
        
        this.translate();
    }

    private void translate() {
        ToolsSwingManager tsm = ToolsSwingLocator.getToolsSwingManager();
        tsm.translate(this.lblName);    
    }
    
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
    
    private void setOkButtonEnabled(boolean enabled) {
        if( this.dialog==null ) {
            return;
        }
        this.dialog.setButtonEnabled(WindowManager_v2.BUTTONS_OK, enabled);
    }
    
    private TopologyDataSet getSelectedDataSet() {
        TreePath path = treeDataSets.getSelectionPath();
        Object element = path.getLastPathComponent();
        if( element instanceof TopologyDataSet ) {
            TopologyDataSet dataSet = (TopologyDataSet) element;
            return dataSet;
        }
        return null;
    }
    
    public TopologyDataSet getDataSet() {
        TopologyDataSet dataSet = this.getSelectedDataSet();
        if( dataSet == null ) {
            return null;
        }
        String name = this.txtName.getName();
        if( !StringUtils.isEmpty(name) ) {
            dataSet.setName(name);
        }
        return dataSet;
     }    

    public void performDataSetSelected() {
        TopologyDataSet dataSet = this.getSelectedDataSet();
        if( dataSet==null ) {
            this.lblName.setText("");
            this.setOkButtonEnabled(false);
        } else {
            this.lblName.setText(dataSet.getName());
            this.setOkButtonEnabled(true);
        }
    }
    
}
