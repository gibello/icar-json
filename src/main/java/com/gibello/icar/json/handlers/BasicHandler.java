package com.gibello.icar.json.handlers;

import java.io.PrintStream;

import com.gibello.icar.json.parser.EventHandler;
import com.gibello.icar.json.parser.JsonParser;

/**
 * Basic handler for JSON push parser (displays JSON content abstract).
 * @author Pierre-Yves Gibello
 */
public class BasicHandler implements EventHandler {

	PrintStream out;
	int level = 0;

	public BasicHandler(PrintStream out) {
		this.out = out;
	}

	@Override
	public void startParsing(JsonParser parser) throws Exception {
		println(0, "Start JSON parsing");
	}

	@Override
	public void endParsing() throws Exception {
		println(0, "End JSON parsing");
		if(this.out != null) this.out.flush();
	}

	@Override
	public void startObject() throws Exception {
		println(this.level++, "Start object");
	}

	@Override
	public void endObject() throws Exception {
		println(--this.level, "End object");
	}

	@Override
	public void key(String key) throws Exception {
		println(this.level, "Key: " + key);
	}

	@Override
	public void simpleValue(String value) throws Exception {
		println(this.level, "Value: " + value);
	}

	@Override
	public void startArray() throws Exception {
		println(this.level++, "Start array");
	}

	@Override
	public void endArray() throws Exception {
		println(--this.level, "End array");
	}
	
	private void println(int level, String str) {
		if(this.out != null) {
			for(int i=0; i < level; i++) this.out.print("\t");
			this.out.println(str);
		}
	}
}
