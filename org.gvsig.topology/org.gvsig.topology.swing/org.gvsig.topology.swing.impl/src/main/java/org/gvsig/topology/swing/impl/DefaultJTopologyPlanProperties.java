package org.gvsig.topology.swing.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.tools.swing.api.ToolsSwingManager;
import org.gvsig.tools.swing.api.windowmanager.Dialog;
import org.gvsig.tools.swing.api.windowmanager.WindowManager_v2;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyLocator;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyRule;
import org.gvsig.topology.swing.api.JTopologyPlanProperties;
import org.gvsig.topology.swing.api.TopologySwingServices;

/**
 *
 * @author jjdelcerro
 */
public class DefaultJTopologyPlanProperties
        extends DefaultJTopologyPlanPropertiesView
        implements JTopologyPlanProperties {

    private TopologyPlan plan;
    private TopologySwingServices services;

    public DefaultJTopologyPlanProperties(TopologySwingServices services) {
        this.services = services;
        this.plan = TopologyLocator.getTopologyManager().createTopologyPlan(null);

        this.initComponents();
    }

    private void initComponents() {
        this.translate();
        
        this.btnAddDataSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAddDataSet();
            }
        });
        this.btnRemoveDataSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRemoveDataSet();
            }
        });
        this.btnAddRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAddRule();
            }
        });
        this.btnRemoveRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRemoveRule();
            }
        });
        this.lstDataSets.setModel(new DefaultListModel());
        this.lstDataSets.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                performDataSetSelected();
            }
        });
        this.lstRules.setModel(new DefaultListModel());
        this.lstRules.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                performRuleSelected();
            }
        });
        this.setPreferredSize(new Dimension(450, 300));
    }

    
    private void translate() {
        ToolsSwingManager tsm = ToolsSwingLocator.getToolsSwingManager();
        tsm.translate(this.btnAddRule);
        tsm.translate(this.btnRemoveRule);
        tsm.translate(this.btnAddDataSet);
        tsm.translate(this.btnRemoveDataSet);
        tsm.translate(this.lblName);
        tsm.translate(this.tabPanel);
    }
    
    public void performDataSetSelected() {
        if( lstDataSets.getValueIsAdjusting()  ) {
            return;
        }
        btnRemoveDataSet.setEnabled(!lstDataSets.isSelectionEmpty());
    }
    
    public void performRuleSelected() {
        if( lstRules.getValueIsAdjusting()  ) {
            return;
        }
        btnRemoveRule.setEnabled(!lstRules.isSelectionEmpty());
    }
    
    public void setServices(TopologySwingServices services) {
        this.services = services;
    }

    @Override
    public TopologySwingServices getServices() {
        return this.services;
    }

    @Override
    public void put(TopologyPlan thePlan) {
        this.plan = TopologyLocator.getTopologyManager().createTopologyPlan(thePlan.getTopologyServices());
        this.plan.setName(thePlan.getName());
        for (TopologyDataSet dataSet : thePlan.getDataSets()) {
            this.plan.addDataSet(dataSet);
        }
        for (TopologyRule rule : thePlan.getRules()) {
            this.plan.addRule(rule);
        }
        
        this.txtName.setText(this.plan.getName());
        this.lstDataSets.removeAll();
        DefaultListModel<TopologyDataSet> modelDataSets = new DefaultListModel<>();
        for (TopologyDataSet dataSet : thePlan.getDataSets()) {
            modelDataSets.addElement(dataSet);
        }
        this.lstDataSets.setModel(modelDataSets);

        this.lstDataSets.removeAll();
        DefaultListModel<TopologyRule> modelRules = new DefaultListModel<>();
        for (TopologyRule rule : thePlan.getRules()) {
            modelRules.addElement(rule);
        }
        this.lstDataSets.setModel(modelRules);
    }

    @Override
    public TopologyPlan fetch(TopologyPlan thePlan) {
        if( thePlan == null ) {
            thePlan = TopologyLocator.getTopologyManager().createTopologyPlan(this.services);
        } else {
            thePlan.clear();
        }
        thePlan.setName(thePlan.getName());
        for (TopologyDataSet dataSet : this.plan.getDataSets()) {
            thePlan.addDataSet(dataSet);
        }
        for (TopologyRule rule : this.plan.getRules()) {
            thePlan.addRule(rule);
        }
        return thePlan;
    }

    @Override
    public JComponent asJComponent() {
        return this;
    }

    private void performAddDataSet() {
        WindowManager_v2 winManager = (WindowManager_v2) ToolsSwingLocator.getWindowManager();
        SelectDataSetDialog panel = new SelectDataSetDialog(this.services);
        Dialog dlg = winManager.createDialog(
                panel,
                "_Select_a_dataset",
                null, 
                WindowManager_v2.BUTTONS_OK_CANCEL
        );
        panel.setDialog(dlg);
        dlg.show(WindowManager_v2.MODE.DIALOG);
        if( dlg.getAction()==WindowManager_v2.BUTTON_OK) {
            TopologyDataSet dataSet = panel.getDataSet();
            if( dataSet != null ) {
                this.plan.addDataSet(dataSet);
                DefaultListModel model = (DefaultListModel) this.lstDataSets.getModel();
                model.addElement(dataSet);
            }
        }
    }

    private void performRemoveDataSet() {
        if( lstDataSets.getValueIsAdjusting()  ) {
            return;
        }
        if( lstDataSets.isSelectionEmpty() ) {
            return;
        }
        TopologyDataSet dataSet = (TopologyDataSet) lstDataSets.getSelectedValue();
        this.plan.removeDataSet(dataSet);
        lstDataSets.remove(lstDataSets.getSelectedIndex());
    }

    private void performAddRule() {
        WindowManager_v2 winManager = (WindowManager_v2) ToolsSwingLocator.getWindowManager();
        CreateRuleDialog panel = new CreateRuleDialog(this.plan);
        Dialog dlg = winManager.createDialog(
                panel,
                "_Add_new_rule",
                null, 
                WindowManager_v2.BUTTONS_OK_CANCEL
        );
        panel.setDialog(dlg);
        dlg.show(WindowManager_v2.MODE.DIALOG);
        if( dlg.getAction()==WindowManager_v2.BUTTON_OK) {
            TopologyRule rule = panel.getRule();
            if( rule != null ) {
                this.plan.addRule(rule);
                DefaultListModel model = (DefaultListModel) this.lstRules.getModel();
                model.addElement(rule);
            }
        }
    }

    private void performRemoveRule() {
        if( lstRules.getValueIsAdjusting()  ) {
            return;
        }
        if( lstRules.isSelectionEmpty() ) {
            return;
        }
        TopologyRule rule = (TopologyRule) lstRules.getSelectedValue();
        this.plan.removeRule(rule);
        lstRules.remove(lstRules.getSelectedIndex());
    }

}
