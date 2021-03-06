********************************************************************************
* TransCoder README                                                            *
********************************************************************************
What it is:
	A library of Java classes designed to translate Ancient Greek from one encoding to another.
	
License:
	This software is copyright Hugh A. Cayless.  It is licensed under the terms of the GNU LGPL.
	See LICENSE.txt for license details.
	
Supported encodings:
	BetaCode, PerseusBetaCode (Beta Code using lowercase ASCII), Unicode (input only) UnicodeC (output only), UnicodeD (output only), GreekKeys, SGreek, SPIonic, GreekXLit (output only). 
	
How to use it:
  The transcoder can be invoked with arguments denoting the source and result files or directories and the source and result encodings.
  Arguments:
  -s   The source file or directory.
  -o   The output file or directory.  If the source is a directory, the result must also be one.
  -se  The source encoding (default BetaCode).
  -oe  The output encoding (default UnicodeC).
  -x   Use XML mode.  Treat the source and result files as XML.  Not needed if the files have a .xml suffix.
  -f   A filter to be used in determining what files to process, e.g. 'xml' for files ending with '.xml'.
  -r   Recursively process the input folder.  '-o' is ignored if this flag is used.
  -v   Verbose output.

	The classes that do the work implement the Parser and Converter interfaces.  These may be loaded and accessed by the TransCoder class. The following code snippet creates a TransCoder and uses it to transform a Greek text file written in GreekKeys to a Unicode string.  The transcoder.jar file now includes a GUI form which can be used for testing the conversion of various font encodings.  The .jar file is executable, so it should be possible to run it by double clicking on the icon or executing it from the command line (java -jar transcoder.jar).
	
	<code>
	    TransCoder tc = new TransCoder("GreekKeys", "UnicodeC");
	    String result = tc.getString(new File("C:/temp/test.txt"));
	</code>
	
	or
	
	<code>
	    String source = "A)/NDRA MOI E)/NNEPE, MOU=SA";
	    TransCoder tc = new TransCoder();
	    tc.setParser("Unicode");
	    tc.setConverter("BetaCode");
	    String result = tc.getString(source);
	</code>
	
