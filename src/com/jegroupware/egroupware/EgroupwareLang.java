/*
 * jegroupware - Egroupware Java Client
 * this is a java http/https egroupware client, for use please check the example
 *
 * @link https://www.hw-softwareentwicklung.de
 * @author Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @package jegroupware
 * @copyright (c) 2018-18 by Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @license http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)
 * @version 17.1
 */
package com.jegroupware.egroupware;

import com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect;
import java.sql.Timestamp;
import java.util.ArrayList;
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
 * EgroupwareLang
 * @author Stefan Werfling
 */
public class EgroupwareLang extends EgroupwarRequest {

	// lang
    public static final String EGW_HTTP_LANG = "api/lang.php";

	public static final String RETURN_BEGIN = "egw.set_lang_arr(";
	public static final String RETURN_END = ", egw && egw.window !== window);";

	public static final String LANG_DE = "de";
	public static final String LANG_EN = "en";

	/**
     * static list app lang lists
     */
    static protected Map<String,HashMap> _appLangList = new HashMap<>();

	/**
	 * lang
	 */
	protected String _lang = EgroupwareLang.LANG_EN;

	/**
	 * Egroupware object
	 */
	protected Egroupware _egw = null;

	/**
	 * EgroupwareLang
	 * @param egw
	 */
	public EgroupwareLang(Egroupware egw) {
		this._request_type = EgroupwarRequest.EGW_HTTP_REQUEST_GET;
		this._egw = egw;
	}

	/**
     * setRawContent
     * @param content
     * @throws com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect
     */
    @Override
    public void setRawContent(String content) throws EGroupwareExceptionRedirect {
		if( content.indexOf(EgroupwareLang.RETURN_BEGIN) != -1 ) {
			content = content.substring(
				EgroupwareLang.RETURN_BEGIN.length(),
				content.length()-EgroupwareLang.RETURN_END.length()-2);

			content = "[" + content + "]";

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
				LinkedList response = (LinkedList)parser.parse(content.trim(), containerFactory);

				if( response.size() == 2 ) {
					String appname = (String) response.get(0);
					HashMap<String, String> langs = (HashMap) response.get(1);

					EgroupwareLang._appLangList.put(appname, langs);
				}
			}
			catch( ParseException ex ) {
				Logger.getLogger(
					EgroupwareJson.class.getName()).log(
						Level.SEVERE,
						null,
						ex);
			}
		}
	}

	/**
	 * lang
	 * @param appname
	 * @param str
	 * @param lang
	 * @return
	 */
	public String lang(String appname, String str, String lang) {
		if( !EgroupwareLang._appLangList.containsKey(appname) ) {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			this._request_url = EgroupwareLang.EGW_HTTP_LANG +
				"?app=" + appname + "&lang=" + lang + "&etag=" + Long.toString(timestamp.getTime());

			try {
				this._egw.request(this);
			}
			catch( Exception e ) {
				// none
			}
		}

		Map<String, String> _appLangs =  EgroupwareLang._appLangList.get(appname);

		if( _appLangs.containsKey(str) ) {
			return _appLangs.get(str);
		}

		return str;
	}
}