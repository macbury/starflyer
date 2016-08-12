package de.macbury.server.osm;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.gen.exc.ReqlQueryLogicError;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import de.macbury.server.db.Database;
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
  private final Database database;

  public OSMImporter(File fileToImport, Database database) {
    parser     = new OSMParser(fileToImport, this);
    this.database   = database;

    try {
      parser.run();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onNodeFound(OSMParser parser, OSMNode cursor) {
    if (cursor.isPointOfInterest() && cursor.haveName()) {
      try {
        database.locations.create(cursor.toLocation());
      } catch (Exception e){
        e.toString();
      }

    }
  }
}
