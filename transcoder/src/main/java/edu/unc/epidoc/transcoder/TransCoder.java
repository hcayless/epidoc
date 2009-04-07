/*
 * TransCoder.java
 *
 * (c) Hugh A. Cayless (hcayless@email.unc.edu)
 * This software is licensed under the terms of the GNU LGPL.
 * See http://www.gnu.org/licenses/lgpl.html for details.
 */

package edu.unc.epidoc.transcoder;

import edu.unc.epidoc.transcoder.xml.sax.TranscodingContentHandler;

import java.io.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.*;

import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.*;

/** TransCoder is the main class of the program.  It can easily be incorporated
 * into any other Java program.  TransCoder expects the user to set the
 * encoding type of any text that is being read and the desired output
 * encoding.  If you are using one of the types that has been implemented
 * in the edu.unc.epidoc package, then all you need to do is provide its name
 * within the package.  Otherwise, you will need to provide the fully-
 * qualified name of the parser or converter class you wish to use (and
 * make sure that class is in your Classpath).
 *
 * Examples:
 * Invoking the Transcoder against a file:
 * <PRE>
 *  TransCoder tc = new TransCoder("GreekKeys", "UnicodeC");
 *  String result = tc.getString(new File("C:/temp/test.txt"));
 * </PRE>
 * 
 * Invoking the Transcoder against a String:	
 * <PRE>
 *    String source = "A)/NDRA MOI E)/NNEPE, MOU=SA";
 *    TransCoder tc = new TransCoder();
 *    tc.setParser("Unicode");
 *    tc.setConverter("BetaCode");
 *    String result = tc.getString(source);
 *</PRE>
 *
 * @author Hugh A. Cayless (hcayless@email.unc.edu)
 * @version 0.3
 *
 *
 */
public class TransCoder {
    private Parser p;
    private Converter conv;
    private Properties parsers;
    private Properties converters;
    private String[] ps;
    private String[] cv;
    
    /** Creates new Transcoder */
    public TransCoder() {
        init();
    }
    
    /** Creates new <CODE>Transcoder<CODE> with the <CODE>Parser</CODE>
     * and <CODE>Converter</CODE> set by parameters.
     * @param from The name of the <CODE>Parser</CODE> to use with this instance
     * of the <CODE>Transcoder</CODE>.
     * @param to The name of the <CODE>Converter</CODE> to use with this instance
     * of the <CODE>Transcoder</CODE>.
     * @throws Exception if a problem is encountered.
     */    
    public TransCoder(String from, String to) throws Exception {
        init();
        /** Load the appropriate Parser class. */
        setParser(from);
        /** Load the appropriate Converter class */
        setConverter(to);
    }    
    
