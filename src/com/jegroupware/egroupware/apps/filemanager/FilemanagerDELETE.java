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
package com.jegroupware.egroupware.apps.filemanager;

import java.util.LinkedHashMap;

/**
 * FilemanagerDELETE
 * @author Stefan Werfling
 */
public class FilemanagerDELETE extends FilemanagerBase {

	/**
     * Delete
     */
    public static final String FILEMANAGER_FILE_ACTION_DELETE = "delete";

	/**
	 * path on egroupware
	 */
	protected String _path = "";

	/**
	 * setPath
	 * @param path
	 */
	public void setPath(String path) {
		this._path = path;
	}

	/**
	 * getRequest
	 * @return
	 */
	@Override
	public String getRequest() {
		String tpath = "";
        String lchar = String.valueOf(
            this._path.charAt((this._path.length()-1)));

        if( lchar != "/" ) {
            String[] tmp = this._path.split("/");

            for( int i=0; i<(tmp.length-1); i++ ) {
                if( tmp[i].length() != 0 ) {
                    tpath += "/" + tmp[i];
                }
            }
        }

		String request = "\"" + FilemanagerDELETE.FILEMANAGER_FILE_ACTION_DELETE + "\", " +
            "[\"" + this._path + "\"], " +
            "\"" + tpath + "\"";

		return request;
	}

	/**
	 * getResponse
	 * @param jsonresponse
	 * @return
	 */
	@Override
	public Boolean getResponse(LinkedHashMap jsonresponse) {
		String action = (String) jsonresponse.get("action");

		if( action.indexOf("delete") != -1 ) {
			long files   = (long) jsonresponse.get("files");
			long errs    = (long) jsonresponse.get("errs");
			long dirs    = (long) jsonresponse.get("dirs");

			if( (errs == 0) && ((files > 0) || (dirs > 0)) ) {
				return true;
			}
		}

		return false;
	}
}
