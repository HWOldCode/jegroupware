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
 * FilemanagerUPLOADMOVE
 * @author Stefan Werfling
 */
public class FilemanagerUPLOADMOVE extends FilemanagerBase {

	/**
	 * tmp filename
	 */
	protected String _tmpfilename;

	/**
	 * org filename
	 */
	protected String _orgfilename;

	/**
	 * file type
	 */
	protected String _filetype;

	/**
	 * path to move
	 */
	protected String _path;

	/**
	 * setTmpFilename
	 * @param tmpfilename
	 */
	public void setTmpFilename(String tmpfilename) {
		this._tmpfilename = tmpfilename;
	}

	/**
	 * setOrgFilename
	 * @param orgfilename
	 */
	public void setOrgFilename(String orgfilename) {
		this._orgfilename = orgfilename;
	}

	/**
	 * setFileType
	 * @param filetype
	 */
	public void setFileType(String filetype) {
		this._filetype = filetype;
	}

	/**
	 * setPath
	 * @param path
	 */
	public void setPath(String path) {
		this._path = path;
	}

	@Override
	public Object getRequest() {
		String data = "\"upload\", "
			+ "{"
				+ "\"" + this._tmpfilename + "\": {"
					+ "\"name\": \"" + this._orgfilename + "\","
					+ "\"type\": \"" + this._filetype + "\""
				+ "}"
			+ "},"
			+ "\"" + this._path + "\"";

		return data;
	}

	/**
	 * getResponse
	 * @param jsonresponse
	 * @return
	 */
	@Override
	public Boolean getResponse(LinkedHashMap jsonresponse) {
		String action = (String) jsonresponse.get("action");

		if( action.indexOf("upload") != -1 ) {
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