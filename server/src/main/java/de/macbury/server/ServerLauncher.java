package de.macbury.server;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.macbury.server.db.Database;
import de.macbury.server.db.models.Location;
import de.macbury.server.osm.OSMImporter;
import de.macbury.server.osm.OSMNode;
import de.macbury.server.osm.OSMParser;
import de.macbury.server.tiles.mapzen.MapZenApi;
import de.macbury.server.tiles.mapzen.MapZenLayersResult;
import de.macbury.server.utils.ServerArgs;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Launches the server application.
 */
public class ServerLauncher {
  public static void main(String[] args) {
    ServerArgs serverArgs = new ServerArgs(args);
    if (serverArgs.isSuccess()) {
      MapZenApi.setApiKey(serverArgs.getMapZenApiKey());
      try {
        MapZenApi.getTile(72839, 44399, new Callback<MapZenLayersResult>() {
          @Override
          public void completed(HttpResponse<MapZenLayersResult> response) {
            System.err.println();
          }

          @Override
          public void failed(UnirestException e) {
            System.err.println();
          }

          @Override
          public void cancelled() {
            System.err.println();
          }
        });
      } catch (UnirestException e) {
        e.printStackTrace();
      }
      Database database = new Database(serverArgs.getDatabaseHost(), serverArgs.getDatabasePort(), serverArgs.getDatabaseName());

      if (serverArgs.isGoingToImportFile()) { // Import osm into database
        OSMImporter osmImporter = new OSMImporter(serverArgs.getImportFile(), database);
        System.exit(0);
      } else {
        HeadlessApplication application = new HeadlessApplication(new StarflyerServer(database, serverArgs), getDefaultConfiguration());
      }
    } else {
      System.err.println();
      System.err.println(serverArgs.getHelp());
      System.err.println();
      System.exit(1);
    }
  }


  private static HeadlessApplicationConfiguration getDefaultConfiguration() {
    HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
    configuration.renderInterval = -1f; // When this value is negative, Starflyer#render() is never called.
    return configuration;
  }
}