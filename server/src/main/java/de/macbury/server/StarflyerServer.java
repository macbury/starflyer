package de.macbury.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
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

  public StarflyerServer(Database database, ServerArgs serverArgs) {
    this.database = database;
    this.args     = serverArgs;
  }

  @Override
  public void create() {
    Gdx.app.log(TAG, "Initializing...");
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
