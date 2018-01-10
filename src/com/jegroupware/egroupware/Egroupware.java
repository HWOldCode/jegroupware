/*
 * jegroupware - Egroupware Java Client
 * this is a java http/https egroupware client, for use please check the example
 *
 * @link https://www.hw-softwareentwicklung.de
 * @author Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @package jegroupware
 * @copyright (c) 2012-18 by Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @license http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)
 * @version 17.1
 */
package com.jegroupware.egroupware;

import com.jegroupware.egroupware.events.EgroupwareAuthentifiactionEvent;
import com.jegroupware.egroupware.events.EgroupwareEvent;
import com.jegroupware.egroupware.events.EgroupwareEventListener;
import com.jegroupware.egroupware.events.EgroupwareEventMulticaster;
import com.jegroupware.egroupware.events.EgroupwareEventRequest;
import com.jegroupware.egroupware.events.EgroupwareLogoutEvent;
import com.jegroupware.egroupware.exceptions.EGroupwareExceptionAnonymous;
import com.jegroupware.egroupware.exceptions.EGroupwareExceptionLoginStatus;
import com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect;
import com.jegroupware.egroupware.exceptions.EGroupwareExceptionSiteNotFound;
import com.jegroupware.egroupware.exceptions.EGroupwareExceptionUnknowLoginDomain;
import com.jegroupware.egroupware.exceptions.EGroupwareExceptionUserConfig;
import com.jegroupware.egroupware.exceptions.EgroupwareExceptionResponseEmpty;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Egroupware
 * @author Stefan Werfling
 */
public class Egroupware implements Runnable {

	/**
	 * Static
	 */
	static {
		EgroupwareHttp.addUserAgent("Java;" + System.getProperty("java.version"));
		EgroupwareHttp.addUserAgent(EgroupwareConst.JEGW_TITLE + ";" + EgroupwareConst.JEGW_VERSION);
	}

    /**
	 * Vars
	 */
    public static final String EGW_HTTP_POST_VAR_PASSWORD_TYPE  = "passwd_type";
    public static final String EGW_HTTP_POST_VAR_ACCOUNT_TYPE   = "account_type";
    public static final String EGW_HTTP_POST_VAR_LOGINDOMAIN    = "logindomain";
    public static final String EGW_HTTP_POST_VAR_LOGIN          = "login";
    public static final String EGW_HTTP_POST_VAR_PASSWD         = "passwd";
    public static final String EGW_HTTP_POST_VAR_SUBMITIT       = "submitit";

    /**
	 * values
	 */
    public static final String EGW_HTTP_POST_PASSWORD_TYPE_TEXT     = "text";
    public static final String EGW_HTTP_POST_ACCOUNT_TYPE_U         = "u";
    public static final String EGW_HTTP_POST_SUBMITIT               = "++Anmelden++";

    /**
	 * Index
	 */
    public static final String EGW_HTTP_INDEXSCRIPT         = "index.php";

    // LoginScript
    public static final String EGW_HTTP_LOGINSCRIPT         = "login.php";
    public static final String EGW_HTTP_LOGOUTSCRIPT        = "logout.php";
    public static final String EGW_HTTP_GET_PHPGW_FORWARD   = "phpgw_forward";

    // links parts
    public static final String EGW_HTTP_LOGINLINK           = "login.php?cd=";

    // login status
    public static final int EGW_HTTP_LOGIN_STATUS_NONE      = -1;
    public static final int EGW_HTTP_LOGIN_STATUS_LOGIN     = 999;

    /**
     * list of instance of Egroupware
     */
    static protected List<Egroupware> _instances = new ArrayList<>();

	/**
	 * egroupware verson
	 */
	static protected String _version = null;

    /**
     * getInstance
     * @param config
     * @return Egroupware
     */
    static public Egroupware getInstance(EgroupwareConfig config) {
        Egroupware egw = null;

        for( Egroupware _tinstance : Egroupware._instances ) {
            egw = _tinstance;

            if( egw.getConfig().equals(config) ) {
                return egw;
            }
        }

        egw = new Egroupware(config);

        Egroupware._instances.add(egw);

        return egw;
    }

