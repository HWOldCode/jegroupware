/*
 * jegroupware - Egroupware Java Client
 * this is a java http/https egroupware client, for use please check the example
 *
 * @link https://www.hw-softwareentwicklung.de
 * @author Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @package jegroupware
 * @copyright (c) 2012-15 by Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @license http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)
 * @version 14.2.6
 */
package com.jegroupware.egroupware.events;

import java.util.EventListener;

/**
 * EgroupwareEventListener
 * @author Stefan Werfling
 */
public interface EgroupwareEventListener extends EventListener {

	/**
	 * authentificationSucceeded
	 * @param e
	 */
    public void authentificationSucceeded(EgroupwareAuthentifiactionEvent e);

	/**
	 * authentificationFailed
	 * @param e
	 */
    public void authentificationFailed(EgroupwareAuthentifiactionEvent e);

	/**
	 * logoutSucceeded
	 * @param e
	 */
    public void logoutSucceeded(EgroupwareLogoutEvent e);

	/**
	 * logoutFailed
	 * @param e
	 */
    public void logoutFailed(EgroupwareLogoutEvent e);

	/**
	 * requestSucceeded
	 * @param e
	 */
    public void requestSucceeded(EgroupwareEventRequest e);

	/**
	 * requestFailed
	 * @param e
	 */
    public void requestFailed(EgroupwareEventRequest e);

	/**
	 * threadAction
	 * @param e
	 */
    public void threadAction(EgroupwareEvent e);
}
