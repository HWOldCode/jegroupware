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
package com.jegroupware.egroupware.apps;

import com.jegroupware.egroupware.Egroupware;
import com.jegroupware.egroupware.EgroupwareApp;
import com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * EgroupwareFilemanager
 * 
 * @author Stefan Werfling
 */
public class EgroupwareFilemanager extends EgroupwareApp {
   
    public static final String EGW_HTTP_GET_AJAX_ACTION = "filemanager_ui::ajax_action";
    
    public static final String FILEMANAGER_FILE_ACTION_DELETE = "delete"; 
    
    /**
     * fileaction
     */
    protected String _fileaction = "";
    
    /**
     * filepath
     */
    protected String _filepath = "";
    
    /**
     * json response
     */
    protected LinkedHashMap _jsonresponse = null;
    
    /**
     * EgroupwareFilemanager
     */
    public EgroupwareFilemanager() {
        super("filemanager");
    }
    
    /**
     * getPost
     * @return Map<String, String>
     */
    @Override
    public Map<String, String> getPost() {
        Map<String, String> data = new HashMap<>();
        
        String tpath = "";
        String lchar = String.valueOf(
            this._filepath.charAt((this._filepath.length()-1)));
        
        if( lchar != "/" ) {
            String[] tmp = this._filepath.split("/");
            
            for( int i=0; i<(tmp.length-1); i++ ) {
                if( tmp[i].length() != 0 ) {
                    tpath += "/" + tmp[i];
                }
            }
        }
        
        data.put("json_data", "{\"request\":{\"parameters\":[" 
            + "\"" + this._fileaction + "\", " +
            "[\"" + this._filepath + "\"], " +
            "\"" + tpath + "\"" +
            "]}}");
        
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
            LinkedList respsone = (LinkedList) this._json.get("response");

            if( respsone != null ) {
                for( int i=0; i<respsone.size(); i++ ) {
                    LinkedHashMap rcontent = (LinkedHashMap) respsone.get(i);
                    String type = (String) rcontent.get("type");

                    if( type.compareTo("data") == 0 ) {
                        LinkedHashMap data = (LinkedHashMap) rcontent.get("data");
                        
                        this._jsonresponse = data;
                    }
                }
            }
        }
    }
    
    /**
     * deleteFile
     * 
     * @param filepath
     * @param egw
     * @return
     * @throws Exception 
     */
    public Boolean deleteFile(String filepath, Egroupware egw) throws Exception {
        this._request_url = this._createJsonMenuaction(EgroupwareFilemanager.EGW_HTTP_GET_AJAX_ACTION);
        
        if( egw.isLogin() ) {
            this._fileaction    = EgroupwareFilemanager.FILEMANAGER_FILE_ACTION_DELETE;
            this._filepath      = filepath;
            this._jsonresponse  = null;
            
            egw.request(this);
            
            if( this._jsonresponse != null ) {
                String action = (String) this._jsonresponse.get("action");
                
                if( action.indexOf("delete") != -1 ) {
                    long files   = (long) this._jsonresponse.get("files");
                    long errs    = (long) this._jsonresponse.get("errs");
                    long dirs    = (long) this._jsonresponse.get("dirs");
                    
                    if( (errs == 0) && ((files > 0) || (dirs > 0)) ) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * downloadFile
     * 
     * @param filepath
     * @param egw
     * @return
     * @throws Exception 
     */
    public String downloadFile(String filepath, Egroupware egw) throws Exception {
        
        return null;
    }
}
