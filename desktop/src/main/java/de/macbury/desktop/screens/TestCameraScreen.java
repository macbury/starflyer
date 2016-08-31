package de.macbury.desktop.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisWindow;
import de.macbury.entity.EntityManager;
import de.macbury.entity.EntityManagerBuilder;
import de.macbury.entity.components.CameraComponent;
import de.macbury.entity.components.ModelInstanceComponent;
import de.macbury.entity.components.PositionComponent;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.screens.AbstractScreen;

/**
 * Created by macbury on 30.08.16.
 */
public class TestCameraScreen extends AbstractScreen {
  private MessagesManager messages;
  private GeoPerspectiveCamera camera;
  private EntityManager entities;
  private ModelBatch modelBatch;
  private Stage stage;
  private VisSlider rotationSlider;
  private VisSlider tiltSlider;
  private VisSlider zoomSlider;
  private VisSlider cameraXSlider;
  private VisSlider cameraYSlider;
  private Vector3 origin;

  @Override
  public void preload() {

  }

  @Override
  public void create() {
    stage               = new Stage(new ScreenViewport());
    this.modelBatch       = new ModelBatch();
    this.messages         = new MessagesManager();
    this.camera           = new GeoPerspectiveCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    this.entities = new EntityManagerBuilder()
            .withMessageDispatcher(messages)
            .withCamera(camera)
            .withModelBatch(modelBatch)
            .build();

    this.origin = new Vector3(200000, 200000, 0);

    createPlayer();
    createBoxModel();

  }

  private void createCameraInspector(final CameraComponent cameraComponent, final PositionComponent cameraPositionComponent) {
    VisWindow cameraInspectorWindow = new VisWindow("Camera");

    this.rotationSlider = new VisSlider(0, MathUtils.PI2, 0.01f, false);
    rotationSlider.setValue(cameraComponent.rotation);

    rotationSlider.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        cameraComponent.rotation = rotationSlider.getValue();
      }
    });

    this.tiltSlider     = new VisSlider(0, MathUtils.PI2, 0.01f, false);
    tiltSlider.setValue(cameraComponent.tilt);

    tiltSlider.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        cameraComponent.tilt = tiltSlider.getValue();
      }
    });

    this.zoomSlider     = new VisSlider(1f, 100.0f, 0.1f, false);
    zoomSlider.setValue(cameraComponent.zoom);

    zoomSlider.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        cameraComponent.zoom = zoomSlider.getValue();
      }
    });

    cameraInspectorWindow.add(new VisLabel("Rotation")).right().padRight(15);
    cameraInspectorWindow.add(rotationSlider).row();
    cameraInspectorWindow.add(new VisLabel("Tilt")).right().padRight(15);
    cameraInspectorWindow.add(tiltSlider).row();
    cameraInspectorWindow.add(new VisLabel("Zoom")).right().padRight(15);
    cameraInspectorWindow.add(zoomSlider).row();

    this.cameraXSlider     = new VisSlider(-100f, 100.0f, 0.1f, false);
    cameraXSlider.setValue(cameraPositionComponent.x);
    cameraXSlider.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        cameraPositionComponent.x = cameraXSlider.getValue();
      }
    });

    cameraInspectorWindow.add(new VisLabel("X")).right().padRight(15);
    cameraInspectorWindow.add(cameraXSlider).row();

    this.cameraYSlider     = new VisSlider(-100f, 100.0f, 0.1f, false);
    cameraYSlider.setValue(cameraPositionComponent.y);
    cameraYSlider.addCaptureListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        cameraPositionComponent.y = cameraYSlider.getValue();
      }
    });
    cameraInspectorWindow.add(new VisLabel("Y")).right().padRight(15);
    cameraInspectorWindow.add(cameraYSlider).row();

    cameraInspectorWindow.pack();

    this.stage.addActor(cameraInspectorWindow);
  }

  private void createBoxModel() {
    Entity boxEntity                 = this.entities.createEntity();
    PositionComponent positionComponent = entities.createComponent(PositionComponent.class);

    positionComponent.set(origin);
    positionComponent.visible = true;
    boxEntity.add(positionComponent);

    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin(); {
      MeshPartBuilder box = modelBuilder.part("box", GL30.GL_LINES, VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(Color.RED)));
      box.box(2,2,0.2f);
    };

    boxEntity.add(new ModelInstanceComponent(modelBuilder.end()));

    entities.addEntity(boxEntity);
  }

  private void createPlayer() {
    Entity playerEntity                 = this.entities.createEntity();
    PositionComponent positionComponent = entities.createComponent(PositionComponent.class);
    CameraComponent cameraComponent     = entities.createComponent(CameraComponent.class);

    cameraComponent.zoom = 10;
    cameraComponent.tilt = 0.4f;
    cameraComponent.rotation = 0.0f;

    positionComponent.set(origin);

    playerEntity.add(positionComponent);
    playerEntity.add(cameraComponent);

    entities.addEntity(playerEntity);


    createCameraInspector(cameraComponent, positionComponent);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public boolean isDisposedAfterHide() {
    return false;
  }

  @Override
  public void hide() {

  }

  @Override
  public void resize(int width, int height) {
    camera.resize(width, height);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    entities.update(Gdx.graphics.getDeltaTime());

    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    stage.draw();
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
