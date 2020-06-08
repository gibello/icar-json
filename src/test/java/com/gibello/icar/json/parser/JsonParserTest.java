package com.gibello.icar.json.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

class JsonParserTest {

	private void defaultParse(String json) throws Exception {
		JsonParser parser = new JsonParser();
		parser.parse(new ByteArrayInputStream(json.getBytes()), new DefaultHandler());
	}

	@Test
	void basicTest() {
		String json = "{\"key1\":\"value1\",\"key2\":{\"key3\":\"value3\"},\"key4\":[\"value4\",\"value5\"]}";
		try {
			defaultParse(json);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}
	
	@Test
	void emptyValuesTest() {
		String json = "{\"k1\":\"\",\"k2\":{\"k3\":\"\"},\"k4\":[\"\",\"\"]}";
		try {
			defaultParse(json);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}
	
	@Test
	void specialCharValuesTest() {
		String json = "{\"k1\":\":\\,\\\"{\\\'}[]\r\n\",\n"
					+ "\"k2\":{\"k3\":\" :\\,\\\"\r{\n\\\'}[] \"},\n"
					+ "\"k4\":[\":\\,\r\\\"{\", \"\n\\\'}[]\"]}";
		try {
			defaultParse(json);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}
}
