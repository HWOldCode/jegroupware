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
package com.jegroupware.egroupware.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * SslUtil
 * @author Stefan Werfling
 */
public class SslUtil {

	/**
	 * ca certs path
	 */
	private static final String CACERTS_PATH = "/lib/security/cacerts";

	/**
	 * ca certs password
	 */
	private static final String CACERTS_PASSWORD = "changeit";

	/**
	 * ca cert file
	 */
	static protected File _cacertfile = null;

	/**
	 * getCaCerts
	 * @return
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 */
	static public File getCaCerts() throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		try {
			File location = new File(System.getProperty("java.io.tmpdir"));

			File[] locationChildren = location.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File directory, String fileName) {
					return fileName.endsWith(".tmp") && fileName.startsWith("cacerts");
				}
			});

			if( locationChildren != null ) {
				Calendar cal = Calendar.getInstance();

				cal.setTimeInMillis(new Date().getTime());
				cal.add(Calendar.DAY_OF_MONTH, -1);

				Timestamp timestamp = new Timestamp(cal.getTime().getTime());

				for (File child : locationChildren) {
					if( child.lastModified() <= timestamp.getTime() ) {
						child.delete();
					}
				}
			}
		}
		catch( Exception exp ) {
			// TODO
		}

		// ---------------------------------------------------------------------

		if( SslUtil._cacertfile == null ) {
			SslUtil._cacertfile = new File(
				System.getProperty("java.io.tmpdir") + "/cacerts" +
				String.valueOf(Math.random()) + ".tmp"); //File.createTempFile("cacerts", ".tmp");

			if( !SslUtil._cacertfile.exists() ) {
				SslUtil._cacertfile.createNewFile();
			}

			final File cacertsFile = new File(System.getProperty("java.home") + CACERTS_PATH);

			if( !cacertsFile.exists() ) {
				throw new FileNotFoundException(cacertsFile.getAbsolutePath());
			}

			FileInputStream cacertsIs	= new FileInputStream(cacertsFile);
			final KeyStore cacerts		= KeyStore.getInstance(KeyStore.getDefaultType());

			cacerts.load(cacertsIs, CACERTS_PASSWORD.toCharArray());
			cacertsIs.close();

			// load certificate from input stream
			final CertificateFactory cf = CertificateFactory.getInstance("X.509");

			// write the updated cacerts keystore
            FileOutputStream cacertsOs = new FileOutputStream(SslUtil._cacertfile);

            cacerts.store(cacertsOs, CACERTS_PASSWORD.toCharArray());
            cacertsOs.close();

			System.setProperty("javax.net.ssl.trustStore", SslUtil._cacertfile.getAbsolutePath());
			System.setProperty("javax.net.ssl.trustStorePassword", CACERTS_PASSWORD);
		}

		return SslUtil._cacertfile;
	}

	/**
	 * ensureSslCertIsInKeystore
	 * Add a certificate to the cacerts keystore if it's not already included
	 * @param alias
	 * @param certInputStream
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static void ensureSslCertIsInKeystore(String alias, InputStream certInputStream)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		// get default cacerts file
        final File cacertsFile = SslUtil.getCaCerts();

        if( !cacertsFile.exists() ) {
            throw new FileNotFoundException(cacertsFile.getAbsolutePath());
        }

		// load cacerts keystore
        FileInputStream cacertsIs	= new FileInputStream(cacertsFile);
        final KeyStore cacerts		= KeyStore.getInstance(KeyStore.getDefaultType());

        cacerts.load(cacertsIs, CACERTS_PASSWORD.toCharArray());
        cacertsIs.close();

		// load certificate from input stream
        final CertificateFactory cf = CertificateFactory.getInstance("X.509");
        final Certificate cert = cf.generateCertificate(certInputStream);

        certInputStream.close();

        // check if cacerts contains the certificate
        if( cacerts.getCertificateAlias(cert) == null ) {
            // cacerts doesn't contain the certificate, add it
            cacerts.setCertificateEntry(alias, cert);

            // write the updated cacerts keystore
            FileOutputStream cacertsOs = new FileOutputStream(cacertsFile);

            cacerts.store(cacertsOs, CACERTS_PASSWORD.toCharArray());
            cacertsOs.close();
        }
	}
}
