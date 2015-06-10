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
 * EGroupwareExceptionLoginStatus
 * @author Stefan Werfling
 */
public class EGroupwareExceptionLoginStatus extends Exception {

    protected int _status = -1;

    public EGroupwareExceptionLoginStatus(int status) {
        super(EGroupwareExceptionLoginStatus.getStatusMessage(status));
        this._status = status;
    }

    public int getStatus() {
        return this._status;
    }

    static public String getStatusMessage(int status) {
        String msg = "";

        switch( status ) {
            case 1:
                msg = "You have been successfully logged out";
                break;

            case 2:
                msg = "Sorry, your login has expired";
                break;

            case 4:
                msg = "Cookies are required to login to this site";
                break;

            case 5:
                msg = "Bad login or password";
                break;

            case 98:
                msg = "Account is expired";
                break;

            case 99:
                msg = "Blocked, too many attempts";
                break;

            case 10:
                msg = "Your session timed out, please log in again";
                break;

            default:
                msg = "Code: " + Integer.toString(status);
        }

        return msg;
    }
}
