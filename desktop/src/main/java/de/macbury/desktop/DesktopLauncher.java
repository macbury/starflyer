package de.macbury.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.macbury.Starflyer;
import de.macbury.server.tiles.TilesManager;
import de.macbury.server.tiles.cache.NullTileCache;
import de.macbury.server.tiles.mapzen.MapZenApi;
import org.apache.http.impl.client.HttpClientBuilder;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
  public static void main(String[] args) throws UnirestException {
    MapZenApi.setApiKey("vector-tiles-XmQtRkM");

    TilesManager tilesManager = new TilesManager(new NullTileCache());
    tilesManager.retrieve(72839, 44399);

    createApplication();
  }

  private static LwjglApplication createApplication() {
    return new LwjglApplication(new Starflyer(), getDefaultConfiguration());
  }

  private static LwjglApplicationConfiguration getDefaultConfiguration() {
    LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
    configuration.title = "Starflyer";
    configuration.width = 1368;
    configuration.height = 768;

    for (int size : new int[] { 128, 64, 32, 16 }) {
      configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
    }

    return configuration;
  }
}