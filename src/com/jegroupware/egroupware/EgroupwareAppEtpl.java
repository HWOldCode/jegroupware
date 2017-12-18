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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * EgroupwareAppEtpl
 * EGroupware App ETemplate
 * @author Stefan Werfling
 */
public class EgroupwareAppEtpl extends EgroupwareJson {

	/**
	 * http menuaction
	 */
	public static final String EGW_HTTP_MENUACTION = "jdots_framework.ajax_exec.template";

	/**
	 * keys
	 */
	public static final String KEY_NAME			= "name";
	public static final String KEY_URL			= "url";
	public static final String KEY_DOM_NODE_ID	= "DOMNodeID";
	public static final String KEY_DATA			= "data";
	public static final String KEY_EXEC_ID		= "etemplate_exec_id";

	/**
	 * load url for request
	 */
	protected String _loadUrl = "";

	/**
	 * template name
	 */
	protected String _tpl_name = "";

	/**
	 * template url
	 */
	protected String _tpl_url = "";

	/**
	 * template dom node id
	 */
	protected String _tpl_dom_node_id = "";

	/**
	 * template exec id
	 */
	protected String _tpl_exec_id = "";

	/**
     * getPost
     * @return Map<String, String>
     */
    @Override
    public Map<String, String> getPost() {
        Map<String, String> data = new HashMap<>();

        data.put("json_data", "{\"request\":{\"parameters\":[" +
			"\"" + this._loadUrl + "\"" +
			"]}}");

        return data;
    }

	/**
	 * load
	 * @param app
	 * @param url
	 * @param egw
	 * @return
	 * @throws Exception
	 */
	public boolean load(EgroupwareApp app, String url, Egroupware egw) throws Exception {
		this._request_url = this._createJsonMenuaction(
			app.getName() + "." + EgroupwareAppEtpl.EGW_HTTP_MENUACTION);

		if( egw.isLogin() ) {
			this._loadUrl = url;

			egw.request(this);

			if( this._json instanceof LinkedHashMap ) {
				LinkedList respsone = (LinkedList) this._json.get("response");

                if( respsone instanceof LinkedList ) {
                    for( int i=0; i<respsone.size(); i++ ) {
                        LinkedHashMap rcontent = (LinkedHashMap) respsone.get(i);

						if( rcontent.containsKey("type") ) {
							String type = (String) rcontent.get("type");

							if( type.equals(EgroupwareJson.TYPE_ET2_LOAD) ) {
								Object _data = rcontent.get("data");

								if( _data instanceof LinkedHashMap ) {
									LinkedHashMap data = (LinkedHashMap) _data;

									if( data.containsKey(EgroupwareAppEtpl.KEY_NAME) ) {
										this._tpl_name = (String) data.get(EgroupwareAppEtpl.KEY_NAME);
									}

									if( data.containsKey(EgroupwareAppEtpl.KEY_URL) ) {
										this._tpl_url = (String) data.get(EgroupwareAppEtpl.KEY_URL);
									}

									if( data.containsKey(EgroupwareAppEtpl.KEY_DOM_NODE_ID) ) {
										this._tpl_dom_node_id = (String) data.get(EgroupwareAppEtpl.KEY_DOM_NODE_ID);
									}

									if( data.containsKey(EgroupwareAppEtpl.KEY_DATA) ) {
										Object _innerData = data.get(EgroupwareAppEtpl.KEY_DATA);

										if( _innerData instanceof LinkedHashMap ) {
											LinkedHashMap innerData = (LinkedHashMap) _innerData;

											if( innerData.containsKey(EgroupwareAppEtpl.KEY_EXEC_ID) ) {
												this._tpl_exec_id = (String) innerData.get(EgroupwareAppEtpl.KEY_EXEC_ID);
											}
										}
									}

									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * getExecId
	 * @return
	 */
	public String getExecId() {
		return this._tpl_exec_id;
	}

	/**
	 * getName
	 * @return
	 */
	public String getName() {
		return this._tpl_name;
	}

	/**
	 * getUrl
	 * @return
	 */
	public String getUrl() {
		return this._tpl_url;
	}

	/**
	 * getDomNodeId
	 * @return
	 */
	public String getDomNodeId() {
		return this._tpl_dom_node_id;
	}
}