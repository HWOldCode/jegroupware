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

import edu.stanford.ejalbert.BrowserLauncher;
import java.util.List;

/**
 * EgroupwareBrowser
 * @author Stefan Werfling
 */
public class EgroupwareBrowser {

    /**
     * open
     * @param egw
     * @throws Exception
     */
    static public void open(Egroupware egw) throws Exception {
        EgroupwareBrowser.open(egw, "");
    }

	/**
	 * open
	 * @param egw
	 * @param browser
	 * @throws Exception
	 */
    static public void open(Egroupware egw, String browser) throws Exception {
        EgroupwareBrowser.open(egw, null, "");
    }

	/**
	 * open
	 * @param egw
	 * @param menuaction
	 * @param browser
	 * @throws Exception
	 */
    static public void open(Egroupware egw, String menuaction, String browser) throws Exception {
        try {
            if( egw.isLogin() ) {
                String url = egw.createBrowserLink(menuaction);

                BrowserLauncher launcher = new BrowserLauncher();

                if( browser == "" ) {
                    browser = egw.getConfig().getDefaultBrowser();
                }

                if( browser == "" ) {
                    launcher.openURLinBrowser(url);
                }
                else {
                    launcher.openURLinBrowser(browser, url);
                }
            }
            else {
                throw new Exception("please login in egroupware");
            }
        }
        catch( Exception exp ) {
            throw new Exception("can not open system default browser");
        }
    }

    /**
     * getSupportedBrowsers
     * @return
     * @throws Exception
     */
    static public List getSupportedBrowsers() throws Exception {
        BrowserLauncher launcher = new BrowserLauncher();
        return launcher.getBrowserList();
    }
}
