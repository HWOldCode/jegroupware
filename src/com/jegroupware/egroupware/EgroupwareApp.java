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

import java.util.ArrayList;

/**
 * EgroupwareApp
 * @author Stefan Werfling
 */
public class EgroupwareApp extends EgroupwareJson {

    /**
     * Apps Register
     */
    static private ArrayList<String> _apps_register = new ArrayList<String>();

	/**
	 * App name
	 */
	protected String _name = "";

	/**
	 * App Title
	 */
	protected String _title = "";

	/**
	 * App template exec id
	 */
	protected String template_exec_id = "";

	/**
	 * EgroupwareApp
	 * @param name
	 */
	public EgroupwareApp(String name, String title) {
		this._name = name;
        this._title = title;
	}

    /**
	 * EgroupwareApp
	 * @param name
	 */
	public EgroupwareApp(String name) {
		this._name = name;
	}

	/**
	 * getName
	 * @return
	 */
	public String getName() {
		return this._name;
	}

    /**
     * setTitle
     * @param title
     */
    public void setTitle(String title) {
        this._title = title;
    }

    /**
     * getTitle
     * @return
     */
    public String getTitle() {
        return this._title;
    }

    /**
     * castApp
     * @return
     * @throws ClassNotFoundException
     */
    public Object castApp() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String name = this.getName();

        name = name.toLowerCase();
        name = Character.toString(name.charAt(0)).toUpperCase()+name.substring(1);

        String classFor = EgroupwareApp.class.getPackage().toString() +
                ".apps.Egroupware" + name;

        classFor = classFor.replaceFirst("package ", "");

        Class t = Class.forName(
            classFor,
            false,
            this.getClass().getClassLoader()
            );

        if( t != null ) {
            return t.newInstance();
        }

        return null;
    }

    /**
     * addAppsRegister
     * @param appclass
     */
    static public void addAppsRegister(String appclass) {
        _apps_register.add(appclass);
    }
}