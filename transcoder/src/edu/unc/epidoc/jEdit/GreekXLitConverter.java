/*
 * GreekXLitConverter.java
 *
 * (c) Michael Jones <mdjone2@uky.edu>
 * This software is licensed under the terms of the GNU LGPL.
 * See http://www.gnu.org/licenses/lgpl.html for details.
 */

import java.io.*;
import java.lang.*;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 *
 * @author  Michael Jones
 * @version
 */
public class GreekXLitConverter implements Converter {

    /** Creates new GreekXLitConverter */
    public GreekXLitConverter() {
        sgp = new Properties();
        try {
            Class c = this.getClass();
            sgp.load(c.getResourceAsStream("GreekXLitConverter.properties"));
        }
        catch (Exception e) {
        }
    }

    private Properties sgp;
    StringBuffer strb = new StringBuffer();
    private static final String ENCODING = "ASCII";
    private static final String LANGUAGE = "grc";

    public String convertToCharacterEntity(String in) {
        String out;
        if (in.indexOf('_')>0 && in.length()>1) {
            strb.delete(0,strb.length());
            String[] elements = split(in);
            String temp = sgp.getProperty(elements[0]);
            for (int i=0;i<elements.length;i++)
               strb.append(sgp.getProperty(elements[i]));
            out = strb.toString();
        }
        else
            out = sgp.getProperty(in, in);
        strb.delete(0,strb.length());
        char[] chars = out.toCharArray();
        for (int i=0;i<chars.length;i++) {
            if (Character.getNumericValue(chars[i]) > 126) {
                strb.append("&#");
                strb.append(Character.getNumericValue(chars[i]));
                strb.append(";");
            }
            else {
                strb.append(chars[i]);
            }
        }
        return strb.toString();
    }

    public String convertToString(String in) {
        if (in.indexOf('_')>0 && in.length()>1) {
	    String[] elements = split(in);
	    strb.delete(0,strb.length());
	    if (elements[1].equals("asper")) {
		if (Character.isUpperCase(elements[0].charAt(0))) {
		    strb.append("H");
		    strb.append((sgp.getProperty(elements[0])).toLowerCase());
		}
		else {
		    strb.append("h");
		    strb.append(sgp.getProperty(elements[0]));
		}
	    }
	    else
		strb.append(sgp.getProperty(elements[0]));
	    return strb.toString();
	}
        else
            return sgp.getProperty(in, in);
    }

    private String[] split(String str) {
        StringTokenizer st = new StringTokenizer(str, "_");
        int tokenCount = st.countTokens();
        String[] result = new String[tokenCount];
        for (int i = 0; i < tokenCount; i++) {
            result[i] = st.nextToken();
        }
        return result;
    }

    public Object getProperty(String name) {
        return null;
    }

    public void setProperty(String name, Object value) {
    }

    public String getEncoding() {
        return new String(ENCODING);
    }

    public boolean supportsLanguage(String lang) {
        return LANGUAGE.equals(lang);
    }
}
