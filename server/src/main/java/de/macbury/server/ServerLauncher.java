package de.macbury.server;

import de.macbury.server.db.Database;
import de.macbury.server.osm.OSMImporter;
import de.macbury.server.osm.OSMNode;
import de.macbury.server.osm.OSMParser;
import de.macbury.server.utils.ServerArgs;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Launches the server application.
 */
public class ServerLauncher {
  public static void main(String[] args) {
    ServerArgs serverArgs = new ServerArgs(args);

    if (serverArgs.isSuccess()) {
      Database database = new Database(serverArgs.getDatabaseHost(), serverArgs.getDatabasePort(), serverArgs.getDatabaseName());

    } else {
      System.err.println();
      System.err.println(serverArgs.getHelp());
      System.err.println();
      System.exit(1);
    }

    if (serverArgs.isGoingToImportFile()) {
      /*/OSMImporter osmImporter = new OSMImporter(
              serverArgs.getImportFile(),
              serverArgs.getDatabaseHost(),
              serverArgs.getDatabasePort()
      );*/
    } else {

    }

    System.exit(0);
  }
}