    private void init() {
        Class c = this.getClass();
        parsers = new Properties();
        converters = new Properties();
        try {
            parsers.load(c.getResourceAsStream("parsers.properties"));
            converters.load(c.getResourceAsStream("converters.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Provides access to the <CODE>Parser</CODE> being used.
     * @return The current <CODE>Parser</CODE>.
     */    
    public Parser getParser() {
        return p;
    }
    
    /** Allows the <CODE>Parser</CODE> to be set.
     * @param from The <CODE>Parser</CODE> to use.
     * @throws Exception If a problem is encountered in setting the <CODE>Parser</CODE>.
     */    
    public void setParser(String from) throws Exception {
        /** Load the appropriate Parser class. */
        String className;
        if (parsers.containsKey(from))
            className = (String)parsers.get(from);
        else {
            if (from.indexOf('.') > 0) {
                className = from;
            } else {
                className = "edu.unc.epidoc.transcoder."+from+"Parser";
            }
        }
        try {
            Class c = Class.forName(className);
            p = (Parser)c.newInstance();
        } catch (Exception e) {
            throw e;
        }
    }
    
    /** Provides access to the <CODE>Converter</CODE> being used.
     * @return The current <CODE>Converter</CODE>.
     */    
    public Converter getConverter() {
        return conv;
    }
    
    /** Allows the <CODE>Converter</CODE> to be set.
     * @param to The <CODE>Converter</CODE> to use.
     * @throws Exception If a problem is encountered in setting the <CODE>Converter</CODE>.
     */    
    public void setConverter(String to) throws Exception {
        /** Load the appropriate Converter class */
        String className;
        if (converters.containsKey(to))
            className = (String)converters.get(to);
        else {
            if (to.indexOf('.') > 0) {
                className = to;
            } else {
                className = "edu.unc.epidoc.transcoder."+to+"Converter";
            }
        }
        try {
            Class c = Class.forName(className);
            conv = (Converter)c.newInstance();
        } catch (Exception e) {
            throw e;
        }
    }
    
    /** Allows for new <CODE>Parsers</CODE> to be registered with the <CODE>Transcoder</CODE>.
     * @param name The name to be used to refer to the new <CODE>Parser</CODE>.
     * @param className The name of the new <CODE>Parser</CODE> class.  This should be
     * the full package name.
     */    
    public void addParser(String name, String className) {
        try {
            if (!parsers.containsValue(className)) {
                parsers.put(name, className);
                parsers.store(new FileOutputStream(this.getClass().getResource("parsers.properties").getPath()), "Parsers");
                ps = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Provides access to the list of <CODE>Parser</CODE>s known to the <CODE>Transcoder</CODE>.
     * @return The list of <CODE>Parsers</CODE>.
     */    
    public String[] getParsers() {
        if (ps == null) {
            ps = new String[parsers.size()];
            Enumeration e = parsers.keys();
            for (int i = 0; i < ps.length; i++)
                ps[i] = (String)e.nextElement();
        }
        return ps;
    }
    
    /** Allows for new <CODE>Converter</CODE>s to be registered with the <CODE>Transcoder</CODE>.
     * @param name The name to be used to refer to the new <CODE>Parser</CODE>.
     * @param className The name of the new <CODE>Parser</CODE> class.  This should be
     * the full package name.
     */    
    public void addConverter(String name, String className) {
        try {
            if (!converters.containsValue(className)) {
                converters.put(name, className);
                converters.store(new FileOutputStream(this.getClass().getResource("converters.properties").getPath()), "Converters");
                cv = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Provides access to the list of <CODE>Converter</CODE>s known to the <CODE>Transcoder</CODE>.
     * @return The list of <CODE>Parsers</CODE>.
     */    
    public String[] getConverters() {
        if (cv == null) {
            cv = new String[converters.size()];
            Enumeration e = converters.keys();
            for (int i = 0; i < cv.length; i++)
                cv[i] = (String)e.nextElement();
        }
        return cv;
    }
    
    /** Get the result <CODE>String</CODE> from the input <CODE>String</CODE>.
     * @param in The <CODE>String</CODE> to be transcoded.
     * @throws UnsupportedEncodingException if the encoding used by the Parser isn't supported.
     * @return The result of the transcoding operation.
     */
    public String getString(String in) throws UnsupportedEncodingException {
        p.setString(in);
        return conv.convertToString(p);
    }
    
    /** Get the result as a <CODE>String</CODE> from an input <CODE>File</CODE>.
     * This method should work with any type of encoding.
     * @param in The file to be transcoded.
     * @throws FileNotFoundException if the specified <CODE>File</CODE> does not exist.
     * @throws IOException if there is a problem reading the <CODE>File</CODE>.
     * @throws UnsupportedEncodingException if the encoding used by the Parser isn't supported.
     * @return The result of the transcoding operation.
     */
    public String getString(File in) throws FileNotFoundException, IOException, UnsupportedEncodingException {
        InputStreamReader isw = new InputStreamReader(new FileInputStream(in), p.getEncoding());
        StringBuffer strb = new StringBuffer();
        int chr = isw.read();
        while (chr > 0) {
            strb.append((char)chr);
            chr = isw.read();
        }
        isw.close();
        p.setString(strb.toString());
        return conv.convertToString(p);
    }
    
    public String getString(StringBuffer in, int start, int length) {
        p.setStringBuffer(in, start, length);
        return conv.convertToString(p);
    }
    
    /** Stream the result from an <CODE>InputStream</CODE> to an <CODE>OutputStream</CODE>.
     * This method should work with any type of encoding.
     * @param in The InputStream to be transcoded.
     * @param out The OutputStream to be written.
     * @throws IOException if there is a problem reading the <CODE>File</CODE>.
     * @throws UnsupportedEncodingException if the encoding used by the Parser isn't supported.
     */
    public void write(InputStream in, OutputStream out) throws IOException, UnsupportedEncodingException {
        BufferedReader br = new BufferedReader(
                                new InputStreamReader(in, p.getEncoding()));
        PrintStream output = new PrintStream(new BufferedOutputStream(out));
        String line = null;
        while ((line = br.readLine()) != null) {
            p.setString(line);
            output.write(conv.convertToString(p).getBytes(conv.getEncoding()));
            output.println();
        }
        output.flush();
        output.close();
    }
    
    public void writeXML(File source, OutputStream out, TransCoder tc) throws Exception {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        if (tFactory.getFeature(SAXSource.FEATURE) && tFactory.getFeature(SAXResult.FEATURE)) {
            TranscodingContentHandler handler = new TranscodingContentHandler();
            Serializer serializer = SerializerFactory.getSerializer 
                          (OutputPropertiesFactory.getDefaultMethodProperties("xml"));        
            serializer.setOutputStream(out);           
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.setFeature("http://xml.org/sax/features/validation", false );
            handler.setup(serializer.asContentHandler(), null,tc);
            InputSource is = new InputSource(new java.io.FileInputStream(source));
            is.setSystemId(source.getAbsoluteFile().getParentFile().getAbsolutePath() + "/");
            reader.parse(is);
        }
    }

    
    /** Get the result <CODE>String</CODE> from the input <CODE>String</CODE>.
     * Characters with codes above 127 will be returned as XML character entities.
     * @param in The <CODE>String</CODE> to be transcoded.
     * @throws UnsupportedEncodingException if the encoding used by the Parser isn't supported.
     * @return The result of the transcoding operation.
     */    
    public String getCharacterEntities(String in) throws UnsupportedEncodingException {
        p.setString(in);
        return conv.convertToCharacterEntities(p);
    }
    
    /**
     * Main method for the Transcoder class when launched from the command line.
     * @param args An array consisting either of: a String to be transcoded plus 
     * the source and result encodings, or: a source file, a result file, plus 
     * the source and result encodings.
     */
    public static void main(String[] args)  {
        if (args.length == 0) {
            edu.unc.epidoc.transcoder.gui.Tester.main(null);
        } else {
            TransCoder tc = new TransCoder();
            File tmpSource = null;
            File result = null;
            boolean xmlMode = false;
            try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-s")) {
                    tmpSource = new File(args[++i]);
                }
                if (args[i].equals("-o")) {
                    result = new File(args[++i]);
                }
                if (args[i].equals("-se")) {
                    tc.setParser(args[++i]);
                }
                if (args[i].equals("-oe")) {
                    tc.setConverter(args[++i]);
                }
                if (args[i].equals("-x")) {
                    xmlMode = true;
                }
                if (args[i].equals("--help")) {
                    System.out.println("The transcoder can be invoked with arguments denoting the source and result files or directories and the source and result encodings.");
                    System.out.println("Arguments:");
                    System.out.println("-s   The source file or directory.");
                    System.out.println("-o   The output file or directory.  If the source is a directory, the result must also be one.");
                    System.out.println("-se  The source encoding (default BetaCode).");
                    System.out.println("-oe  The output encoding (default UnicodeC).");
                    System.out.println("-x   Use XML mode.  Treat the source and result files as XML.  Not needed if the files have a .xml suffix.");
                    System.out.println();
                    System.out.println("Valid encodings are: BetaCode, PerseusBetaCode (Beta Code using lowercase ASCII), UncidodeC, UnicodeD, GreekKeys, SGreek, SPIonic, GreekXLit (output only).");
                    System.exit(0);
                }
            }
            if (tc.getParser() == null) {
                tc.setParser("BetaCode");
            }
            if (tc.getConverter() == null) {
                tc.setConverter("UnicodeC");
            }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            final File source = tmpSource;
            if (!source.exists()) {
                System.out.println(source.getAbsolutePath() + " does not exist.");
            }

            if (source.isDirectory()) {
                if (!result.isDirectory()) {
                    System.out.println("If the source is a directory, the result must also be a directory.");
                    System.exit(1);
                }
                File[] files = source.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        if (dir.equals(source)) {
                            File tmp = new File(dir, name);
                            if (!tmp.isHidden() && !tmp.isDirectory()) {
                                return true;
                            }
                        }
                        return false;
                    }
                });
                for (int i = 0;  i < files.length; i++) {
                    File out = new File(result, files[i].getName());
                    try {
                        out.createNewFile();
                        if (files[i].getName().endsWith(".xml") || xmlMode) {
                            tc.writeXML(files[i], new FileOutputStream(out), tc);
                        } else {
                            tc.write(new FileInputStream(source), new FileOutputStream(out));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Can't write to " + out.getAbsolutePath());
                    }
                }
            } else {
                try {
                    if (source.getName().endsWith(".xml") || xmlMode) {
                        tc.writeXML(source, new FileOutputStream(result), tc);
                    } else {
                        tc.write(new FileInputStream(source), new FileOutputStream(result));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Can't write to " + result.getAbsolutePath());
                    System.exit(1);
                }
            }
        }
    }
    
}