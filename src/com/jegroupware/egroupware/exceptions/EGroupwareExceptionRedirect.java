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
package com.jegroupware.egroupware.exceptions;

/**
 * EGroupwareExceptionRedirect
 * @author Stefan Werfling
 */
public class EGroupwareExceptionRedirect extends Exception {

    /**
     * URL
     */
    protected String _url = "";

    /**
     * EGroupwareExceptionRedirect
     * @param url
     */
    public EGroupwareExceptionRedirect(String url) {
        super("egroupware redirect to url: " + url);
        this._url = url;
    }

    public String getUrl() {
        return this._url;
    }
}
