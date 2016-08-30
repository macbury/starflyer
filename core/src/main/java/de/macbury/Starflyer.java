package de.macbury;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import de.macbury.screens.ScreenManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Starflyer extends ApplicationAdapter {
  public ScreenManager screens;

  @Override
  public void create() {
    screens = new ScreenManager(this);
  }

  @Override
  public void resize(int width, int height) {
    screens.resize(width, height);
  }

  @Override
  public void render() {
    screens.tick(Gdx.graphics.getDeltaTime());
  }

  @Override
  public void pause() {
    screens.pause();
  }

  @Override
  public void resume() {
    screens.resume();
  }

  @Override
  public void dispose() {
    screens.dispose();
  }
}