/*
 * jegroupware - Egroupware Java Client
 * this is a java http/https egroupware client, for use please check the example
 *
 * @link https://www.hw-softwareentwicklung.de
 * @author Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @package jegroupware
 * @copyright (c) 2012-17 by Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @license http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)
 * @version 17.1
 */
package com.jegroupware.egroupware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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

    /**
	 * https trust manager, handle certificates
	 */
    static private EgroupwareHttpsTrustManager _tm = null;

	/**
	 * user agents im http
	 */
	static private List<String> _userAgents = new ArrayList<String>();

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
    private boolean _isAjax = false;

    /**
     * user URL Encode by POST, see (http://en.wikipedia.org/wiki/POST_(HTTP))
     * default = true
     */
    private boolean _isUrlEncode = true;

	/**
	 * addUserAgent
	 * @param useragent
	 */
	static public void addUserAgent(String useragent) {
		EgroupwareHttp._userAgents.add(useragent);
	}

    /**
     * setSocketTimeout
     * @param timeout
     */
    public void setSocketTimeout(int timeout) {
        this._socketTimeout = timeout;
    }

    /**
     * _initConnection
     * @param url
     * @throws Exception
     */
    protected void _initConnection(String url) throws Exception {
        //System.setProperty("sun.net.client.defaultConnectTimeout", Integer.toString(this._socketTimeout));
        //System.setProperty("sun.net.client.defaultReadTimeout", Integer.toString(this._socketTimeout));

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

		// set cookie
		// ---------------------------------------------------------------------
        if( !this._cookie.equals("") ) {
            this._connection.addRequestProperty("Cookie", this._cookie);
        }

		// set ajax
		// ---------------------------------------------------------------------
        if( this._isAjax ) {
            this._connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
        }

		// set user agent info
		// ---------------------------------------------------------------------
		String userAgent = "";

		for( String tagent: EgroupwareHttp._userAgents ) {
			userAgent = userAgent + "/" + tagent;
		}

		this._connection.setRequestProperty("User-Agent", userAgent);

        //this._connection.addRequestProperty("Keep-Alive", "300");
        //this._connection.addRequestProperty("Connection", "keep-alive");
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
     * @param url
     * @param data
     * @return String
     * @throws Exception
     */
    public String sendPOST(String url, Map<String, String> data) throws Exception {
        this._initConnection(url);

        this._connection.setDoOutput(true);

        OutputStreamWriter wr = new OutputStreamWriter(
            this._connection.getOutputStream());

        String post = "";

        for( String key: data.keySet() ) {
            String value = data.get(key);

            if( !post.equals("") ) {
                post += "&";
            }

            if( this._isUrlEncode ) {
                key = URLEncoder.encode(key);
                value = URLEncoder.encode(value);
            }

            post += key + "=" + value;
        }

        wr.write(post);
        wr.flush();
        wr.close();

        this._readHeader();

        return this._readBuffer();
    }

    /**
     * sendFile
     * @param url
     * @param lfile
     * @param data
     * @return
     * @throws Exception
     */
    public List<String> sendFile(String url, File lfile, Map<String, String> data) throws Exception {
		EgroupwareHttpResumable resumble = new EgroupwareHttpResumable(lfile);
		List<String> responses = new ArrayList<>();
		FileInputStream ainputStream = new FileInputStream(lfile);

		String cacheCookie = this._cookie;	// get cookie
		int fileAvailable = ainputStream.available();

		while( fileAvailable > 0 ) {
			this._cookie = cacheCookie;	// set cookie
			this._initConnection(url);

			resumble.nextChunk();

			int chunklen = (int) resumble.getChunkSize();

			if( resumble.isLastChunk() ) {
				chunklen = fileAvailable;

				resumble.setCurrentChunkSize(chunklen);
			}

			String boundary = Long.toHexString(System.currentTimeMillis());

			Map<String, String> tdata = new HashMap<>();

			tdata.put("resumableChunkNumber", Long.toString(resumble.getChunkNumber()));
			tdata.put("resumableCurrentChunkSize", Long.toString(resumble.getCurrentChunkSize()));
			tdata.put("resumableChunkSize", Long.toString(resumble.getChunkSize()));
			tdata.put("resumableType", resumble.getType());
			tdata.put("resumableTotalSize", Long.toString(resumble.getTotalSize()));
			tdata.put("resumableIdentifier", resumble.getIdentifier());
			tdata.put("resumableFilename", resumble.getFilename());
			tdata.put("resumableRelativePath", resumble.getRelativePath());
			tdata.put("resumableTotalChunks", Long.toString(resumble.getTotalChunks()));

			tdata.putAll(data);

			this._connection.setDoOutput(true);
			this._connection.setRequestMethod("POST");
			this._connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			this._connection.setDoOutput(true);

			OutputStream outputStreamToRequestBody = this._connection.getOutputStream();

			BufferedWriter httpRequestBodyWriter =
				new BufferedWriter(new OutputStreamWriter(outputStreamToRequestBody));

			for( Object tkey : tdata.keySet() ) {
				String tvalue = (String) tdata.get(tkey);

				httpRequestBodyWriter.write("\n--" + boundary + "\n");
				httpRequestBodyWriter.write("Content-Disposition: form-data; name=\"" + tkey + "\"");
				httpRequestBodyWriter.write("\n\n");
				httpRequestBodyWriter.write(tvalue);
			}

			httpRequestBodyWriter.write("\n--" + boundary + "\n");
			httpRequestBodyWriter.write("Content-Disposition: form-data;"
				+ " name=\"file\"; filename=\"blob\""
				+ "\nContent-Type: application/octet-stream\n\n");
			httpRequestBodyWriter.flush();

			int bytesRead;
			byte[] dataBuffer = new byte[chunklen];

			if( (bytesRead = ainputStream.read(dataBuffer, 0, chunklen)) != -1 ) {
				outputStreamToRequestBody.write(dataBuffer, 0, bytesRead);
			}
			else {
				throw new Exception();
			}

			outputStreamToRequestBody.flush();

			httpRequestBodyWriter.write("\n--" + boundary + "--\n");
			httpRequestBodyWriter.flush();

			outputStreamToRequestBody.close();
			httpRequestBodyWriter.close();

			this._readHeader();

			String returnBuffer = this._readBuffer();

			if( !returnBuffer.isEmpty() ) {
				responses.add(returnBuffer);
			}

			fileAvailable = ainputStream.available();
		}

        return responses;
    }

	/**
	 * downloadFile
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public File downloadFile(String url) throws IOException, Exception {
		File tempFile = File.createTempFile("jegroupware-", ".dwfile");
		tempFile.deleteOnExit();

		this._initConnection(url);

        this._connection.setDoOutput(true);

		int responseCode = this._connection.getResponseCode();

		if( responseCode == HttpURLConnection.HTTP_OK ) {
			String fileName = "";
			String disposition = this._connection.getHeaderField("Content-Disposition");
            String contentType = this._connection.getContentType();
            int contentLength = this._connection.getContentLength();

			if( disposition != null ) {
				int index = disposition.indexOf("filename=");

				if( index > 0 ) {
                    fileName = disposition.substring(index + 10, disposition.length() - 1);
                }
			}
			else {
				fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
			}


			InputStream inputStream = this._connection.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(tempFile);

			int bytesRead = -1;
            byte[] buffer = new byte[1048576];

            while( (bytesRead = inputStream.read(buffer)) != -1 ) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

			this._connection.disconnect();

			FileInputStream ainputStream = new FileInputStream(tempFile);
			int fileAvailableLen = ainputStream.available();
			ainputStream.close();

			if( fileAvailableLen != contentLength ) {
				throw new Exception("Filesize uncompare: " + Integer.toString(contentLength));
			}
		}

		return tempFile;
	}

    /**
     * isSiteNotFound
     * @return boolean
     * @throws Exception
     */
    public boolean isSiteNotFound() throws Exception {
        return (this._connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND);
    }

    /**
	 * getLocation
	 * server send location (move)
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

    /**
     * getRawCookie
     * @return
     */
    public String getRawCookie() {
        return this._cookie;
    }

    /**
     * getNewHttpClone
     *
     * @return
     */
    public EgroupwareHttp getNewHttpClone() {
        EgroupwareHttp http = new EgroupwareHttp();

        http.setRawCookie(this.getRawCookie());
        http.setSocketTimeout(this._socketTimeout);

        return http;
    }
}