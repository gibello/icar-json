# Icar fast JSON push parser (SAX-like)

Designed in a minimalistic way (as little code as possible), with no external dependency, and released under Creative Commons Zero (CC0) "public domain" license.

Fast enough to parse a 50 Mo JSON file within 3 seconds !

## Build with maven

```
$ mvn clean install
```

## Quick start: pretty-print JSON

```
$ java -jar target/icar.json-0.0.1-SNAPSHOT.jar any-json-file.json
```
 
## How to develop your own parser

Icar is a push, event-based parser.

Writing your own parser requires to override the com.gibello.icar.json.parser.DefaultHandler class (or implement the com.gibello.icar.json.parser.eventHandler interface).
 
For example:

```
package my.parser;

import com.gibello.icar.json.parser.DefaultHandler;
import com.gibello.icar.json.parser.JsonParser;

import java.io.ByteArrayInputStream;

/**
 * Provide a handler
 */
public class MyHandler extends DefaultHandler {

  @Override
  public void key(String key) throws Exception {
    System.out.println("Got key: " + key);
  }

  @Override
  public void simpleValue(String val) throws Exception {
    System.out.println("Got value: " + val);
  }

  /**
   * Main program: parse a JSON String!
   */
  public static void main(String args[]) throws Exception {
    JsonParser parser = new JsonParser();
    String json = "{ \"productInfo\": \"Icar JSON Parser\" }";
    parser.parse(new ByteArrayInputStream(json.getBytes()), new MyHandler());
  }

}

```

## Tip for faster parsing of JSON files

Using a BufferedInputStream is almost 10 times faster than a simple FileInputStream.

Example:

```
parser.parse(new BufferedInputStream(new FileInputStream(jsonFile)), handler);
// Code above is 10 times faster than: parser.parse(new FileInputStream(jsonFile), handler);
```

## Embed the code in your project ?

Why not, as an alternative to a dependency ?

One can just pick the com.gibello.icar.json.parser package (in src/main/java/com/gibello/icar/json/parser).
It is just 3 files, less than 300 lines of code, and is enough.

Feel free to rename, move to your own package, the CC0 license allows anything you like :)

