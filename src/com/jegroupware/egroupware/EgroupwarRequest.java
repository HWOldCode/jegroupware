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

import com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect;
import java.util.HashMap;
import java.util.Map;

/**
 * EgroupwarRequest
 * @author Stefan Werfling
 */
public class EgroupwarRequest {

    private static HashMap<String,String> htmlEntities;

    static {
      htmlEntities = new HashMap<String, String>();
      htmlEntities.put("&lt;","<");
      htmlEntities.put("&gt;",">");
      htmlEntities.put("&amp;","&");
      htmlEntities.put("&quot;","\"");
      htmlEntities.put("&agrave;","à");
      htmlEntities.put("&Agrave;","À");
      htmlEntities.put("&acirc;","â");
      htmlEntities.put("&auml;","ä");
      htmlEntities.put("&Auml;","Ä");
      htmlEntities.put("&Acirc;","Â");
      htmlEntities.put("&aring;","å");
      htmlEntities.put("&Aring;","Å");
      htmlEntities.put("&aelig;","æ");
      htmlEntities.put("&AElig;","Æ" );
      htmlEntities.put("&ccedil;","ç");
      htmlEntities.put("&Ccedil;","Ç");
      htmlEntities.put("&eacute;","é");
      htmlEntities.put("&Eacute;","É" );
      htmlEntities.put("&egrave;","è");
      htmlEntities.put("&Egrave;","È");
      htmlEntities.put("&ecirc;","ê");
      htmlEntities.put("&Ecirc;","Ê");
      htmlEntities.put("&euml;","ë");
      htmlEntities.put("&Euml;","Ë");
      htmlEntities.put("&iuml;","ï");
      htmlEntities.put("&Iuml;","Ï");
      htmlEntities.put("&ocirc;","ô");
      htmlEntities.put("&Ocirc;","Ô");
      htmlEntities.put("&ouml;","ö");
      htmlEntities.put("&Ouml;","Ö");
      htmlEntities.put("&oslash;","ø") ; htmlEntities.put("&Oslash;","Ø");
      htmlEntities.put("&szlig;","ß") ; htmlEntities.put("&ugrave;","ù");
      htmlEntities.put("&Ugrave;","Ù"); htmlEntities.put("&ucirc;","û");
      htmlEntities.put("&Ucirc;","Û") ; htmlEntities.put("&uuml;","ü");
      htmlEntities.put("&Uuml;","Ü")  ; htmlEntities.put("&nbsp;"," ");
      htmlEntities.put("&copy;","\u00a9");
      htmlEntities.put("&reg;","\u00ae");
      htmlEntities.put("&euro;","\u20a0");
    }

    // get
    public static final int EGW_HTTP_REQUEST_GET = 0;

    // post
    public static final int EGW_HTTP_REQUEST_POST = 1;

    /**
     * request url
     */
    protected String _request_url = "";

    /**
     * request type
     */
    protected int _request_type = EgroupwarRequest.EGW_HTTP_REQUEST_GET;

    /**
     * content
     */
    private String _content = "";

    /**
     * getRequestUrl
     * @return string
     */
    public String getRequestUrl() {
        return this._request_url;
    }

    /**
     * getRequestType
     * @return int
     */
    public int getRequestType() {
        return this._request_type;
    }

    /**
     * getPost
     * @return Map<String, String>
     */
    public Map<String, String> getPost() {
        return new HashMap<>();
    }

    /**
     * setRawContent
     * @param content
     * @throws com.jegroupware.egroupware.exceptions.EGroupwareExceptionRedirect
     */
    public void setRawContent(String content) throws EGroupwareExceptionRedirect {
        this._content = content;
    }

    /**
     * getContent
     * @return String
     */
    public String getContent() {
        return this._content;
    }

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
       Parses a Translation and replaces unescaped HTML entities
       @param source translation to parse
       @param start offset to begin searching from
       @return Replaced HTML entities if any for the translation
     */
    public static final String unescapeHTML(String source, int start){
       int i, j;

       i = source.indexOf("&", start);

       if( i > -1 ) {
            j = source.indexOf(";" ,i);

            if( j > i ) {
                String entityToLookFor = source.substring(i , j + 1);
                String value = (String)htmlEntities.get(entityToLookFor);

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
