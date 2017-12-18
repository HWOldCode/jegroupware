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
package com.jegroupware.egroupware.core;

import java.util.HashMap;

/**
 * HTMLUtil
 * @author Stefan Werfling
 */
public class HTMLUtil {

	/**
	 * html entities
	 */
	private static HashMap<String,String> _htmlEntities;

    static {
      _htmlEntities = new HashMap<String, String>();
      _htmlEntities.put("&lt;", "<");
      _htmlEntities.put("&gt;", ">");
      _htmlEntities.put("&amp;", "&");
      _htmlEntities.put("&quot;", "\"");
      _htmlEntities.put("&agrave;", "à");
      _htmlEntities.put("&Agrave;", "À");
      _htmlEntities.put("&acirc;", "â");
      _htmlEntities.put("&auml;", "ä");
      _htmlEntities.put("&Auml;", "Ä");
      _htmlEntities.put("&Acirc;", "Â");
      _htmlEntities.put("&aring;", "å");
      _htmlEntities.put("&Aring;", "Å");
      _htmlEntities.put("&aelig;", "æ");
      _htmlEntities.put("&AElig;", "Æ" );
      _htmlEntities.put("&ccedil;", "ç");
      _htmlEntities.put("&Ccedil;", "Ç");
      _htmlEntities.put("&eacute;", "é");
      _htmlEntities.put("&Eacute;", "É" );
      _htmlEntities.put("&egrave;", "è");
      _htmlEntities.put("&Egrave;", "È");
      _htmlEntities.put("&ecirc;", "ê");
      _htmlEntities.put("&Ecirc;", "Ê");
      _htmlEntities.put("&euml;", "ë");
      _htmlEntities.put("&Euml;", "Ë");
      _htmlEntities.put("&iuml;", "ï");
      _htmlEntities.put("&Iuml;", "Ï");
      _htmlEntities.put("&ocirc;", "ô");
      _htmlEntities.put("&Ocirc;", "Ô");
      _htmlEntities.put("&ouml;", "ö");
      _htmlEntities.put("&Ouml;", "Ö");
      _htmlEntities.put("&oslash;", "ø");
	  _htmlEntities.put("&Oslash;", "Ø");
      _htmlEntities.put("&szlig;", "ß");
	  _htmlEntities.put("&ugrave;", "ù");
      _htmlEntities.put("&Ugrave;", "Ù");
	  _htmlEntities.put("&ucirc;", "û");
      _htmlEntities.put("&Ucirc;", "Û");
	  _htmlEntities.put("&uuml;", "ü");
      _htmlEntities.put("&Uuml;", "Ü");
	  _htmlEntities.put("&nbsp;", " ");
      _htmlEntities.put("&copy;", "\u00a9");
      _htmlEntities.put("&reg;", "\u00ae");
      _htmlEntities.put("&euro;", "\u20a0");
    }

	/**
	 * escapeHTML
	 * @param s
	 * @return
	 */
	public static final String escapeHTML(String s){
        StringBuffer sb = new StringBuffer();
        int n = s.length();

        for (int i = 0; i < n; i++) {
           char c = s.charAt(i);

           switch (c) {
              case '<': sb.append("&lt;"); break;
              case '>': sb.append("&gt;"); break;
              case '&': sb.append("&amp;"); break;
              case '"': sb.append("&quot;"); break;
              case 'à': sb.append("&agrave;");break;
              case 'À': sb.append("&Agrave;");break;
              case 'â': sb.append("&acirc;");break;
              case 'Â': sb.append("&Acirc;");break;
              case 'ä': sb.append("&auml;");break;
              case 'Ä': sb.append("&Auml;");break;
              case 'å': sb.append("&aring;");break;
              case 'Å': sb.append("&Aring;");break;
              case 'æ': sb.append("&aelig;");break;
              case 'Æ': sb.append("&AElig;");break;
              case 'ç': sb.append("&ccedil;");break;
              case 'Ç': sb.append("&Ccedil;");break;
              case 'é': sb.append("&eacute;");break;
              case 'É': sb.append("&Eacute;");break;
              case 'è': sb.append("&egrave;");break;
              case 'È': sb.append("&Egrave;");break;
              case 'ê': sb.append("&ecirc;");break;
              case 'Ê': sb.append("&Ecirc;");break;
              case 'ë': sb.append("&euml;");break;
              case 'Ë': sb.append("&Euml;");break;
              case 'ï': sb.append("&iuml;");break;
              case 'Ï': sb.append("&Iuml;");break;
              case 'ô': sb.append("&ocirc;");break;
              case 'Ô': sb.append("&Ocirc;");break;
              case 'ö': sb.append("&ouml;");break;
              case 'Ö': sb.append("&Ouml;");break;
              case 'ø': sb.append("&oslash;");break;
              case 'Ø': sb.append("&Oslash;");break;
              case 'ß': sb.append("&szlig;");break;
              case 'ù': sb.append("&ugrave;");break;
              case 'Ù': sb.append("&Ugrave;");break;
              case 'û': sb.append("&ucirc;");break;
              case 'Û': sb.append("&Ucirc;");break;
              case 'ü': sb.append("&uuml;");break;
              case 'Ü': sb.append("&Uuml;");break;
              case '®': sb.append("&reg;");break;
              case '©': sb.append("&copy;");break;
              case '€': sb.append("&euro;"); break;
              // be carefull with this one (non-breaking whitee space)
              case ' ': sb.append("&nbsp;");break;
              default:  sb.append(c); break;
           }
        }

        return sb.toString();
    }

	/**
	 * unescapeHTML
	 * parses a Translation and replaces unescaped HTML entities
	 * @param source translation to parse
	 * @param start offset to begin searching from
	 * @return Replaced HTML entities if any for the translation
	 */
    public static final String unescapeHTML(String source, int start){
       int i, j;

       i = source.indexOf("&", start);

       if( i > -1 ) {
            j = source.indexOf(";" ,i);

            if( j > i ) {
                String entityToLookFor	= source.substring(i , j + 1);
                String value			= (String)_htmlEntities.get(entityToLookFor);

                if( value != null ) {
                    source = new StringBuffer().append(
						source.substring(0 , i))
						.append(value)
						.append(source.substring(j + 1))
						.toString();

                    return unescapeHTML(source, i + 1); // recursive call
                }
            }
        }

        return source;
    }
}