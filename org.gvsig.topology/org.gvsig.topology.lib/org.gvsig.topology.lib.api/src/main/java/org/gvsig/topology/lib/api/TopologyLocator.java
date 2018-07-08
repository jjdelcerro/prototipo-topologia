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
package org.gvsig.topology.lib.api;

import org.gvsig.tools.locator.AbstractLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

@SuppressWarnings("rawtypes")
public class TopologyLocator extends AbstractLocator {

	private static final String LOCATOR_NAME = "TopologyLocator";
	
	public static final String TOPOLOGY_MANAGER_NAME =
			"org.gvsig.topology.manager";

	private static final String TOPOLOGY_MANAGER_DESCRIPTION =
			"Topology Manager of gvSIG";
	
	private static final TopologyLocator instance = new TopologyLocator();

	private TopologyLocator() {

	}

	/**
	 * Return the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static TopologyLocator getInstance() {
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
	public static TopologyManager getTopologyManager()
			throws LocatorException {
		return (TopologyManager) getInstance().get(TOPOLOGY_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the TopologyManager interface.
	 * 
	 * @param clazz
	 *            implementing the TopologyManager interface
	 */
	public static void registerTopologyManager(Class clazz) {
		getInstance().register(TOPOLOGY_MANAGER_NAME,
				TOPOLOGY_MANAGER_DESCRIPTION, clazz);
	}

	public static void registerDefaultTopologyManager(Class clazz) {
		getInstance().registerDefault(TOPOLOGY_MANAGER_NAME,
				TOPOLOGY_MANAGER_DESCRIPTION, clazz);
	}


}
