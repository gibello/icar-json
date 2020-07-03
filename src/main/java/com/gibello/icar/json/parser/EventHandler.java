package com.gibello.icar.json.parser;

/**
 * Event handler interface for JSON push parser.
 * @author Pierre-Yves Gibello
 */
public interface EventHandler {
	
	/**
	 * Callback triggered before parsing starts
	 * @param parser The JSON parser (useful to inject the parser)
	 * @throws Exception
	 */
	void startParsing(JsonParser parser) throws Exception;
	
	/**
	 * Callback triggered after parsing ends
	 * @throws Exception
	 */
	void endParsing() throws Exception;
	
	/**
	 * Callback triggered upon JSON object start (eg. first '{' in '{ "a":"b" }').
	 * @throws Exception
	 */
	void startObject() throws Exception;
	
	/**
	 * Callback triggered upon JSON object end (eg. last '}' in '{ "a":"b" }').
	 * @throws Exception
	 */
	void endObject() throws Exception;
	
	/**
	 * Callback triggered upon JSON array start (eg. first '[' in '{ "a": [1,2] }').
	 * @throws Exception
	 */
	void startArray() throws Exception;
	
	/**
	 * Callback triggered upon JSON array end (eg. first ']' in '{ "a": [1,2] }').
	 * @throws Exception
	 */
	void endArray() throws Exception;

	/**
	 * Callback triggered upon JSON key (eg. "a" in '{ "a": "b" }').
	 * @param key The detected key (eg. "a" for '{ "a": "b" }').
	 * @throws Exception
	 */
	void key(String key) throws Exception;
	
	/**
	 * Callback triggered upon JSON simple value (eg. "b" in '{ "a": "b" }').
	 * @param val The detected value (eg. "b" for '{ "a": "b" }').
	 * @throws Exception
	 */
	void simpleValue(String val) throws Exception;
}
