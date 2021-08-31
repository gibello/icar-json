package com.gibello.icar.json.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * JSON push parser (looks like XML sax parsing).
 * @author Pierre-Yves Gibello
 */
public class JsonParser {

	StringBuffer currentValue = new StringBuffer();
	int level = 0;
	int stack[] = new int[512];
	boolean quoted = false;

	// Used to distinguish empty value from "no value"
	// Typically: [ "" ] versus [ ]
	boolean emptyValue = false;

	boolean record;
	StringBuilder recordBuffer = new StringBuilder();

	private static final int OBJECT = 1;
	private static final int ARRAY = 2;
	private static final int VALUE = 4;
	private static final int KEY = 8;

	/**
	 * Parse a JSON input stream, invoking handler callbacks according to parsed tokens.
	 * @param input The JSON input stream (note: using a buffered stream can dramatically increase speed)
	 * @param handler A handler to catch tokens
	 * @throws Exception
	 */
	public void parse(InputStream input, EventHandler handler) throws Exception {
		int expect = OBJECT | ARRAY;
		int status = 0, mainIndex = 0, lineNo = 1;
		boolean firstval = true;
		int c = 0;

		handler.startParsing(this);

		PushbackInputStream in = new PushbackInputStream(input);
		while((c = in.read()) > 0) {
			mainIndex++;
			
			if(record) this.recordBuffer.append((char)c); // Record if requested

			switch((char)c) {
			case '{' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				firstval = true;
				if((expect & OBJECT) == 0) throw new IOException("Unexpected { at index " + mainIndex + " line " + lineNo);
				handler.startObject();

				status = pushStatus(OBJECT);
				expect = KEY;
				break;
			case '}' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				firstval = false;
				if((expect & VALUE) != 0) {
					handler.simpleValue(stripQuotes(this.currentValue.toString()));
					this.currentValue = new StringBuffer();
					expect &= ~VALUE;
				}
				handler.endObject();
				
				status = popStatus();
				if(status == OBJECT) expect = KEY;
				else expect = OBJECT | ARRAY;
				break;
			case '[' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				firstval = true;
				if((expect & ARRAY) == 0) throw new IOException("Unexpected [ at index " + mainIndex  + " line " + lineNo);
				handler.startArray();

				status = pushStatus(ARRAY);
				expect = VALUE | OBJECT | ARRAY;
				break;
			case ']' :
				if(this.quoted) {
					currentValue(c);
					break;
				}	
				if((expect & VALUE) != 0) {
					String value = this.currentValue.toString();
					if(! (firstval && value.isEmpty())) handler.simpleValue(stripQuotes(value));
					else if(this.emptyValue) handler.simpleValue("");
					this.currentValue = new StringBuffer();
				}
				firstval = false;
				handler.endArray();
				
				status = popStatus();
				
				expect = OBJECT | ARRAY;
				if(status == ARRAY) expect |= VALUE;
				else if(status == OBJECT) expect |= KEY;
				break;
			case ',' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				//if(firstval && this.currentValue.length() <= 0) throw new IOException("Unexpected comma at index " + mainIndex + " line " + lineNo);
				if((expect & KEY) != 0 && (firstval || this.currentValue.length()>0)) throw new IOException("Unexpected comma at index " + mainIndex + " line " + lineNo);
				else if((expect & VALUE) != 0) {
					if(! this.quoted) handler.simpleValue(stripQuotes(this.currentValue.toString()));
				}

				if(status == OBJECT) expect = KEY;
				else if(status == ARRAY) expect = VALUE | OBJECT | ARRAY;
				else throw new IOException("Unexpected comma");
				this.currentValue = new StringBuffer();
				firstval = false;
				break;
			case ':' :
				if(!this.quoted && (((expect & KEY) == 0) || this.currentValue.length() <= 0)) throw new IOException("Unexpected colon at index " + mainIndex + " line " + lineNo);
				if(! this.quoted) {
					handler.key(stripQuotes(this.currentValue.toString()));
					expect = VALUE | OBJECT | ARRAY;
					this.currentValue = new StringBuffer();
				} else currentValue(c);
				break;
			case ' ' :
			case '\t' :
				if((expect & VALUE) != 0 && this.currentValue.length() > 0) {
					if(this.quoted) currentValue(c);
				}
				// else ignore
				break;
			case '\r' :
			case '\n' :
				if(c == '\n') lineNo++;
				// ignore
				break;
			case '\\' :
				if((expect & VALUE) != 0) {
					int next = in.read();
					mainIndex++; // 1 additional char read
					if(next == '\\') {
						currentValue(c); currentValue(c);
					} else if(next == '\"') {
						if(this.quoted) {
							currentValue(c);
							currentValue(next);
						}
					} else {
						in.unread(next);
						mainIndex--; // 1 char pushed back
						currentValue(c);
					}
				} else currentValue(c);
				break;
			case '\"':
				if((expect & VALUE) != 0) {
					this.emptyValue = (this.quoted && this.currentValue.toString().trim().isEmpty());
					this.quoted = !this.quoted;
				}
				break;
			default :
				if((expect & (VALUE | KEY)) == 0) throw new IOException("Unexpected character: " + (char)c + " at index " + mainIndex + " line " + lineNo);
				currentValue(c);
				break;
			}
		}

		handler.endParsing();
	}

	/**
	 * Start recording
	 */
	public void startRecording() {
		this.recordBuffer.setLength(0);
		this.record = true;
	}
	
	/**
	 * End recording
	 * @return The recorded text
	 */
	public String endRecording() {
		this.record = false;
		return this.recordBuffer.toString();
	}
	
	/**
	 * Check whether recording is in progress.
	 * @return true if recording, false otherwise
	 */
	public boolean isRecording() { return this.record; }


	private int pushStatus(int status) throws IOException {
		if(this.level >= this.stack.length-1) throw new IOException("Stack overflow");
		this.stack[++ this.level] = status;
		return status;
	}
	
	private int popStatus() {
		if(this.level <= 0) {
			this.level = 0;
			return 0;
		}
		return this.stack[-- this.level ];
	}
	
	private void currentValue(int c) {
		this.currentValue.append((char)c);
	}

	private String stripQuotes(String val) {
    	//val = val.trim();
		//if(val.startsWith("\"")) System.out.println("quoted!");
    	if(val.startsWith("\"")) val = val.substring(1);
		if(val.endsWith("\"") && val.length() > 2 && ! (val.charAt(val.length()-2) == '\\')) val = val.substring(0, val.length()-1);
		return val;
    }
}
