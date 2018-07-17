/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gvsig.topology.swing.impl;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.tools.swing.api.ToolsSwingManager;
import org.gvsig.tools.swing.api.windowmanager.Dialog;
import org.gvsig.tools.swing.api.windowmanager.WindowManager_v2;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyLocator;
import org.gvsig.topology.lib.api.TopologyManager;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyRule;
import org.gvsig.topology.lib.api.TopologyRuleFactory;

/**
 *
 * @author jjdelcerro
 */
public class CreateRuleDialog extends CreateRuleDialogView {

    private final TopologyPlan plan;
    private Dialog dialog;
    
    CreateRuleDialog(TopologyPlan plan ){
        this.plan = plan;
        this.dialog = null;
        this.initComponents();
    }

    private void initComponents() {
        DefaultComboBoxModel<TopologyDataSet> model1 = new DefaultComboBoxModel<>();
        for (TopologyDataSet dataSet : this.plan.getDataSets()) {
            model1.addElement(dataSet);
        }
        this.cboDataSet1.setModel(model1);
        this.cboDataSet1.setSelectedIndex(-1);
        this.cboDataSet1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                performDataSet1Selected();
            }
        });
        this.cboRule.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                performRuleSelected();
            }
        });
        this.txtTolerance.setText(String.valueOf(this.plan.getTolerance()));
        this.txtTolerance.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateData();
            }
        });
        
        this.setOkButtonEnabled(false);
        this.cboRule.setSelectedIndex(-1);
        this.cboRule.setEnabled(false);
        this.cboDataSet2.setEnabled(false);
        this.cboDataSet2.setSelectedItem(-1);
        
        this.setPreferredSize(new Dimension(650, 200));        
        
        this.translate();
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

    private void translate() {
        ToolsSwingManager tsm = ToolsSwingLocator.getToolsSwingManager();
        tsm.translate(this.lblDataSet1);    
        tsm.translate(this.lblDataSet2);    
        tsm.translate(this.lblLabelDescription);    
        tsm.translate(this.lblRule);    
        tsm.translate(this.lblTolerance);    
    }

    private void validateData() {
        try {
            double x = Double.parseDouble(this.txtTolerance.getText());
        } catch(Exception ex) {
            this.setOkButtonEnabled(false);
            return;
        }
        TopologyDataSet dataSet = (TopologyDataSet) this.cboDataSet1.getSelectedItem();
        if( dataSet==null ) {
            this.setOkButtonEnabled(false);
            return;
        }
        TopologyRuleFactory ruleFactory = (TopologyRuleFactory) this.cboRule.getSelectedItem();
        if( ruleFactory==null ) {
            this.setOkButtonEnabled(false);
            return;
        }
        this.setOkButtonEnabled(true);
    }

    private void performDataSet1Selected() {
        TopologyDataSet dataSet = (TopologyDataSet) this.cboDataSet1.getSelectedItem();
        if( dataSet==null ) {
            this.setOkButtonEnabled(false);
            this.cboRule.setSelectedIndex(-1);
            this.cboRule.setEnabled(false);
            this.cboDataSet2.setEnabled(false);
            this.cboDataSet2.setSelectedItem(-1);
            return ;
        }
        TopologyManager manager = TopologyLocator.getTopologyManager();
        DefaultComboBoxModel<TopologyRuleFactory> model1 = new DefaultComboBoxModel<>();
        for (TopologyRuleFactory ruleFactory : manager.getRuleFactories(dataSet)) {
            model1.addElement(ruleFactory);
        }
        this.cboRule.setModel(model1);
        this.cboRule.setSelectedIndex(-1);
        this.cboRule.setEnabled(true);
    }

    private void performRuleSelected() {
        TopologyRuleFactory ruleFactory = (TopologyRuleFactory) this.cboRule.getSelectedItem();
        if( ruleFactory==null ) {
            this.setOkButtonEnabled(false);
            this.cboDataSet2.setEnabled(false);
            this.cboDataSet2.setSelectedItem(-1);
            return ;
        }
        DefaultComboBoxModel<TopologyDataSet> model1 = new DefaultComboBoxModel<>();
        if( ruleFactory.hasSecondaryDataSet() ) {
            for (TopologyDataSet dataSet : this.plan.getSecondaryDataSets(ruleFactory) ) {
                model1.addElement(dataSet);
            }
        }
        if( model1.getSize()==0 ) {
            this.cboDataSet2.setEnabled(false);
        } else {
            this.cboDataSet2.setEnabled(true);
        }
        this.cboDataSet1.setModel(model1);
        this.cboDataSet2.setSelectedItem(-1);
        
        this.lblImage.setIcon(new ImageIcon(ruleFactory.getImageDescription()));
        String description = ruleFactory.getDescription();
        description = description.replace("\\n", "<br>\n");
        description = description.replace("\n", "<br>\n");
        this.lblDescription.setText("<html><p>"+description+"</p></html>");
        this.validateData();
    }
    
    public TopologyRule getRule() {
        TopologyDataSet dataSet1 = (TopologyDataSet) this.cboDataSet1.getSelectedItem();
        TopologyRuleFactory ruleFactory = (TopologyRuleFactory) this.cboRule.getSelectedItem();
        if( dataSet1==null || ruleFactory==null ) {
            return null;
        }
        TopologyDataSet dataSet2 = (TopologyDataSet) this.cboDataSet2.getSelectedItem();
        double tolerance = this.plan.getTolerance();
        try {
            tolerance = Double.parseDouble(this.txtTolerance.getText());
        } catch(Exception ex) {
            
        }
        TopologyRule rule = ruleFactory.createRule(plan, dataSet1.getName(), dataSet2.getName(), tolerance);
        return rule;
    }
    
}
