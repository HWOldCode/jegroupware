/*
 * jegroupware - Egroupware Java Client
 * this is a java http/https egroupware client, for use please check the example
 *
 * @link https://www.hw-softwareentwicklung.de
 * @author Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @package jegroupware
 * @copyright (c) 2012-15 by Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @license http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)
 * @version 14.2.6
 */
package com.jegroupware.egroupware;

import com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect;
import java.util.HashMap;
import java.util.Map;

/**
 * EgroupwareNotifications
 * @author Stefan Werfling
 */
public class EgroupwareNotifications extends EgroupwareJson  {

    // new EPL Notifications
	public static final String EGW_HTTP_GET_NOTIFICATIONS_ACTION =
		"notifications.notifications_jdesk_ajax.get_notifications";

	// new EPL Notifications
	public static final String EGW_HTTP_GET_NOTIFICATIONS_ACTION_CONFIRM =
		"notifications.notifications_jdesk_ajax.confirm_message";

    /**
     * constructor
     */
    public EgroupwareNotifications() {
        super();

        this._request_url = this._createJsonMenuaction(
            EgroupwareNotifications.EGW_HTTP_GET_NOTIFICATIONS_ACTION);
    }

    /**
     * getPost
     * @return Map<String, String>
     */
    @Override
    public Map<String, String> getPost() {
        Map<String, String> data = new HashMap<>();
        data.put("json_data", "{\"request\":{\"parameters\":[null]}}");
        return data;
    }

    /**
     * setRawContent
     * @param content
     */
    @Override
    public void setRawContent(String content) throws EGroupwareExceptionRedirect {
        super.setRawContent(content);

        if( this._json != null ) {
            //System.out.println(content);
        }
    }
}
