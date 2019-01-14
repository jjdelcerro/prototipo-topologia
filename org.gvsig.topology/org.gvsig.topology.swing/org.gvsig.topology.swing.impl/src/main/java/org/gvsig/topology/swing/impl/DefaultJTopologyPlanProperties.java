package org.gvsig.topology.swing.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.i18n.I18nManager;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.tools.swing.api.ToolsSwingManager;
import org.gvsig.tools.swing.api.windowmanager.Dialog;
import org.gvsig.tools.swing.api.windowmanager.WindowManager_v2;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyLocator;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyRule;
import org.gvsig.topology.swing.api.JTopologyPlanProperties;

/**
 *
 * @author jjdelcerro
 */
public class DefaultJTopologyPlanProperties
        extends DefaultJTopologyPlanPropertiesView
        implements JTopologyPlanProperties {

    private TopologyPlan plan;

    public DefaultJTopologyPlanProperties() {
        this.plan = TopologyLocator.getTopologyManager().createTopologyPlan();

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
        tsm.translate(this.lblTolerance);
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

    @Override
    public void put(TopologyPlan thePlan) {
        this.plan = TopologyLocator.getTopologyManager().createTopologyPlan();
        this.plan.setName(thePlan.getName());
        this.plan.setTolerance(thePlan.getTolerance());
        for (TopologyDataSet dataSet : thePlan.getDataSets()) {
            this.plan.addDataSet(dataSet);
        }
        for (TopologyRule rule : thePlan.getRules()) {
            this.plan.addRule(rule);
        }
        
        this.txtName.setText(this.plan.getName());
        this.txtTolerance.setText(String.valueOf(this.plan.getTolerance()));
        this.lstDataSets.removeAll();
        DefaultListModel<TopologyDataSet> modelDataSets = new DefaultListModel<>();
        for (TopologyDataSet dataSet : thePlan.getDataSets()) {
            modelDataSets.addElement(dataSet);
        }
        this.lstDataSets.setModel(modelDataSets);

        this.lstRules.removeAll();
        DefaultListModel<TopologyRule> modelRules = new DefaultListModel<>();
        for (TopologyRule rule : thePlan.getRules()) {
            modelRules.addElement(rule);
        }
        this.lstRules.setModel(modelRules);
    }

    @Override
    public TopologyPlan fetch(TopologyPlan thePlan) {
        if( thePlan == null ) {
            thePlan = TopologyLocator.getTopologyManager().createTopologyPlan();
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
        SelectDataSetDialog panel = new SelectDataSetDialog();
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
        I18nManager i18n = ToolsLocator.getI18nManager();
        WindowManager_v2 winManager = (WindowManager_v2) ToolsSwingLocator.getWindowManager();
        CreateRuleDialog panel = new CreateRuleDialog(this.plan);
        Dialog dlg = winManager.createDialog(
                panel,
                i18n.getTranslation("_Add_new_rule"),
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
