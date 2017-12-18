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
package com.jegroupware.egroupware.apps;

import com.jegroupware.egroupware.EgroupwarRequest;
import com.jegroupware.egroupware.Egroupware;
import com.jegroupware.egroupware.EgroupwareApp;
import com.jegroupware.egroupware.EgroupwareAppEtpl;
import com.jegroupware.egroupware.EgroupwareConst;
import com.jegroupware.egroupware.EgroupwareFile;
import com.jegroupware.egroupware.apps.filemanager.FilemanagerBase;
import com.jegroupware.egroupware.apps.filemanager.FilemanagerDELETE;
import com.jegroupware.egroupware.apps.filemanager.FilemanagerDIR;
import com.jegroupware.egroupware.apps.filemanager.FilemanagerUPLOAD;
import com.jegroupware.egroupware.apps.filemanager.FilemanagerUPLOADMOVE;
import com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * EgroupwareFilemanager
 * @author Stefan Werfling
 */
public class EgroupwareFilemanager extends EgroupwareApp {

    /**
     * Ajax Action
     */
    public static final String EGW_HTTP_GET_AJAX_ACTION = "filemanager_ui::ajax_action";

    /**
     * Upload Action
     */
    public static final String EGW_HTTP_UPLOAD_ACTION = "filemanager.etemplate_widget_file.ajax_upload.etemplate";
	public static final String EGW_HTTP_UPLOAD_ACTION_16_1 = "EGroupware\\Api\\Etemplate\\Widget\\File::ajax_upload";

	public static final String EGW_HTTP_DOWNLOAD = "webdav.php";

	/**
	 * List Action
	 */
	public static final String EGW_HTTP_LIST_ACTION	= "filemanager.etemplate_widget_nextmatch.ajax_get_rows.etemplate";
	public static final String EGW_HTTP_LIST_ACTION_16_1	 = "EGroupware\\Api\\Etemplate\\Widget\\Nextmatch::ajax_get_rows";

	/**
	 * Filemanager index
	 */
	public static final String FILEMANAGER_INDEX = "index.php?menuaction=filemanager.filemanager_ui.index&ajax=true";

	/**
	 * _fmbase
	 */
	protected FilemanagerBase _fmbase = null;

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

		if( this._fmbase != null ) {
			Object fmrequest = this._fmbase.getRequest();

			if( fmrequest instanceof String ) {
				data.put("json_data",  "{\"request\":{\"parameters\":[" +
					this._fmbase.getRequest() + "]}}");
			}
			else if( fmrequest instanceof Map ) {
				data = (Map<String, String>) fmrequest;
			}
		}

