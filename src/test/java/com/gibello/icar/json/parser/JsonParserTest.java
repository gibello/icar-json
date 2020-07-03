package com.gibello.icar.json.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.gibello.icar.json.handlers.PrettyPrintHandler;

/**
 * Basic tests for JSON push parser
 * @author Pierre-Yves Gibello
 */
class JsonParserTest {

	/**
	 * Parse JSON String using default handler
	 * @param json JSON String to parse
	 * @throws Exception
	 */
	private void defaultParse(String json) throws Exception {
		JsonParser parser = new JsonParser();
		parser.parse(new ByteArrayInputStream(json.getBytes()), new DefaultHandler());
	}
	
	/**
	 * Parse JSON Stream using default handler
	 * @param json JSON Stream to parse
	 * @throws Exception
	 */
	private void defaultParse(InputStream json) throws Exception {
		JsonParser parser = new JsonParser();
		parser.parse(json, new DefaultHandler());
	}

	/**
	 * Parse JSON String and render using PrettyPrint handler
	 * @param json JSON String to parse
	 * @param out Target PrintStream for rendering
	 * @throws Exception
	 */
	private void prettyPrintParse(String json, PrintStream out) throws Exception {
		JsonParser parser = new JsonParser();
		parser.parse(new ByteArrayInputStream(json.getBytes()), new PrettyPrintHandler(out));
	}
	
	/**
	 * Pretty-print back and forth (render then parse back)
	 * @param json JSON String to render then parse
	 */
	private void prettyPrintReturn(String json) {
		try {
			File prettyJson = File.createTempFile("icar-json", "json");
			prettyPrintParse(json, new PrintStream(prettyJson));
			defaultParse(new FileInputStream(prettyJson));
		} catch (Exception e) {
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}

	@Test
	void basicTest() {
		String json = "{\"key1\":\"value1\",\"key2\":{\"key3\":\"value3\"},\"key4\":[\"value4\",\"value5\"]}";
		try {
			defaultParse(json);
			prettyPrintReturn(json);
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
			prettyPrintReturn(json);
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
			prettyPrintReturn(json);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}
	
	@Test
	void numericValuesTest() {
		String json = "{\"startAt\":0,\"maxResults\":0,\"total\":23.08,\"issues\":[1,2,\"3\",4.5]}";
		try {
			defaultParse(json);
			prettyPrintReturn(json);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			fail(e.getMessage());
		}
	}
}
