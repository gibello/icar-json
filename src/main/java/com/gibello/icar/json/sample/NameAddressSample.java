package com.gibello.icar.json.sample;

import java.io.ByteArrayInputStream;

import com.gibello.icar.json.parser.JsonParser;

/**
 * This example parses JSON-encoded name/address information, with the following format:
 * { "name": "My name", "address": { city: "My city", country: "My country" } }
 * Note: the parser is tolerant concerning possibly missing quotes around keys
 * (for example: { key: "value" } and { "key": "value" } are accepted).
 * @author Pierre-Yves Gibello
 */
public class NameAddressSample extends com.gibello.icar.json.parser.DefaultHandler {
	
	String name;
	String city;
	String country;
	
	String lastKey;
	int level;

	@Override
	public void startObject() {
		this.level ++;
	}

	@Override
	public void endObject() {
		this.level --;
	}

	@Override
	public void key(String key) {
		this.lastKey = key;
	}

	@Override
	public void simpleValue(String val) throws Exception {
		if("name".equals(this.lastKey)) {
			if(level > 1) throw new Exception("Syntax error: name should be at 1st level");
			this.name = val;
		}
		else if("city".equals(lastKey)) this.city = val;
		else if("country".equals(lastKey)) this.country = val;
	}

	/**
	 * Specific method (not from the EventHandler interface)
	 * @return A NameAddress that contains the parsed data
	 */
	public NameAddress getResult() {
		return new NameAddress(this.name, this.city, this.country);
	}

	// Test main program.
	public static void main(String args[]) throws Exception {
		JsonParser parser = new JsonParser();
	    NameAddressSample handler = new NameAddressSample();
	    String json = (args.length > 0 ? args[0] : "{ \"name\": \"Oscar Wilde\", address: { city: \"London\", country: \"UK\" }}" );
		parser.parse(new ByteArrayInputStream(json.getBytes()), handler);
	    System.out.println(handler.getResult());
	}
}

/**
 *  Class that represents name/address information (name, city, country) and formats it.
 */
class NameAddress {

	String name;
	String city;
	String country;

	public NameAddress(String name, String city, String country) {
		this.name = name;
		this.city = city;
		this.country = country;
	}

	public String toString() {
		return this.name + " lives in " + this.city + ", " + this.country;
	}
}
