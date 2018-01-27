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
package com.jegroupware.egroupware.apps;

import com.jegroupware.egroupware.Egroupware;
import com.jegroupware.egroupware.EgroupwareApp;
import com.jegroupware.egroupware.EgroupwareUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EgroupwareAdmin
 * @author Stefan Werfling
 */
public class EgroupwareAdmin extends EgroupwareApp {

    /**
     * EgroupwareAdmin
     */
    public EgroupwareAdmin() {
        super("admin");
    }

	/**
     * getPost
     * @return Map<String, String>
     */
    @Override
    public Map<String, String> getPost() {
		Map<String, String> data = new HashMap<>();

		return data;
	}

	public List<EgroupwareUser> getUserList(Egroupware egw) {
		return null;
	}
}