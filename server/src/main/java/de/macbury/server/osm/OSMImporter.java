package de.macbury.server.osm;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.gen.exc.ReqlQueryLogicError;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static com.rethinkdb.RethinkDB.r;


/**
 * Import points of interest from osm into RethinkDB
 */
public class OSMImporter implements OSMParser.Listener {
  private final OSMParser parser;
  private final Connection connection;

  public OSMImporter(File fileToImport, String host, int port) {
    connection = r.connection().hostname(host).port(port).connect();
    parser     = new OSMParser(fileToImport, this);

    r.db("starflyer").tableCreate("locations").run(connection);

    try {
      parser.run();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    connection.close();
  }

  @Override
  public void onNodeFound(OSMParser parser, OSMNode nodeCursor) {

  }
}
