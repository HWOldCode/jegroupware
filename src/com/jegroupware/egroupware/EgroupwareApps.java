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

import com.jegroupware.egroupware.core.HTMLUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * EgroupwareApps
 * @author Stefan Werfling
 */
public class EgroupwareApps extends EgroupwarRequest {

    // data-navbar-apps
	public static final String EGW_HTTP_DATA_NAVBAR_APPS = "data-navbar-apps";

	// powered by egroupware
	public static final String EGW_HTTP_DATA_POWER_BY = "Powered by EGroupware";

    // apps
    protected Map<String, Object> _apps = new HashMap<>();

    /**
     * EgroupwareApps
     */
    public EgroupwareApps() {
        this._request_type = EgroupwarRequest.EGW_HTTP_REQUEST_GET;
        this._request_url = Egroupware.EGW_HTTP_INDEXSCRIPT + "?cd=yes";
    }

    /**
     * setRawContent
     * @param content
     */
	@Override
    public void setRawContent(String content) {
		// apps ----------------------------------------------------------------
        int begin = content.indexOf(EGW_HTTP_DATA_NAVBAR_APPS + "=\"");

        if( begin > -1 ) {
            int end = content.indexOf("\"",
                begin + EGW_HTTP_DATA_NAVBAR_APPS.length() + 3);

            if( (begin != -1) && (end != -1) ) {
                String json = content.substring(
                    begin + EGW_HTTP_DATA_NAVBAR_APPS.length() + 2,
                    end);

                try {
                    JSONParser parser = new JSONParser();
                    ContainerFactory containerFactory = new ContainerFactory(){
						@Override
                        public List creatArrayContainer() {
                            return new LinkedList();
                        }

						@Override
                        public Map createObjectContainer() {
                            return new LinkedHashMap();
                        }
                    };

                    json = HTMLUtil.unescapeHTML(json.trim(), 0);

                    LinkedList _json = (LinkedList)parser.parse(json
                            , containerFactory);

                    for( int i=0; i<_json.size(); i++ ) {
                        LinkedHashMap _element = (LinkedHashMap) _json.get(i);

                        String name = (String) _element.get("name");
                        String title = (String) _element.get("title");

                        EgroupwareApp app = new EgroupwareApp(name);

                        try {
                            Object t = app.castApp();

                            if( t != null ) {
                                ((EgroupwareApp)t).setTitle(title);

                                this._apps.put(name, t);
                            }
                            else {
                                this._apps.put(name, app);
                            }
                        }
						catch( ClassNotFoundException ex ) {
                            //Logger.getLogger(EgroupwareApps.class.getName()).log(Level.SEVERE, null, ex);
                        }
						catch( Exception ex ) {
                            Logger.getLogger(EgroupwareApps.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
				catch( ParseException ex ) {
                    Logger.getLogger(EgroupwareApps.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

		// version -------------------------------------------------------------
		int vbegin = content.indexOf(EGW_HTTP_DATA_POWER_BY);

		if( vbegin > -1 ) {
			int vend = content.indexOf("</a>",
                vbegin + EGW_HTTP_DATA_POWER_BY.length());

			if( (vbegin != -1) && (vend != -1) ) {
				Egroupware.setVersion(content.substring(
                    vbegin + EGW_HTTP_DATA_POWER_BY.length(),
                    vend).trim());
			}
		}
    }

    /**
     * getAppObject
     * @param appname
     * @return
     */
    public EgroupwareApp getAppObject(String appname) {
        EgroupwareApp tapp = (EgroupwareApp) this._apps.get(appname);

        return tapp;
    }

    /**
     * existApp
     * @param appname
     * @return
     */
    public Boolean existApp(String appname) {
        return this._apps.containsKey(appname);
    }
}