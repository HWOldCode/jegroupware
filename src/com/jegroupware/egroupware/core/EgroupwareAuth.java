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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.Crypt;

/**
 * EgroupwareAuth
 * @author Stefan Werfling
 */
public class EgroupwareAuth {

	/**
	 * comparePassword
	 * @param cleartext
	 * @param encrypted
	 * @param type_in
	 * @param username
	 * @return
	 */
    static public Boolean comparePassword(String cleartext, String encrypted, String type_in, String username) {
        String type = type_in;
        String saved_enc = encrypted;

        Pattern p = Pattern.compile("^\\{([a-z_5]+)\\}(.+)$");
        Matcher m = p.matcher(encrypted);

        if( m.matches() ) {
            type = m.group(1).toLowerCase();
            encrypted = m.group(2);

            if( "md5".compareTo(type) == 0 ) {
                // TODO
            }
            else if( ("plain".compareTo(type) == 0) || ("crypt".compareTo(type) == 0) ) {
                // nothing to do
            }
            else {
                encrypted = saved_enc;
            }
        }
        else {
            if( encrypted.charAt(0) == "$".charAt(0) ) {
                type = "crypt";
            }
        }

        Boolean ret = false;

        if( "plain".compareTo(type) == 0 ) {
            ret = (cleartext.compareTo(encrypted) == 0);
        }
        else if( "smd5".compareTo(type) == 0 ) {
            // TODO
        }
        else if( "sha".compareTo(type) == 0 ) {
            // TODO
        }
        else if( "ssha".compareTo(type) == 0 ) {
            // TODO
        }
        else if( ("crypt".compareTo(type) == 0) ||
            ("des".compareTo(type) == 0) || ("md5_crypt".compareTo(type) == 0) ||
            ("blowish_crypt".compareTo(type) == 0) || ("blowfish_crypt".compareTo(type) == 0) ||
            ("ext_crypt".compareTo(type) == 0) || ("sha256_crypt".compareTo(type) == 0) ||
            ("sha512_crypt".compareTo(type) == 0) )
        {
            ret = EgroupwareAuth._cryptCompare(cleartext, encrypted, type);
        }
        else if( "md5_hmac".compareTo(type) == 0 ) {

        }
        else if( "md5".compareTo(type) == 0 ) {

        }
        else {
            // md5
        }

        return ret;
    }

    /**
     * _cryptCompare
     * @param form_val
     * @param db_val
     * @param type
     * @return
     */
    static protected Boolean _cryptCompare(String form_val, String db_val, String type) {

        // alternative check for password
        // http://code.google.com/p/jbcrypt/
        if( BCrypt.checkpw(form_val, db_val) ) {
            return true;
        }

        //----------------------------------------------------------------------

        HashMap<String, String[]> params = new HashMap<String, String[]>();
        params.put("crypt", new String[]{"CRYPT_STD_DES", "", "2", ""});
        params.put("ext_crypt", new String[]{"CRYPT_EXT_DES", "_J9..", "4", ""});
        params.put("md5_crypt", new String[]{"CRYPT_MD5", "$1$", "8", "$"});
        params.put("blowfish_crypt", new String[]{"CRYPT_BLOWFISH", "$2a$12$", "22", ""});
        params.put("sha256_crypt", new String[]{"CRYPT_SHA256", "$5$", "16", "$"});
        params.put("sha512_crypt", new String[]{"CRYPT_SHA512", "$6$", "16", "$"});

        String[] list = db_val.split("\\$");

        String first = "";
        String dollar = "";
        String salt = "";
        String salt2 = "";

        if( list.length == 4 ) {
            first = list[0];
            dollar = list[1];
            salt = list[2];
            salt2 = list[3];
        }
        else {
            return false;
        }

        int len = 0;

        for( Map.Entry<String, String[]> entry : params.entrySet()) {
            String paramname = entry.getKey();
            String[] tparams = entry.getValue();

            String prefix = tparams[1];
            String random = tparams[2];

            int irandom = Integer.parseInt(random);

			String postfix = tparams[3];

            String[] tlist = prefix.split("\\$");

            String d = "";

            if( tlist.length > 1 ) {
                d = tlist[1];
            }

            if( (dollar.compareTo(d) == 0) ||
                ( (dollar.length() == 0) && (
                    (first.charAt(0) == prefix.charAt(0)) || (
                        (first.charAt(0) != "_".charAt(0)) &&
                        (prefix.length() == 0) )  )  ) )
            {
                String tmp = prefix + salt + postfix;

                len = ( (prefix.length() != 0) ? prefix.length() + irandom : tmp.length() );

                if( ((type.compareTo("sha256_crypt") == 0) || (type.compareTo("sha512_crypt") == 0)) &&
                    (salt.substring(0, 7).compareTo("rounds=") == 0) )
                {
                    len += salt2.length()+1;
                }

                break;
            }
        }

        salt = db_val.substring(0, len);

        try {
            //Cryp
            String new_hash = Crypt.crypt(form_val, salt);

            if( new_hash.compareTo(db_val) == 0 ) {
                return true;
            }
        }
        catch( Exception ex ) {
			//TODO
        }

        return false;
    }
}