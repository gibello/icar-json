package com.gibello.icar.json.parser;

/**
 * Event handler interface for JSON push parser.
 * @author Pierre-Yves Gibello
 */
public interface EventHandler {
	
	void startParsing(JsonParser parser) throws Exception;
	void endParsing() throws Exception;
	
	void startObject() throws Exception;
	void endObject() throws Exception;
	void startArray() throws Exception;
	void endArray() throws Exception;

	void key(String key) throws Exception;
	void simpleValue(String val) throws Exception;
}
