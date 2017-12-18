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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * FilemanagerUPLOAD
 * @author Stefan Werfling
 */
public class FilemanagerUPLOAD extends FilemanagerBase {

	/**
	 * tplid
	 */
	protected String _tplid;

	/**
	 * getRequest
	 * @return
	 */
	@Override
	public Object getRequest() {
		Map<String, String> data = new HashMap<>();

		data.put("request_id", this._tplid);
		data.put("widget_id", "upload");

		return data;
	}

	/**
	 * setTplId
	 * @param tplid
	 */
	public void setTplId(String tplid) {
		this._tplid = tplid;
	}

	/**
	 * getResponse
	 * @param jsonresponse
	 * @return
	 */
	@Override
	public Object getResponse(LinkedHashMap jsonresponse) {
		if( jsonresponse != null ) {
			if( jsonresponse.size() == 1 ) {
				ArrayList<String> tmpkeys = new ArrayList<String>(jsonresponse.keySet());

				String tmpfilename = tmpkeys.get(0);

				LinkedHashMap fileinfo = (LinkedHashMap) jsonresponse.get(tmpfilename);

				String orgfilename = (String) fileinfo.get("name");
				String filetype = (String) fileinfo.get("type");

				FilemanagerUPLOADMOVE _uploadmove = new FilemanagerUPLOADMOVE();

				_uploadmove.setTmpFilename(tmpfilename);
				_uploadmove.setOrgFilename(orgfilename);
				_uploadmove.setFileType(filetype);

				return _uploadmove;
			}
		}

		return null;
	}
}
