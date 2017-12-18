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

import com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * EgroupwarRequest
 * @author Stefan Werfling
 */
public class EgroupwarRequest {

    // get
    public static final int EGW_HTTP_REQUEST_GET = 0;

    // post
    public static final int EGW_HTTP_REQUEST_POST = 1;

	// file upload
	public static final int EGW_HTTP_REQUEST_FILE_UPLOAD = 2;

	// file download
	public static final int EGW_HTTP_REQUEST_FILE_DOWNLOAD = 3;

    /**
     * request url
     */
    protected String _request_url = "";

    /**
     * request type
     */
    protected int _request_type = EgroupwarRequest.EGW_HTTP_REQUEST_GET;

	/**
	 * request file
	 */
	protected File _request_file = null;

    /**
     * content
     */
    private String _content = "";

    /**
     * getRequestUrl
     * @return string
     */
    public String getRequestUrl() {
        return this._request_url;
    }

    /**
     * getRequestType
     * @return int
     */
    public int getRequestType() {
        return this._request_type;
    }

	/**
	 * getRequestFile
	 * @return
	 */
	public File getRequestFile() {
		return this._request_file;
	}

	/**
	 * setRequestFile
	 * @param afile
	 */
	public void setRequestFile(File afile) {
		this._request_file = afile;
	}

    /**
     * getPost
     * @return Map<String, String>
     */
    public Map<String, String> getPost() {
        return new HashMap<>();
    }

    /**
     * setRawContent
     * @param content
     * @throws com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect
     */
    public void setRawContent(String content) throws EGroupwareExceptionRedirect {
        this._content = content;
    }

    /**
     * getContent
     * @return String
     */
    public String getContent() {
        return this._content;
    }
}