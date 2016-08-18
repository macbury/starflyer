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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Starflyer extends ApplicationAdapter {
  private Stage stage;
  private PerspectiveCamera camera;
  private Model model;
  private ModelInstance instance;
  private ModelBatch modelBatch;

  @Override
  public void create () {
    VisUI.load(SkinScale.X1);

    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(10, 10, 10);
    camera.far = 300;
    camera.near = 1f;
    camera.lookAt(Vector3.Zero);

    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin(); {
      modelBuilder.part("box", GL30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN))).box(5, 5, 5);
    } model = modelBuilder.end();
    instance = new ModelInstance(model);

    modelBatch = new ModelBatch();
    VisTable root = new VisTable();
    root.setFillParent(true);
    stage.addActor(root);

    final VisTextButton textButton = new VisTextButton("click me!");
    textButton.addListener(new ChangeListener() {
        @Override
        public void changed (ChangeEvent event, Actor actor) {
            textButton.setText("clicked");
            Dialogs.showOKDialog(stage, "message", "good job!");
        }
    });

    VisWindow window = new VisWindow("example window");
    window.add("this is a simple VisUI window").padTop(5f).row();
    window.add(textButton).pad(10f);
    window.pack();
    window.centerWindow();
    stage.addActor(window.fadeIn());
  }

  @Override
  public void resize (int width, int height) {
    stage.getViewport().update(width, height, true);
    camera.viewportWidth = width;
    camera.viewportHeight = height;
    camera.update(true);
  }

  @Override
  public void render () {
    camera.update();
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    modelBatch.begin(camera); {
      modelBatch.render(instance);
    } modelBatch.end();

    Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    stage.draw();
  }

  @Override
  public void dispose () {
    VisUI.dispose();
    stage.dispose();
  }
}