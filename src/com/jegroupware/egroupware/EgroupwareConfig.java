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
 * EgroupwareConfig
 * @author Stefan Werfling
 */
public class EgroupwareConfig implements Serializable {

	/**
	 * serialVersionUID
	 */
	private final static long serialVersionUID = 1;

    /**
     * url
     */
    protected String _url = "";

    /**
     * domain
     */
    protected String _domain = "";

    /**
     * user
     */
    protected String _user = "";

    /**
     * password
     */
    protected String _password = "";

    /**
     * autologin
     */
    protected boolean _autoLogin = false;

    /**
     * autologout
     */
    protected boolean _autoLogout = false;

    /**
     * only request to anonymous scripts
     */
    protected boolean _anonymous = false;

    /**
     * socket timeout
     */
    protected int _socketTimeout = 1000;

    /**
     * default browser
     */
    protected String _defaultBrowser = "";

    /**
     * use a egw thread
     */
    protected boolean _egwThread = true;

    /**
     * EgroupwareConfig
     */
    public EgroupwareConfig() {
        // default constructor
    }

    /**
     * EgroupwareConfig
     * @param url
     * @param domain
     * @param user
     * @param password
     */
    public EgroupwareConfig(String url, String domain, String user, String password) {
        this.setUrl(url);
        this.setDomain(domain);
        this.setUser(user);
        this.setPassword(password);
    }

    /**
     * EgroupwareConfig
     * @param autoLogin
     * @param url
     * @param domain
     * @param user
     * @param password
     */
    public EgroupwareConfig(Boolean autoLogin, String url, String domain, String user, String password) {
        this.setAutoLogin(autoLogin);
        this.setUrl(url);
        this.setDomain(domain);
        this.setUser(user);
        this.setPassword(password);
    }

    /**
     * setUrl
     * @param url String
     */
    public void setUrl(String url) {
        this._url = url;
    }

    /**
     * getUrl
     * @return String
     */
    public String getUrl() {
        return this._url;
    }

    /**
     * setDomain
     * @param domain String
     */
    public void setDomain(String domain) {
        this._domain = domain;
    }

    /**
     * getDomain
     * @return String
     */
    public String getDomain() {
        return this._domain;
    }

    /**
     * setUser
     * @param user
     */
    public void setUser(String user) {
        this._user = user;
    }

    /**
     * getUser
     * @return String
     */
    public String getUser() {
        return this._user;
    }

    /**
     * setPassword
     * @param password
     */
    public void setPassword(String password) {
        this._password = password;
    }

    /**
     * getPassword
     * @return String
     */
    public String getPassword() {
        return this._password;
    }

    /**
     * getAutoLogin
     * @return Boolean
     */
    public Boolean getAutoLogin() {
        return this._autoLogin;
    }

    /**
     * setAutoLogin
     *
     * @param autologin Boolean
     */
    public void setAutoLogin(Boolean autologin) {
        this._autoLogin = autologin;
    }

    /**
     * setAutoLogout
     * @param autologout
     */
    public void setAutoLogout(Boolean autologout) {
        this._autoLogout = autologout;
    }

    /**
     * getAutoLogout
     * @return
     */
    public Boolean getAutoLogout() {
        return this._autoLogout;
    }

    /**
     * setAnonymous
     * @param anonymous
     */
    public void setAnonymous(Boolean anonymous) {
        this._anonymous = anonymous;
    }

    /**
     * getAnonymous
     * @return
     */
    public Boolean getAnonymous() {
        return this._anonymous;
    }

    /**
     * setSocketTimeout
     * @param timeout
     */
    public void setSocketTimeout(int timeout) {
        this._socketTimeout = timeout;
    }

    /**
     * getSocketTimeout
     * @return int
     */
    public int getSocketTimeout() {
        return this._socketTimeout;
    }

    /**
     * getDefaultBrowser
     * @return String
     */
    public String getDefaultBrowser() {
        return this._defaultBrowser;
    }

    /**
     * setUseEgwThread
     * @param enable
     */
    public void setUseEgwThread(boolean enable) {
        this._egwThread = enable;
    }

    /**
     * getUseEgwThread
     * @return boolean
     */
    public boolean getUseEgwThread() {
        return this._egwThread;
    }

	/**
	 * update
	 * @param config
	 */
	public void update(EgroupwareConfig config) {
		this._url				= config._url;
		this._user				= config._user;
		this._password			= config._password;
		this._anonymous			= config._anonymous;
		this._autoLogin			= config._autoLogin;
		this._autoLogout		= config._autoLogout;
		this._defaultBrowser	= config._defaultBrowser;
		this._domain			= config._domain;
		this._egwThread			= config._egwThread;
		this._socketTimeout		= config._socketTimeout;
	}

    /**
     * equals
     * @param config EgroupwareConfig
     * @return Boolean
     */
    public Boolean equals(EgroupwareConfig config) {
        if( config.getUrl().compareTo(this.getUrl()) != 0 ) {
            return false;
        }

        if( config.getDomain().compareTo(this.getDomain()) != 0 ) {
            return false;
        }

        if( config.getUser().compareTo(this.getUser()) != 0 ) {
            return false;
        }

        return true;
    }

	/**
	 * toSerializableString
	 * @param config
	 * @return
	 * @throws IOException
	 */
	static public String toSerializableString(EgroupwareConfig config) throws IOException {
		ByteArrayOutputStream _baos = new ByteArrayOutputStream();
		ObjectOutputStream _oos = new ObjectOutputStream(_baos);

		_oos.writeObject(config);
		_oos.flush();

		return _baos.toString("ISO-8859-1");
	}

	/**
	 * fromSerializableString
	 * @param serialize
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static public EgroupwareConfig fromSerializableString(String serialize) throws IOException, ClassNotFoundException {
		ObjectInputStream _ois = new ObjectInputStream(new ByteArrayInputStream(serialize.getBytes("ISO-8859-1")));
		Object _o = _ois.readObject();
		_ois.close();

		return (EgroupwareConfig) _o;
	}
}
