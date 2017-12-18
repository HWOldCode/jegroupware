/*
 * jegroupware - Egroupware Java Client
 * this is a java http/https egroupware client, for use please check the example
 *
 * @link https://www.hw-softwareentwicklung.de
 * @author Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @package jegroupware
 * @copyright (c) 2012-16 by Stefan Werfling <stefan.werfling-AT-hw-softwareentwicklung.de>
 * @license http://opensource.org/licenses/GPL-2.0 GPL2 - GNU General Public License, version 2 (GPL-2.0)
 * @version 14.2.6
 */
package com.jegroupware.egroupware;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.util.Base64;
import java.util.Base64.Encoder;

/**
 * EgroupwareHttpsTrustManager
 * @author Stefan Werfling
 */
public class EgroupwareHttpsTrustManager implements javax.net.ssl.X509TrustManager {

    /**
     * accept unsafe
     */
    protected boolean _acceptUnsafe = false;

    /**
     * accept Certs
     */
    private ArrayList<X509Certificate> acceptCerts = new ArrayList<X509Certificate>();

    /**
     * setAcceptUnsafe
     * @param accept boolean
     */
    public void setAcceptUnsafe(boolean accept) {
        this._acceptUnsafe = accept;
    }

    /**
     * checkClientTrusted
     * @param xcs
     * @param string
     * @throws CertificateException
     */
    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    /**
     * checkServerTrusted
     * @param xcs
     * @param string
     * @throws CertificateException
     */
    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        for( int i=0; i<xcs.length; i++ ) {
            X509Certificate cs = xcs[i];

            if( this.acceptCerts.indexOf(cs) != -1 ) {
                return;
            }

            try {
                cs.checkValidity();

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());

                tmf.init((KeyStore)null);
                TrustManager[] tms = tmf.getTrustManagers();

                if( tms.length > 0 ) {
                    X509TrustManager x509TrustManager = (X509TrustManager) tms[0];
                    x509TrustManager.checkServerTrusted(xcs, string);
                }
            }
            catch( Exception exp ) {
                if( this._acceptUnsafe ) {
                    this.acceptCerts.add(cs);
                }
                else {
                    throw new CertificateException(exp.getMessage());
                }
            }
        }
    }

    /**
     * getAcceptedIssuers
     * @return X509Certificate[]
     */
    public X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[] {};
    }

    /**
     * getAcceptedCerts
     * return all accepted Certs
     * @return String Certs in PEM
     * @throws CertificateEncodingException
     */
    public String getAcceptedCerts() throws CertificateEncodingException {
        String certs = "";

        for( X509Certificate cert : this.acceptCerts ) {
			Encoder encoder = Base64.getEncoder();
            certs += "-----BEGIN CERTIFICATE-----";
            certs += encoder.encode(cert.getEncoded());
            certs += "-----END CERTIFICATE-----";
            certs += "\r\n\r\n";
        }

        return certs;
    }

    /**
     * setAcceptedCerts
     * @param strcerts
     * @throws CertificateException
     */
    public void setAcceptedCerts(String strcerts) throws CertificateException {
        String[] tmp = strcerts.split("\r\n\r\n");

        for( String tmp1 : tmp ) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = new ByteArrayInputStream(tmp1.getBytes());
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);

            this.acceptCerts.add(cert);
        }
    }
}
