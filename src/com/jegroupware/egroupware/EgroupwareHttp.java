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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * EgroupwareHttp
 * @author Stefan Werfling
 */
public class EgroupwareHttp {

    /**
     * connection
     */
    private HttpURLConnection _connection = null;

    // https trust manager, handle certificates
	static private EgroupwareHttpsTrustManager _tm = null;

    /**
     * use SSL (HTTPS)
     */
    private Boolean _useSSL = false;

    /**
     * socket timeout
     */
	private int _socketTimeout = 10000;

    /**
     * cookie
     */
    private String _cookie = "";

    /**
     * response is ajax (json)
     */
    private boolean _isAjax	= false;

    /**
     * user URL Encode by POST, see (http://en.wikipedia.org/wiki/POST_(HTTP))
     * default = true
     */
	private boolean _isUrlEncode			= true;

    /**
     * setSocketTimeout
     * @param timeout
     */
    public void setSocketTimeout(int timeout) {
        this._socketTimeout = timeout;
    }

    /**
     * _initConnection
     *
     * @param url
     * @throws Exception
     */
    protected void _initConnection(String url) throws Exception {
        if( url.contains("https://") ) {
            this._useSSL = true;
        }

        HttpURLConnection.setFollowRedirects(false);

        URL jtUrl = new URL(url);

        if( this._useSSL ) {
            SSLContext sslContext = SSLContext.getInstance("SSL");

            if( EgroupwareHttp._tm == null ) {
				EgroupwareHttp._tm = new EgroupwareHttpsTrustManager();
			}

			sslContext.init(
				null,
				new javax.net.ssl.TrustManager[] { EgroupwareHttp._tm },
				new java.security.SecureRandom()
				);

			HttpsURLConnection.setDefaultSSLSocketFactory(
                sslContext.getSocketFactory());
        }


        this._connection = (HttpURLConnection) jtUrl.openConnection();
		this._connection.setConnectTimeout(this._socketTimeout);

        if( !this._cookie.equals("") ) {
            this._connection.addRequestProperty("Cookie", this._cookie);
        }

		if( this._isAjax ) {
			this._connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		}
    }

    /**
     * _readHeader
     * @throws Exception
     */
    protected void _readHeader() throws Exception {
        if( this._connection != null ) {
            Map<String, List<String>> headers = this._connection.getHeaderFields();
            List<String> values = headers.get("Set-Cookie");

            this._cookie = "";

            if( values != null ) {
                for( String value : values ) {
                    if( !this._cookie.equals("") ) {
                        this._cookie += ";";
                    }

                    this._cookie += value;
                }
            }
        }
    }

    /**
     * _readBuffer
     * @return
     * @throws Exception
     */
    protected String _readBuffer() throws Exception {
        String buff = "";

        try {
            BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(this._connection.getInputStream()));

			String line = bufferedReader.readLine();

            while( line != null ) {
				buff = buff + line + "\r\n";
				line = bufferedReader.readLine();
			}

			bufferedReader.close();

            this._connection.disconnect();
        }
        catch( Exception exp ) {
            throw exp;
        }

        return buff;
    }

    /**
     * sendGET
     *
     * @param url String
     * @return String
     * @throws Exception
     */
    public String sendGET(String url) throws Exception {
        this._initConnection(url);
        this._readHeader();

        return this._readBuffer();
    }

    /**
     * sendPOST
     *
     * @param url
     * @param data
     * @return String
     * @throws Exception
     */
    public String sendPOST(String url, Map<String, String> data) throws Exception {
        this._initConnection(url);

        this._connection.setDoOutput(true);

        String post = "";

        for( String key: data.keySet() ) {
            String value = data.get(key);

            if( !post.equals("") ) {
				post += "&";
			}

            if( this._isUrlEncode ) {
                key		= URLEncoder.encode(key);
				value	= URLEncoder.encode(value);
            }

            post += key + "=" + value;
        }

        OutputStreamWriter wr = new OutputStreamWriter(this._connection.getOutputStream());
        wr.write(post);
        wr.flush();
        wr.close();

        this._readHeader();

        return this._readBuffer();
    }

    public String sendFile(String url, File lfile, Map<String, String> data) throws Exception {
        this._initConnection(url);

        String boundary = Long.toHexString(System.currentTimeMillis());

        this._connection.setDoOutput(true);
        this._connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(this._connection.getOutputStream(), "UTF-8"));

        // TODO
        //http://stackoverflow.com/questions/2469451/upload-files-with-java

        return "";
    }

    /**
     * isSiteNotFound
     *
     * @return boolean
     * @throws Exception
     */
    public boolean isSiteNotFound() throws Exception {
        if( this._connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND ) {
            return true;
        }

        return false;
    }

    /**
	 * getLocation
	 * server send location (move)
	 *
	 * @return String
	 */
    public String getLocation() {
        return this._connection.getHeaderField("Location");
    }

    /**
     * getRequestURI
     * @return String
     */
    public String getRequestURI() {
        return this._connection.getURL().toString();
    }

    /**
     * getCookieParameter
     * @param name
     * @return null|String
     */
    public String getCookieParameter(String name) {
        int _pos = this._cookie.indexOf(name + "=");

        if( _pos > -1) {
            int _end = this._cookie.indexOf(";", _pos);
            int _end2 = this._cookie.length();

            if( (_end == -1) || (_end2 < _end)) {
                _end = _end2;
            }

            return this._cookie.substring( _pos + name.length() +1, _end);
        }

        return null;
    }

    /**
     * setRawCookie
     * @param cookie String
     */
    public void setRawCookie(String cookie) {
        this._cookie = cookie;
    }
}
