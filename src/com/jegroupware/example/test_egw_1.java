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
package com.jegroupware.example;

import com.jegroupware.egroupware.Egroupware;
import com.jegroupware.egroupware.EgroupwareApps;
import com.jegroupware.egroupware.EgroupwareConfig;
import com.jegroupware.egroupware.apps.EgroupwareFilemanager;
import com.jegroupware.egroupware.events.EgroupwareAuthentifiactionEvent;
import com.jegroupware.egroupware.events.EgroupwareEvent;
import com.jegroupware.egroupware.events.EgroupwareEventListener;
import com.jegroupware.egroupware.events.EgroupwareEventRequest;
import com.jegroupware.egroupware.events.EgroupwareLogoutEvent;

/**
 *
 * @author swe
 */
public class test_egw_1 implements EgroupwareEventListener {
    /**
	 * main
	 * @param args String[]
	 */
	public static void main(String[] args) {
        Egroupware egw = Egroupware.getInstance(new EgroupwareConfig(
            "http://mydomain/egroupware/",
            "default",
            "admin",
            "password"
            ));

        // add own listener
        egw.addListener(new test_egw_1());
        
        try {
            //System.out.println(egw.getLoginDomains());
            egw.login();

            if( egw.isLogin() ) {
                //EgroupwareNotifications not = new EgroupwareNotifications();
                //egw.request(not);

                EgroupwareApps apps = new EgroupwareApps();
                egw.request(apps);

                if( apps.existApp("filemanager") ) {
                    EgroupwareFilemanager fm = (EgroupwareFilemanager) apps.getAppObject("filemanager");
                    fm.deleteFile("/home/admin/www.hw-softwareentwicklung.de.har", egw);
                }
                //EgroupwareMainWebDialog test = new EgroupwareMainWebDialog();
                //test.loadUrl();
                //test.show(egw.createBrowserLink("cd=no&menuaction=infolog.infolog_ui.edit&info_id="));
                
                //EgroupwareBrowser.open(egw);
                
                egw.logout();
            }

            while( egw.isLogin() ) {
                Thread.sleep(100);
            }
            
            System.out.println("Session destroy");
            
        }
        catch( Exception e ) {
            System.out.println(e.getMessage());
        }
	}

    @Override
    public void authentificationSucceeded(EgroupwareAuthentifiactionEvent e) {
        System.out.println("Login Succeeded: " + e.getEgroupware().getConfig().getUser());
    }

    @Override
    public void authentificationFailed(EgroupwareAuthentifiactionEvent e) {
        System.out.println("Login Failed: " + e.getException().getMessage());
    }

    @Override
    public void logoutSucceeded(EgroupwareLogoutEvent e) {
        System.out.println("Logout Succeeded");
    }

    @Override
    public void logoutFailed(EgroupwareLogoutEvent e) {
        System.out.println("Logout Failed");
    }

    @Override
    public void requestSucceeded(EgroupwareEventRequest e) {
        
    }

    @Override
    public void requestFailed(EgroupwareEventRequest e) {
        
    }

    @Override
    public void threadAction(EgroupwareEvent e) {
        
    }
}