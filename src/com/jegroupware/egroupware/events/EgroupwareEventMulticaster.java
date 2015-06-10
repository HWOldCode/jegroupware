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

import com.jegroupware.egroupware.EgroupwarRequest;
import com.jegroupware.egroupware.Egroupware;
import java.util.Vector;

/**
 * EgroupwareEventMulticaster
 * @author Stefan Werfling
 */
public class EgroupwareEventMulticaster implements EgroupwareEventListener {

    /**
     * listener
     */
    protected Vector _listener = new Vector();

    /**
     * remove
     * @param l
     */
    public void remove(EgroupwareEventListener l) {
        this._listener.remove(l);
    }

    /**
     * add
     * @param a
     */
    public void add(EgroupwareEventListener a) {
        if( !this._listener.contains(a)) {
            this._listener.addElement(a);
        }
    }
    
    /**
     * authentificationSucceeded
     * @param e 
     */
    @Override
    public void authentificationSucceeded(EgroupwareAuthentifiactionEvent e) {
        for( int i=0; i<this._listener.size(); i++ ) {
            ((EgroupwareEventListener)
                this._listener.elementAt(i)).
                    authentificationSucceeded(e);
        }
    }
    
    /**
     * authentificationFailed
     * @param e 
     */
    @Override
    public void authentificationFailed(EgroupwareAuthentifiactionEvent e) {
        for( int i=0; i<this._listener.size(); i++ ) {
            ((EgroupwareEventListener)
                this._listener.elementAt(i)).
                    authentificationFailed(e);
        }
    }
    
    /**
     * logoutSucceeded
     * @param e 
     */
    @Override
    public void logoutSucceeded(EgroupwareLogoutEvent e) {
        for( int i=0; i<this._listener.size(); i++ ) {
            ((EgroupwareEventListener)
                this._listener.elementAt(i)).
                    logoutSucceeded(e);
        }
    }
    
    /**
     * logoutFailed
     * @param e 
     */
    @Override
    public void logoutFailed(EgroupwareLogoutEvent e) {
        for( int i=0; i<this._listener.size(); i++ ) {
            ((EgroupwareEventListener)
                this._listener.elementAt(i)).
                    logoutFailed(e);
        }
    }

    /**
     * requestSucceeded
     * 
     * @param e 
     */
    @Override
    public void requestSucceeded(EgroupwareEventRequest e) {
        for( int i=0; i<this._listener.size(); i++ ) {
            ((EgroupwareEventListener)
                this._listener.elementAt(i)).
                    requestSucceeded(e);
        }
    }

    /**
     * requestFailed
     * 
     * @param e 
     */
    @Override
    public void requestFailed(EgroupwareEventRequest e) {
        for( int i=0; i<this._listener.size(); i++ ) {
            ((EgroupwareEventListener)
                this._listener.elementAt(i)).
                    requestFailed(e);
        }
    }

    /**
     * threadAction
     * 
     * @param e 
     */
    @Override
    public void threadAction(EgroupwareEvent e) {
        for( int i=0; i<this._listener.size(); i++ ) {
            ((EgroupwareEventListener)
                this._listener.elementAt(i)).
                    threadAction(e);
        }
    }
}