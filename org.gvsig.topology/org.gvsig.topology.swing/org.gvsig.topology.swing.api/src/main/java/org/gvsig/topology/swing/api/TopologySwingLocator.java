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
package org.gvsig.topology.swing.api;

import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

@SuppressWarnings("rawtypes")
public class TopologySwingLocator extends AbstractLocator {

	private static final String LOCATOR_NAME = "TopologySwingLocator";
	
	public static final String TOPOLOGY_SWING_MANAGER_NAME =
			"org.gvsig.topology.swing.manager";

	private static final String TOPOLOGY_SWING_MANAGER_DESCRIPTION =
			"Topology Swing Manager of gvSIG";
	
	private static final TopologySwingLocator instance = new TopologySwingLocator();

	private TopologySwingLocator() {

	}

	/**
	 * Return the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static TopologySwingLocator getInstance() {
		return instance;
	}

        @Override
	public String getLocatorName() {
		return LOCATOR_NAME;
	}

	/**
	 * Return a reference to TopologyManager.
	 * 
	 * @return a reference to TopologyManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator#get(String)
	 */
	public static TopologySwingManager getTopologySwingManager()
			throws LocatorException {
		return (TopologySwingManager) getInstance().get(TOPOLOGY_SWING_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the TopologyManager interface.
	 * 
	 * @param clazz
	 *            implementing the TopologyManager interface
	 */
	public static void registerTopologySwingManager(Class clazz) {
		getInstance().register(TOPOLOGY_SWING_MANAGER_NAME,
				TOPOLOGY_SWING_MANAGER_DESCRIPTION, clazz);
	}

	public static void registerDefaultTopologySwingManager(Class clazz) {
		getInstance().registerDefault(TOPOLOGY_SWING_MANAGER_NAME,
				TOPOLOGY_SWING_MANAGER_DESCRIPTION, clazz);
	}


}
