package de.macbury.server.osm;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Parse osm file and emits on listener found nodes
 */
public class OSMParser extends DefaultHandler {
  private static final String ELEMENT_NODE = "node";
  private static final String ELEMENT_TAG = "tag";
  private static final String ATTRIBUTE_KEY = "k";
  private static final String ATTRIBUTE_VALUE = "v";
  private final File file;
  private final Listener listener;
  private OSMNode cursor;

  public OSMParser(File file, Listener listener) {
    this.file = file;
    this.listener = listener;
    if (!file.exists()) {
      throw new RuntimeException("Could not find file: " + file.getAbsolutePath());
    }
  }

  /**
   * Parse file
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public void run() throws ParserConfigurationException, SAXException, IOException {
    this.cursor              = new OSMNode();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser      = factory.newSAXParser();
    saxParser.parse(file, this);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    switch (qName) {
      case ELEMENT_NODE:
        cursor.readFrom(attributes);
        break;
      case ELEMENT_TAG:
        cursor.tags.put(
                attributes.getValue(ATTRIBUTE_KEY),
                attributes.getValue(ATTRIBUTE_VALUE)
        );
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (qName == ELEMENT_NODE) {
      listener.onNodeFound(this, cursor);
    }
  }

  public interface Listener {
    public void onNodeFound(OSMParser parser, OSMNode nodeCursor);
  }
}