        return data;
    }

    /**
     * setRawContent
     * @param content
	 * @throws com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect
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
	 * getUserPath
	 * @return
	 */
	public String getUserPath() {
		return this.getUserPath(null);
	}

	/**
	 * getUserPath
	 * @param egw
	 * @return
	 */
	public String getUserPath(Egroupware egw) {
		if( egw == null ) {
			egw = Egroupware.getLastInstance();
		}

		return "/home/" + egw.getConfig().getUser() + "/";
	}

	/**
	 * dir
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public List<EgroupwareFile> dir(String filepath) throws Exception {
		return this.dir(filepath, null);
	}

	/**
	 * dir
	 * @param filepath
	 * @param egw
	 * @return
	 * @throws Exception
	 */
	public List<EgroupwareFile> dir(String filepath,  Egroupware egw) throws Exception {
		if( egw == null ) {
			egw = Egroupware.getLastInstance();
		}

		this._jsonresponse  = null;
		this._request_type	= EgroupwarRequest.EGW_HTTP_REQUEST_POST;
		this._request_url	= this._createJsonMenuaction(EgroupwareFilemanager.EGW_HTTP_LIST_ACTION);
		this._fmbase		= null;

		if( egw.isLogin() ) {
			EgroupwareAppEtpl tpl = new EgroupwareAppEtpl();

			if( tpl.load(this, EgroupwareFilemanager.FILEMANAGER_INDEX, egw) ) {
				FilemanagerDIR _dir = new FilemanagerDIR();

				_dir.setTplId(tpl.getExecId());
				_dir.setPath(filepath);

				this._fmbase = _dir;

				egw.request(this);

				return _dir.getResponse(this._jsonresponse);
			}
		}

		return null;
	}

	/**
	 * deleteFile
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public Boolean deleteFile(String filepath) throws Exception {
		return this.deleteFile(filepath, null);
	}

    /**
     * deleteFile
     * @param filepath
     * @param egw
     * @return
     * @throws Exception
     */
    public Boolean deleteFile(String filepath, Egroupware egw) throws Exception {
		if( egw == null ) {
			egw = Egroupware.getLastInstance();
		}

		this._jsonresponse  = null;
		this._request_type	= EgroupwarRequest.EGW_HTTP_REQUEST_POST;
        this._request_url = this._createJsonMenuaction(EgroupwareFilemanager.EGW_HTTP_GET_AJAX_ACTION);

        if( egw.isLogin() ) {
			FilemanagerDELETE _delete = new FilemanagerDELETE();

			_delete.setPath(filepath);

			this._fmbase = _delete;

            egw.request(this);

            if( this._jsonresponse != null ) {
                return _delete.getResponse(this._jsonresponse);
            }
        }

        return false;
    }

	/**
	 * downloadFile
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public File downloadFile(String filepath) throws Exception {
		return this.downloadFile(filepath, null);
	}

    /**
     * downloadFile
     * @param filepath
     * @param egw
     * @return
     * @throws Exception
     */
    public File downloadFile(String filepath, Egroupware egw) throws Exception {
		if( egw == null ) {
			egw = Egroupware.getLastInstance();
		}

		if( filepath.charAt(0) != "/".charAt(0) ) {
			filepath = "/" + filepath;
		}

		this._jsonresponse  = null;
		this._request_type	= EgroupwarRequest.EGW_HTTP_REQUEST_FILE_DOWNLOAD;
		this._request_url	= EgroupwareFilemanager.EGW_HTTP_DOWNLOAD + filepath;
		this._request_file	= null;
		this._fmbase		= null;

		egw.request(this);

		if( this._request_file != null ) {
			return this._request_file;
		}

        return null;
    }

	/**
	 * uploadFile
	 * @param sourceFilepath
	 * @param destinationFilepath
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFile(String sourceFilepath, String destinationFilepath) throws Exception {
		return this.uploadFile(sourceFilepath, destinationFilepath, null);
	}

    /**
     * uploadFile
     * @param sourceFilepath
     * @param destinationFilepath
     * @param egw
     * @return
     * @throws Exception
     */
    public boolean uploadFile(String sourceFilepath, String destinationFilepath,
        Egroupware egw) throws Exception
    {
		if( egw == null ) {
			egw = Egroupware.getLastInstance();
		}

		this._request_url = this._createJsonMenuaction(EgroupwareFilemanager.EGW_HTTP_UPLOAD_ACTION);

		if( Egroupware.getVersion() != null ) {
			if( Double.parseDouble(Egroupware.getVersion()) > Double.parseDouble(EgroupwareConst.EGW_VERSION_14_3) ) {
				this._request_url = this._createJsonMenuaction(EgroupwareFilemanager.EGW_HTTP_UPLOAD_ACTION_16_1);
			}
		}

        if( egw.isLogin() ) {
			this._request_file = new File(sourceFilepath);

			if( this._request_file.exists() ) {
				EgroupwareAppEtpl tpl = new EgroupwareAppEtpl();

				if( tpl.load(this, EgroupwareFilemanager.FILEMANAGER_INDEX, egw) ) {
					this._jsonresponse  = null;
					this._request_type = EgroupwarRequest.EGW_HTTP_REQUEST_FILE_UPLOAD;

					FilemanagerUPLOAD _upload = new FilemanagerUPLOAD();

					_upload.setTplId(tpl.getExecId());

					this._fmbase = _upload;

					egw.request(this);

					FilemanagerUPLOADMOVE _uploadmove = (FilemanagerUPLOADMOVE) _upload.getResponse(this._jsonresponse);

					if( _uploadmove != null ) {
						_uploadmove.setPath(destinationFilepath);

						this._jsonresponse = null;
						this._request_type = EgroupwarRequest.EGW_HTTP_REQUEST_POST;
						this._request_url = this._createJsonMenuaction(EgroupwareFilemanager.EGW_HTTP_GET_AJAX_ACTION);

						this._fmbase = _uploadmove;

						egw.request(this);

						if( _uploadmove.getResponse(this._jsonresponse) ) {
							return true;
						}
					}
				}
			}
        }

        return false;
    }
}