package de.macbury.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.macbury.server.tiles.mapzen.MapZenApi;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
  public static void main(String[] args) throws UnirestException {
    MapZenApi.setApiKey("vector-tiles-XmQtRkM");

    createApplication();
  }

  private static LwjglApplication createApplication() {
    return new LwjglApplication(new GameExplorer(), getDefaultConfiguration());
  }

  private static LwjglApplicationConfiguration getDefaultConfiguration() {
    LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
    configuration.title = "CoreGame";
    configuration.width = 1368;
    configuration.height = 768;
    configuration.vSyncEnabled = true;
    configuration.foregroundFPS = 30;

    for (int size : new int[] { 128, 64, 32, 16 }) {
      configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
    }

    return configuration;
  }
}