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
package com.jegroupware.egroupware;

import java.io.*;

/**
 * EgroupwareSession
 *
 * @author Stefan Werfling
 */
public class EgroupwareSession implements Serializable {

	/**
	 * serialVersionUID
	 */
	private final static long serialVersionUID = 1;

    public static final String EGW_SESSION_ID       = "sessionid";
    public static final String EGW_KP3              = "kp3";
    public static final String EGW_LAST_LOGIN_ID    = "last_loginid";
    public static final String EGW_LAST_DOMAIN      = "last_domain";
    public static final String EGW_DOMAIN           = "domain";

    /**
     * sessionid
     */
    protected String _sessionid = "";

    /**
     * kp3
     */
    protected String _kp3 = "";

    /**
     * last loginid
     */
    protected String _last_loginid = "";

    /**
     * last domain
     */
    protected String _last_domain = "";

    /**
     * domain
     */
    protected String _domain = "";

    /**
     * constructor
     */
    public EgroupwareSession() {
        // default
    }

    /**
     * getCookieParameters
     * @return String[]
     */
    static public String[] getCookieParameters() {
        return new String[]{
            EGW_SESSION_ID,
            EGW_KP3,
            EGW_LAST_LOGIN_ID,
            EGW_LAST_DOMAIN,
            EGW_DOMAIN
            };
    }

    /**
     * setValue
     * @param key String
     * @param value String
     */
    public void setValue(String key, String value) {
        if( key.compareTo(EGW_SESSION_ID) == 0 ) {
            this.setSessionId(value);
        }
        else if( key.compareTo(EGW_KP3) == 0 ) {
            this.setKp3(value);
        }
        else if( key.compareTo(EGW_LAST_LOGIN_ID) == 0 ) {
            this.setLastLoginId(value);
        }
        else if( key.compareTo(EGW_LAST_DOMAIN) == 0 ) {
            this.setLastDomain(value);
        }
        else if( key.compareTo(EGW_DOMAIN) == 0 ) {
            this.setDomain(value);
        }
    }

    /**
     * getValue
     *
     * @param key
     * @return String
     */
    public String getValue(String key) {
        if( key.compareTo(EGW_SESSION_ID) == 0 ) {
            return this.getSessionId();
        }
        else if( key.compareTo(EGW_KP3) == 0 ) {
            return this.getKp3();
        }
        else if( key.compareTo(EGW_LAST_LOGIN_ID) == 0 ) {
            return this.getLastLoginId();
        }
        else if( key.compareTo(EGW_LAST_DOMAIN) == 0 ) {
            return this.getLastDomain();
        }
        else if( key.compareTo(EGW_DOMAIN) == 0 ) {
            return this.getDomain();
        }

        return null;
    }

    /**
     * setSessionId
     * @param value String
     */
    public void setSessionId(String value) {
        this._sessionid = value;
    }

    /**
     * getSessionId
     * @return String
     */
    public String getSessionId() {
        return this._sessionid;
    }

    /**
     * setKp3
     * @param value String
     */
    public void setKp3(String value) {
        this._kp3 = value;
    }

    /**
     * getKp3
     * @return String
     */
    public String getKp3() {
        return this._kp3;
    }

    /**
     * setLastLoginId
     * @param value String
     */
    public void setLastLoginId(String value) {
        this._last_loginid = value;
    }

    /**
     * getLastLoginId
     * @return String
     */
    public String getLastLoginId() {
        return this._last_loginid;
    }

    /**
     * setLastDomain
     * @param value String
     */
    public void setLastDomain(String value) {
        this._last_domain = value;
    }

    /**
     * getLastDomain
     * @return String
     */
    public String getLastDomain() {
        return this._last_domain;
    }

    /**
     * setDomain
     * @param value String
     */
    public void setDomain(String value) {
        this._domain = value;
    }

    /**
     * getDomain
     * @return String
     */
    public String getDomain() {
        return this._domain;
    }

    /**
     * getRawCookie
     * @return
     */
    public String getRawCookie() {
        String cookie = "";

        for( String key:  EgroupwareSession.getCookieParameters() ) {
            String value = this.getValue(key);

            cookie = cookie + key + "=" + value + ";";
        }

        return cookie;
    }

	/**
	 * toSerializableString
	 *
	 * @param session
	 * @return
	 * @throws IOException
	 */
	static public String toSerializableString(EgroupwareSession session) throws IOException {
		ByteArrayOutputStream _baos = new ByteArrayOutputStream();
		ObjectOutputStream _oos = new ObjectOutputStream(_baos);

		_oos.writeObject(session);
		_oos.flush();

		return _baos.toString("ISO-8859-1");
	}

	/**
	 * fromSerializableString
	 *
	 * @param serialize
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static public EgroupwareSession fromSerializableString(String serialize) throws IOException, ClassNotFoundException {
		ObjectInputStream _ois = new ObjectInputStream(new ByteArrayInputStream(serialize.getBytes("ISO-8859-1")));
		Object _o = _ois.readObject();
		_ois.close();

		return (EgroupwareSession) _o;
	}
}
