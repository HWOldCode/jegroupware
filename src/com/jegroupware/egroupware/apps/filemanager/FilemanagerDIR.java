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

import com.jegroupware.egroupware.EgroupwareFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * FilemanagerDIR
 * @author Stefan Werfling
 */
public class FilemanagerDIR extends FilemanagerBase {

	/**
	 * template id for request
	 */
	protected String _tplid = "";

	/**
	 * path on egroupware
	 */
	protected String _path = "";

	/**
	 * setTplId
	 * @param tplid
	 */
	public void setTplId(String tplid) {
		this._tplid = tplid;
	}

	/**
	 * setPath
	 * @param path
	 */
	public void setPath(String path) {
		this._path = path;
	}

	/**
	 * getRequest
	 * return request for server
	 * @return
	 */
	@Override
	public String getRequest() {
		String request = "";

		// 1. add template id
		request += "\"" + this._tplid + "\",";

		// 2. table request
		request +=
			"{"
				+ "\"start\":0,"
				+ "\"num_rows\":0,"
				+ "\"refresh\":false"
			+ "},"
			+ "{"
				+ "\"col_filter\":{},"
				+ "\"filter\":\"\","
				+ "\"filter2\":[],"
				+ "\"search\":\"\","
				+ "\"sort\":{"
					+ "\"id\":\"name\","
					+ "\"asc\":true"
				+ "},"
				+ "\"path\":\"" + this._path + "\","
				+ "\"upload\":{},"
				+ "\"buttons\":{"
					+ "\"upload\":{}"
				+ "}"
			+ "},"
			+ "\"nm\","
			+ "[],"
			+ "0";

		return request;
	}

	/**
	 * getResponse
	 * @param jsonresponse
	 * @return
	 */
	@Override
	public List<EgroupwareFile> getResponse(LinkedHashMap jsonresponse) {
		List<EgroupwareFile> files = new ArrayList<>();

		if( jsonresponse instanceof LinkedHashMap ) {
			LinkedHashMap data = (LinkedHashMap) jsonresponse.get("data");

			if( data != null ) {
				for( Object key : data.keySet() ) {
					LinkedHashMap fdata = (LinkedHashMap) data.get(key);

					files.add(new EgroupwareFile(fdata));
				}
			}
		}

		return files;
	}
}