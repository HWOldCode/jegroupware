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

import java.util.LinkedHashMap;

/**
 * EgroupwareFile
 * @author Stefan Werfling
 */
public class EgroupwareFile {

	/**
	 * path
	 */
	protected String _path;

	/**
	 * size
	 */
	protected Long _size;

	/**
	 * mode
	 */
	protected Long _mode;

	/**
	 * uid
	 */
	protected Long _uid;

	/**
	 * mtime
	 */
	protected String _mtime;

	/**
	 * ctime
	 */
	protected String _ctime;

	/**
	 * mime
	 */
	protected String _mime;

	/**
	 * EgroupwareFile
	 * @param path
	 */
	public EgroupwareFile(String path) {
		this._path = path;
	}

	/**
	 * EgroupwareFile
	 * @param path
	 * @param size
	 */
	public EgroupwareFile(String path, Long size) {
		this._path = path;
		this._size = size;
	}

	/**
	 * EgroupwareFile
	 * @param data
	 */
	public EgroupwareFile(LinkedHashMap data) {
		if( data instanceof LinkedHashMap ) {
			this._path = (String) data.get("path");
			this._size = (Long) data.get("size");
			this._mode = (Long) data.get("mode");
			this._uid = (Long) data.get("uid");
			this._mtime = (String) data.get("mtime");
			this._ctime = (String) data.get("ctime");
			this._mime = (String) data.get("mime");
		}
	}

	/**
	 * getPath
	 * @return
	 */
	public String getPath() {
		return this._path;
	}

	/**
	 * getSize
	 * @return
	 */
	public Long getSize() {
		return this._size;
	}

	/**
	 * getMode
	 * @return
	 */
	public Long getMode() {
		return this._mode;
	}

	/**
	 * getUid
	 * @return
	 */
	public Long getUid() {
		return this._uid;
	}

	/**
	 * getMTime
	 * @return
	 */
	public String getMTime() {
		return this._mtime;
	}

	/**
	 * getCTime
	 * @return
	 */
	public String getCTime() {
		return this._ctime;
	}

	/**
	 * getMime
	 * @return
	 */
	public String getMime() {
		return this._mime;
	}

	/**
	 * isDir
	 * @return
	 */
	public Boolean isDir() {
		return this._mime.equals("");
	}
}