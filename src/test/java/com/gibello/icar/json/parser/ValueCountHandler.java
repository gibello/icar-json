package com.gibello.icar.json.parser;

import java.io.PrintStream;

/**
 * Count all JSON simple values
 * @author Pierre-Yves Gibello
 *
 */
public class ValueCountHandler implements EventHandler {

	String lastKey;
	String toCount;
	PrintStream out;
	int valueCount = 0;

	public ValueCountHandler(String value, PrintStream out) {
		this.toCount = value;
		this.out = out;
	}

	@Override
	public void startParsing(JsonParser parser) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void endParsing() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void startObject() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void endObject() throws Exception {
		// TODO Auto-generated method stub	
	}

	@Override
	public void startArray() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void endArray() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void key(String key) throws Exception {
		this.lastKey = key;
	}

	@Override
	public void simpleValue(String val) throws Exception {
		if(val == null || val.isEmpty()) {
			//out.println("Empty value detected for key: " + this.lastKey);
			if(toCount == null || toCount.isEmpty()) this.valueCount ++;
		} else if(val.equals(toCount)) this.valueCount ++;
	}

	/**
	 * Retrieves overall JSON simple values count
	 * @return Number of simple values found
	 */
	public int getValueCount() {
		return this.valueCount;
	}

}
