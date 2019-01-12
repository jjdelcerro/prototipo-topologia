/**
 * gvSIG. Desktop Geographic Information System.
 *
 * Copyright (C) 2007-2013 gvSIG Association.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * For any additional information, do not hesitate to contact us
 * at info AT gvsig.com, or visit our website www.gvsig.com.
 */
package org.gvsig.topology.app.mainplugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import org.apache.commons.lang.StringUtils;
import org.gvsig.andami.plugins.Extension;
import org.gvsig.app.ApplicationLocator;
import org.gvsig.app.ApplicationManager;
import org.gvsig.app.project.documents.Document;
import org.gvsig.app.project.documents.view.ViewDocument;
import org.gvsig.app.project.documents.view.ViewManager;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.tools.swing.api.windowmanager.Dialog;
import org.gvsig.tools.swing.api.windowmanager.WindowManager;
import org.gvsig.tools.swing.api.windowmanager.WindowManager_v2;
import org.gvsig.topology.lib.api.TopologyLocator;
import org.gvsig.topology.lib.api.TopologyManager;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.swing.api.JTopologyPlanProperties;
import org.gvsig.topology.swing.api.JTopologyReport;
import org.gvsig.topology.swing.api.TopologySwingLocator;
import org.gvsig.topology.swing.api.TopologySwingManager;

/**
 *
 * @author jjdelcerro
 */
public class TopologyExtension extends Extension {

    @Override
    public void initialize() {
        
    }

    @Override
    public void postInitialize() {
        AppTopologyServices services = new AppTopologyServices();
        
        TopologyManager manager = TopologyLocator.getTopologyManager();
        manager.setDefaultServices(services);
        
        TopologySwingManager swingManager = TopologySwingLocator.getTopologySwingManager();
        swingManager.setDefaultServices(services);
        
    }

    
    @Override
    public void execute(String action) {
        if( StringUtils.equalsIgnoreCase("tools-topology-create-or-modify", action) ) {
            ApplicationManager application = ApplicationLocator.getManager();
            final ViewDocument view = (ViewDocument) application.getActiveDocument(ViewManager.TYPENAME);
            if( view==null ) {
                // TODO: Mensaje de se necesita una vista 
                return;
            }
            TopologyManager manager = TopologyLocator.getTopologyManager();
            TopologySwingManager swingManager = TopologySwingLocator.getTopologySwingManager();
            WindowManager_v2 winManager = (WindowManager_v2) ToolsSwingLocator.getWindowManager();
            
            final JTopologyPlanProperties panel = swingManager.createJTopologyPlan();

            TopologyPlan plan = manager.createTopologyPlan();
            String jsonPlan = (String) view.getProperty("TopologyPlan");
            if( !StringUtils.isEmpty(jsonPlan) ) {
                plan.fromJSON(jsonPlan);
            } 
            panel.put(plan);
            
            final Dialog dlg = winManager.createDialog(
                    panel.asJComponent(),
                    "_Create_topology_plan",
                    null, 
                    WindowManager_v2.BUTTONS_OK_CANCEL
            );
            dlg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if( dlg.getAction()==WindowManager_v2.BUTTON_OK ) {
                        TopologyPlan plan = panel.fetch(null);
                        view.setProperty("TopologyPlan", plan.toJSON().toString());
                    }
                }
            });
            dlg.show(WindowManager.MODE.WINDOW);

        } else if( StringUtils.equalsIgnoreCase("tools-topology-execute", action) ) {
            ApplicationManager application = ApplicationLocator.getManager();
            final ViewDocument view = (ViewDocument) application.getActiveDocument(ViewManager.TYPENAME);
            if( view==null ) {
                // TODO: Mensaje de se necesita una vista 
                return;
            }
            TopologyManager manager = TopologyLocator.getTopologyManager();
            final TopologySwingManager swingManager = TopologySwingLocator.getTopologySwingManager();
            final WindowManager_v2 winManager = (WindowManager_v2) ToolsSwingLocator.getWindowManager();
            

            String jsonPlan = (String) view.getProperty("TopologyPlan");
            if( StringUtils.isEmpty(jsonPlan) ) {
                // TODO: Mensaje de crear plan
                return;
            }
           
            final TopologyPlan plan = manager.createTopologyPlan();
            plan.fromJSON(jsonPlan);
            JTopologyReport panel = swingManager.createJTopologyReport(plan);
            panel.put(plan.getReport());
            winManager.showWindow(
                    panel.asJComponent(), 
                    "_Error_inspector",
                    WindowManager.MODE.TOOL
            );

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    plan.execute();
                }
            }, "_Topology_plan "+ plan.getName());
            th.start();
        }
    }

    @Override
    public boolean isEnabled() {
        ApplicationManager application = ApplicationLocator.getManager();
        Document view = application.getActiveDocument(ViewManager.TYPENAME);
        return view!=null;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
    
}
