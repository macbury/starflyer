package de.macbury.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import de.macbury.SharedConsts;
import de.macbury.Starflyer;

import java.io.IOException;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
      Client client = new Client();
      client.start();
      try {
        client.connect(5000, "localhost", SharedConsts.PORT);
        client.sendTCP("Hello!");
        client.addListener(new Listener() {

        });
      } catch (IOException e) {
        e.printStackTrace();
      }

      return new LwjglApplication(new Starflyer(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Starflyer";
        configuration.width = 640;
        configuration.height = 480;
        for (int size : new int[] { 128, 64, 32, 16 }) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }


        return configuration;
    }
}