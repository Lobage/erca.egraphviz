 /*******************************************************************************
 * Copyright (c) 2009, Jean-Rémy Falleri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jean-Rémy Falleri - initial API and implementation
 *******************************************************************************/

package com.googlecode.egraphviz;

import java.io.IOException;

public class DotOptions {
	
	public static void main(String[] args) {
		DotOptions o = new DotOptions();
		o.setInputFile("test/test.dot");
		o.setAlgorithm("dot");
		o.setOutputFile("test/test.svg");
		o.setOutputFormat("svg");
		try {
			System.out.println(o.runDotCommand());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static final String DEFAULT_OUTPUT_FORMAT = "svg";
	
	public static final String DEFAULT_ALGORITHM = "dot";
	
	public static final String[] OUTPUT_FORMATS = new String[] { "svg" , "jpg" , "eps" , "png" , "fig" , "gif" , "ps" };
	
	public static final String[] ALGORITHMS = new String[] { "dot" , "neato" , "fdp" , "circo" };
	
	private String outputFormat;
	
	private String outputFile;

	private String inputFile;
	
	private String algorithm;
	
	private String installFolder;
	
	public DotOptions() {
		this.outputFile = null;
		this.inputFile = null;
		this.algorithm = DEFAULT_ALGORITHM;
		this.outputFormat = DEFAULT_OUTPUT_FORMAT;
		
		if ( System.getProperty("os.name").toLowerCase().startsWith("mac") )
			installFolder = "/usr/local/bin/";
		else
			installFolder = "";
	}
	
	public DotOptions(String installFolder) {
		this();
		this.installFolder = installFolder;
	}
	
	public void setOutputFormat(String outputFormat) {
		if ( has(OUTPUT_FORMATS,outputFormat) )
			this.outputFormat = outputFormat;
	}
	
	public void setAlgorithm(String algorithm) {
		if ( has(ALGORITHMS,algorithm) )
			this.algorithm = algorithm;
	}
	
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	public boolean isReady() {
		return ( inputFile != null && algorithm != null && outputFormat != null && outputFile != null);
	}
	
	public String[] getCommandLine() {
		String[] cmd = new String[] { "bash" , "-c", installFolder + algorithm + " -T" + outputFormat + " \"" + inputFile + "\"" + " -o " + "\"" + outputFile + "\""};
		return cmd;
	}
	
	public int runDotCommand() throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec( getCommandLine() );
		p.waitFor();
		//InputStreamReader r = new InputStreamReader(p.getErrorStream());
		//BufferedReader br = new BufferedReader(r);
		//System.out.println(br.readLine());
		return p.exitValue();
	}

	private boolean has(String[] tab,String string) {
		for(String s: tab)
			if ( s.equals(string) )
				return true;
		
		return false;
	}
	
}
