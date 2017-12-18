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
package com.jegroupware.egroupware.exceptions;

/**
 * EGroupwareExceptionUserConfig
 *
 * @author Stefan Werfling
 */
public class EGroupwareExceptionUserConfig extends Exception {

	/**
	 * fieldname
	 */
	protected String _fieldName = "";

	/**
	 * constructor
	 * @param fieldName
	 */
	public EGroupwareExceptionUserConfig(String fieldName) {
		this._fieldName = fieldName;
	}

	/**
	 * getFieldName
	 *
	 * @return
	 */
	public String getFieldName() {
		return this._fieldName;
	}
}