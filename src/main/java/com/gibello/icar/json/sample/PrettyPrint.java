package com.gibello.icar.json.sample;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.gibello.icar.json.handlers.PrettyPrintHandler;
import com.gibello.icar.json.parser.JsonParser;

/**
 * Pretty-print JSON file content.
 * Default class for "java -jar" (not just an example, but also a utility class)
 * @author Pierre-Yves Gibello
 */
public class PrettyPrint {

	public static void main(String args[]) throws Exception {
	    if(args.length < 1) throw new Exception("Usage: PrettyPrint <json-file>");
		JsonParser parser = new JsonParser();
	    PrettyPrintHandler handler = new PrettyPrintHandler(System.out);
	    // Use BufferedInputStream for more speed (up to x10).
		parser.parse(new BufferedInputStream(new FileInputStream(new File(args[0]))), handler);
	}
}
