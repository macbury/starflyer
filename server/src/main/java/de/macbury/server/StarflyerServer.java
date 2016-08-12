package de.macbury.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.macbury.SharedConsts;
import de.macbury.server.db.Database;
import de.macbury.server.utils.ServerArgs;

import java.io.IOException;

/**
 * LibGdx application running server logic
 */
public class StarflyerServer implements ApplicationListener {
  private static final String TAG = "StarflyerServer";
  private final Database database;
  private final ServerArgs args;
  private final Server server;

  public StarflyerServer(Database database, ServerArgs serverArgs) {
    this.database = database;
    this.args     = serverArgs;
    this.server   = new Server();
    server.addListener(new Listener() {
      @Override
      public void received(Connection connection, Object object) {
        super.received(connection, object);
        Gdx.app.log("RECIVED", object.toString());
      }
    });
  }

  @Override
  public void create() {
    Gdx.app.log(TAG, "Initializing...");
    server.start();
    try {
      server.bind(SharedConsts.PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void render() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }
}