    /**
     * findInstance
     * @param username
     * @return
     */
    static public Egroupware findInstance(String username) {
        for( Egroupware _tinstance : Egroupware._instances ) {
            if( _tinstance.getConfig().getUser().equals(username) ) {
                return _tinstance;
            }
        }

        return null;
    }

	/**
	 * getLastInstance
	 * @return
	 */
	static public Egroupware getLastInstance() {
		if( Egroupware._instances.size() > 0 ) {
			return Egroupware._instances.get(Egroupware._instances.size()-1);
		}

		return null;
	}

    /**
     * shutdownInstances
     */
    static public void shutdownInstances() {
        for( Egroupware _tinstance : Egroupware._instances ) {
            if( _tinstance.isLogin() ) {
                if( !_tinstance.getConfig().getAnonymous() ) {
                    if( _tinstance.getConfig().getAutoLogout() ) {
                        try {
                            _tinstance.logout();
                        } catch (Exception ex) {
                            Logger.getLogger(Egroupware.class.getName()).log(
                                Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

	/**
	 * setVersion
	 * @param version
	 */
	static public void setVersion(String version) {
		Egroupware._version = version;
	}

	/**
	 * getVersion
	 * @return
	 */
	static public String getVersion() {
		return Egroupware._version;
	}

    /**
     * EgroupwareConfig
     */
    protected EgroupwareConfig _config = null;

    /**
     * EgroupwareSession
     */
    protected EgroupwareSession _session = null;

    /**
     * EgroupwareHttp
     */
    protected EgroupwareHttp _http = null;

    /**
     * Domains
     */
    protected List<String> _domains = new ArrayList<>();

    /**
     * Thread is egw login
     */
    protected Thread _cthread = null;

    /**
     * is Requesting
     */
    protected Boolean _isRequesting = false;

    /**
     * EgroupwareEventMulticaster
     */
    protected EgroupwareEventMulticaster _eventMulticaster = new EgroupwareEventMulticaster();

    /**
     * Egroupware
     * @param config EgroupwareConfig
     */
    public Egroupware(EgroupwareConfig config) {
        this._config = config;
        this._http = new EgroupwareHttp();
        this._http.setSocketTimeout(this._config.getSocketTimeout());

        try {
            if( this._config.getAutoLogin() && (!this.isLogin()) ) {
                this.login();
            }
        }
        catch( Exception exp ) {
            // TODO
        }

        Runtime.getRuntime().addShutdownHook( new Thread() {
            @Override
            public void run() {
                Egroupware.shutdownInstances();
            }
        });

    }

    /**
     * getConfig
     * @return EgroupwareConfig
     */
    public EgroupwareConfig getConfig() {
        return this._config;
    }

    /**
     * isLogin
     * @return Boolean
     */
    public Boolean isLogin() {
        return this._session != null;
    }

    /**
     * login
     */
    public Boolean login() throws Exception {
        try {
            if( this._config.getAnonymous() ) {
                throw new EGroupwareExceptionAnonymous();
            }

            if( this._domains.isEmpty() ) {
                this.getLoginDomains();
            }

            if( this._domains.indexOf(this._config.getDomain()) < 0 ) {
                throw new EGroupwareExceptionUnknowLoginDomain();
            }

			// is username empty
			if( "".equals(this._config.getUser().trim()) ) {
				throw new EGroupwareExceptionUserConfig("username");
			}

			// is password empty
			if( "".equals(this._config.getPassword().trim()) ) {
				throw new EGroupwareExceptionUserConfig("password");
			}

            String url = this._config.getUrl() + EGW_HTTP_LOGINSCRIPT +
                "?" + EGW_HTTP_GET_PHPGW_FORWARD + "=";

            Map<String, String> postdata = new HashMap<>();
            postdata.put(EGW_HTTP_POST_VAR_PASSWORD_TYPE, EGW_HTTP_POST_PASSWORD_TYPE_TEXT);
            postdata.put(EGW_HTTP_POST_VAR_ACCOUNT_TYPE, EGW_HTTP_POST_ACCOUNT_TYPE_U);
            postdata.put(EGW_HTTP_POST_VAR_LOGINDOMAIN, this._config.getDomain());
            postdata.put(EGW_HTTP_POST_VAR_LOGIN, this._config.getUser());
            postdata.put(EGW_HTTP_POST_VAR_PASSWD, this._config.getPassword());
            postdata.put(EGW_HTTP_POST_VAR_SUBMITIT, EGW_HTTP_POST_SUBMITIT);

            String buffer = this._http.sendPOST(url, postdata);

            if( this._http.isSiteNotFound() ) {
                throw new EGroupwareExceptionSiteNotFound(url);
            }

            this._checkLoginStatus();

            if( buffer.length() == 0 ) {
                throw new EgroupwareExceptionResponseEmpty(url);
            }

            // ---------------------------------------------------------------------

            EgroupwareSession _nSession = new EgroupwareSession();

            for( String key:  EgroupwareSession.getCookieParameters() ) {
                String value = this._http.getCookieParameter(key);

                if( value != null ) {
                    _nSession.setValue(key, value);
                }
                else {
                    throw new Exception("cookie parameter not found: " +
                        key + " sendet cookie: '" + this._http.getRawCookie() + "'");
                }
            }

            this._session = _nSession;

            // event authentification succeeded
            this._eventMulticaster.authentificationSucceeded(
                new EgroupwareAuthentifiactionEvent(
                    this));

            // create thread
            // -----------------------------------------------------------------
            if( this._config.getUseEgwThread() ) {
                this._cthread = new Thread(this);
                this._cthread.setDaemon(true);
                this._cthread.setPriority(Thread.MIN_PRIORITY);
                this._cthread.start();
            }
        }
        catch(
			EGroupwareExceptionLoginStatus |
            EGroupwareExceptionAnonymous |
            EgroupwareExceptionResponseEmpty |
            EGroupwareExceptionUserConfig |
            EGroupwareExceptionUnknowLoginDomain es )
		{
			// reset session
			this._session = null;

            // event authentification faild
            this._eventMulticaster.authentificationFailed(
                new EgroupwareAuthentifiactionEvent(
                    this,
                    es
                ));

			// fire to next
            throw es;
        }
        catch( Exception ex ) {
			// reset session
			this._session = null;

            // fire to next
            throw ex;
        }

        // return is login
        return this.isLogin();
    }

    /**
     * logout
     * @throws Exception
     */
    public void logout() throws Exception {
        try {
            if( this._config.getAnonymous() ) {
                throw new EGroupwareExceptionAnonymous();
            }

            if( !this.isLogin() ) {
                return;
            }

            String url = this._config.getUrl() + EGW_HTTP_LOGOUTSCRIPT;

            this._http.setRawCookie(this._session.getRawCookie());
            this._session = null;   // set empty

            String buffer = this._http.sendGET(url);

            if( buffer.length() == 0 ) {
                throw new EgroupwareExceptionResponseEmpty(url);
            }
        }
        catch( Exception ex ) {
            // event logout failed
            this._eventMulticaster.logoutFailed(
                new EgroupwareLogoutEvent(
                    this
                ));

            throw ex;
        }

        // event logout succeeded
        this._eventMulticaster.logoutSucceeded(
            new EgroupwareLogoutEvent(
                this
            ));

		// reset domains
		this._domains.clear();
    }

    /**
     * addListener
     * @param a
     */
    public void addListener(EgroupwareEventListener a) {
        this._eventMulticaster.add(a);
    }

    /**
     * _getLoginStatus
     * @param http
     * @return
     */
    protected int _getLoginStatus(EgroupwareHttp http) {
        if( http == null ) {
            http = this._http;
        }

        String _tbuff = http.getLocation();

        if( _tbuff == null ) {
            _tbuff = http.getRequestURI();
        }

        if( _tbuff == null ) {
            _tbuff = "";
        }

        int pos = _tbuff.indexOf(EGW_HTTP_LOGINLINK);
        int end = _tbuff.length();

        if( (pos != -1) && (end != -1) ) {
            String _tmp =_tbuff.substring( pos +
                    EGW_HTTP_LOGINLINK.length(), end);

            return Integer.parseInt(_tmp);
        }
        else if( (_tbuff.indexOf("http://") != -1) ||
                (_tbuff.indexOf("https://") != -1) )
        {
            if( _tbuff.indexOf("index.php?cd=yes") == -1 ) {
                return EGW_HTTP_LOGIN_STATUS_LOGIN;
            }
        }

        return EGW_HTTP_LOGIN_STATUS_NONE;
    }

    /**
     * _checkLoginStatus
     *
     * @throws EGroupwareExceptionLoginStatus
     */
    protected void _checkLoginStatus() throws EGroupwareExceptionLoginStatus {
        this._checkLoginStatus(null);
    }

    /**
     * _checkLoginStatus
     *
     * @param http
     * @throws EGroupwareExceptionLoginStatus
     */
    protected void _checkLoginStatus(EgroupwareHttp http) throws EGroupwareExceptionLoginStatus {
        if( !this._config.getAnonymous() ) {
            int status = this._getLoginStatus(http);

            if( (status > EGW_HTTP_LOGIN_STATUS_NONE) && (status < EGW_HTTP_LOGIN_STATUS_LOGIN) ) {
                this._session = null;
                throw new EGroupwareExceptionLoginStatus(status);
            }
        }
    }

    /**
     * getSession
     * @return EgroupwareSession|null
     */
    public EgroupwareSession getSession() {
        return this._session;
    }

    /**
     * setSession
     * @param session
     */
    public void setSession(EgroupwareSession session) {
        this._session = session;
    }

    /**
     * getLoginDomains
     * @return List<String>
     * @throws Exception
     */
    public List<String> getLoginDomains() throws Exception {
        if( this._domains.size() > 0 ) {
            return this._domains;
        }

        String url = this._config.getUrl() + EGW_HTTP_LOGINSCRIPT +
            "?" + EGW_HTTP_GET_PHPGW_FORWARD + "=";

        String buffer = this._http.sendGET(url);

        /**
         * Hidden Logindomain
         */
        int begin   = -1;
        int end     = -1;

        String search = "<input type=\"hidden\" name=\"logindomain\" value=\"";

        if( (begin = buffer.indexOf(search)) > -1 ) {
            end = buffer.indexOf("\"", begin + search.length());

            if( (begin != -1) && (end != -1) ) {
                String tmp = buffer.substring( begin +
                    search.length(), end);

                this._domains.add(tmp);

                return this._domains;
            }
        }

        /**
         * Select Logindomain
         */

        begin  = -1;
        end    = -1;
        search = "<select name=\"logindomain\"";

        if( (begin = buffer.indexOf(search)) > -1 ) {
            end = buffer.indexOf("</select>", begin + search.length());

            if( (begin != -1) && (end != -1) ) {
                String tmp = buffer.substring( begin +
                search.length(), end);

                tmp = tmp.trim();

                String ltmp[] = tmp.split("</option>");

                for( int i=0; i<ltmp.length; i++ ) {
                    String tbuffer	= ltmp[i];
                    String tsearch	= "value=\"";

                    int tbegin		= -1;
                    int tend		= -1;

                    if( (tbegin = tbuffer.indexOf(tsearch)) > -1 ) {
                        tend = tbuffer.indexOf("\"", tbegin + tsearch.length());

                        if( (begin != -1) && (tend != -1) ) {
                            String ttmp = tbuffer.substring( tbegin +
                                tsearch.length(), tend);

                            this._domains.add(ttmp);
                        }
                    }
                }
            }
        }

        return this._domains;
    }

    /**
     * createBrowserLink
     * @return String
     */
    public String createBrowserLink() throws EGroupwareExceptionAnonymous {
        return this.createBrowserLink(null);
    }

    /**
     * createBrowserLink
     * @param menuaction
     * @return String
     */
    public String createBrowserLink(String menuaction) throws EGroupwareExceptionAnonymous {
        if( this._config.getAnonymous() ) {
            throw new EGroupwareExceptionAnonymous();
        }

        if( !this.isLogin() ) {
            return null;
        }

        String url = this._config.getUrl() +
            EGW_HTTP_INDEXSCRIPT + "?notifiy=1&cd=yes";

        if( (menuaction != null) && (menuaction.length() > 0) ) {
            url = url + "&" + menuaction;
        }

        for( String key:  EgroupwareSession.getCookieParameters() ) {
            String value = this._session.getValue(key);

            url = url + "&" + key + "=" + value;
        }

        return url;
    }

    /**
     * request
     * @param request EgroupwarRequest
     * @return
     * @throws Exception
     */
    public EgroupwarRequest request(EgroupwarRequest request) throws Exception {
        try {
            if( !this._config.getAnonymous() ) {
                if( !this.isLogin() ) {
                    return null;
                }
            }

            String url = this._config.getUrl() + request.getRequestUrl();
            String buffer = "";

            this._http.setRawCookie(this._session.getRawCookie());

            EgroupwareHttp http = this._http.getNewHttpClone();

			if( request.getRequestType() == EgroupwarRequest.EGW_HTTP_REQUEST_FILE_UPLOAD ) {
				List<String> responses = http.sendFile(url, request.getRequestFile(), request.getPost());

				if( responses.size() > 0 ) {
					buffer = responses.get(responses.size()-1);
				}
			}
			else if( request.getRequestType() == EgroupwarRequest.EGW_HTTP_REQUEST_FILE_DOWNLOAD ) {
				request.setRequestFile(http.downloadFile(url));
				buffer = "{}";
			}
			else if( request.getRequestType() == EgroupwarRequest.EGW_HTTP_REQUEST_POST ) {
                buffer = http.sendPOST(url, request.getPost());
            }
            else {
                buffer = http.sendGET(url);
            }

            // ---------------------------------------------------------------------

            if( http.isSiteNotFound() ) {
                throw new EGroupwareExceptionSiteNotFound(url);
            }

            this._checkLoginStatus(http);

            if( buffer.length() == 0 ) {
                throw new EgroupwareExceptionResponseEmpty(url);
            }

            // ---------------------------------------------------------------------

            try {
                request.setRawContent(buffer);
            }
            catch( EGroupwareExceptionRedirect ex ) {
                 URL aURL = new URL(this._config.getUrl());

                this._http.sendGET(
                    aURL.getProtocol() + "://" + aURL.getHost() +
                        ":" + aURL.getPort() + ex.getUrl());

                this._checkLoginStatus();
            }

            //this._isRequesting = false;

            // listener succes request
            this._eventMulticaster.requestSucceeded(
                new EgroupwareEventRequest(this, request));

            return request;
        }
        catch( Exception ex ) {
            //this._isRequesting = false;

            // listener faild request
            this._eventMulticaster.requestFailed(
                new EgroupwareEventRequest(this, request));

            throw ex;
        }
    }

    /**
     * run
     * thread action
     */
    @Override
    public void run() {
        if( this._config.getAnonymous() ) {
            return;
        }

        try {
            while( true ) {
                if( !this.isLogin() ) {
                    return;
                }

                // -------------------------------------------------------------

                // thread event
                this._eventMulticaster.threadAction(new EgroupwareEvent(this));

                // -------------------------------------------------------------

                EgroupwareNotifications notif = new EgroupwareNotifications();

                try {
                    this.request(notif);
                }
                catch( Exception e ) {
                    Logger.getLogger(Egroupware.class.getName()).log(Level.SEVERE, null, e);
                }

                // -------------------------------------------------------------

                Thread.sleep(5000);
            }
        }
        catch( InterruptedException ex ) {
            Logger.getLogger(Egroupware.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * isRunnableAlive
     * @return is egroupware thread
     */
    public Boolean isRunnableAlive() {
        if( this._cthread != null ) {
            return this._cthread.isAlive();
        }

        return false;
    }

	/**
	 * getLang
	 * @return
	 */
	public EgroupwareLang getLang() {
		return new EgroupwareLang(this);
	}
}