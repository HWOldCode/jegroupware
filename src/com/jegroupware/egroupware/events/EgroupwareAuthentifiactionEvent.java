/*
 * jegroupware - Egroupware Java Client
 * this is a java http/https egroupware client, for use please check the example
 *
 * @link https://www.hw-softwareentwicklung.de
 * @author Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @package jegroupware
 * @copyright (c) 2012-16 by Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @license http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)
 * @version 14.2.6
 */
package com.jegroupware.egroupware.events;

import com.jegroupware.egroupware.Egroupware;

/**
 * EgroupwareAuthentifiactionEvent
 * @author Stefan Werfling
 */
public class EgroupwareAuthentifiactionEvent extends EgroupwareEvent {

	/**
	 * exception
	 */
    protected Exception _exc;

    /**
     * EgroupwareAuthentifiactionEvent
     * @param source
     */
    public EgroupwareAuthentifiactionEvent(Egroupware source) {
        super(source);
    }

    /**
     * EgroupwareAuthentifiactionEvent
     * @param source
     * @param exc
     */
    public EgroupwareAuthentifiactionEvent(Egroupware source, Exception exc) {
        super(source);
        this._exc = exc;
    }

    /**
     * getException
     * @return
     */
    public Exception getException() {
       return this._exc;
    }
}
