package com.gibello.icar.json.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

class JsonParserTest {

	@Test
	void basicTest() {
		String json = "{\"key1\":\"value1\",\"key2\":{\"key3\":\"value3\"},\"key4\":[\"value4\",\"value5\"]}";
		JsonParser parser = new JsonParser();
		try {
			parser.parse(new ByteArrayInputStream(json.getBytes()), new DefaultHandler());
		} catch (Exception e) {
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}
}
