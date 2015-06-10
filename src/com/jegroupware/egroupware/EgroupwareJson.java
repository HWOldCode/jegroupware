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
 * EgroupwareJson
 * @author Stefan Werfling
 */
public class EgroupwareJson extends EgroupwarRequest {

    // json
    public static final String EGW_HTTP_JSON = "json.php";

    /**
     * json
     */
    protected Map _json = null;

    /**
     * EgroupwareJson
     */
    public EgroupwareJson() {
        this._request_type = EgroupwarRequest.EGW_HTTP_REQUEST_POST;
    }

    /**
     * _createJsonMenuaction
     * @param action
     * @return String
     */
    protected String _createJsonMenuaction(String action) {
        return EgroupwareJson.EGW_HTTP_JSON + "?menuaction=" + action;
    }

    /**
     * setRawContent
     * @param content
     * @throws com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect
     */
    @Override
    public void setRawContent(String content) throws EGroupwareExceptionRedirect {
        super.setRawContent(content);
        
        // is json request
        if( this._request_url.indexOf(EgroupwareJson.EGW_HTTP_JSON) == -1 ) {
            return;
        }
        
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory(){
                public List creatArrayContainer() {
                    return new LinkedList();
                }

                public Map createObjectContainer() {
                    return new LinkedHashMap();
                }
            };

        try {
            this._json = (Map)parser.parse(content.trim(), containerFactory);

            if( this._json != null ) {
                LinkedList respsone = (LinkedList) this._json.get("response");

                if( respsone != null ) {
                    for( int i=0; i<respsone.size(); i++ ) {
                        LinkedHashMap rcontent = (LinkedHashMap) respsone.get(i);

                        String type = (String) rcontent.get("type");
                        LinkedHashMap data = (LinkedHashMap) rcontent.get("data");

                        if( type.compareTo("redirect") == 0 ) {
                            throw new EGroupwareExceptionRedirect((String) data.get("url"));
                        }

                        System.out.println(i);
                    }
                }
            }
        } catch( ParseException ex ) {
            Logger.getLogger(
                EgroupwareJson.class.getName()).log(
                    Level.SEVERE,
                    null,
                    ex);
        }
    }
}
