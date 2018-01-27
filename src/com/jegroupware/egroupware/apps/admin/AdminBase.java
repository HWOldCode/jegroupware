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
package com.jegroupware.egroupware.apps.admin;

import java.util.LinkedHashMap;

/**
 * AdminBase
 * @author Stefan Werfling
 */
abstract public class AdminBase {

	/**
	 * getRequest
	 * return request string
	 * @return
	 */
	abstract public Object getRequest();

	/**
	 * getResponse
	 * @param jsonresponse
	 * @return
	 */
	abstract public Object getResponse(LinkedHashMap jsonresponse);
}
