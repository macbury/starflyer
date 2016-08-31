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

  @Override
  public void preload() {

  }

  @Override
  public void create() {
    this.modelBatch       = new ModelBatch();
    this.messages         = new MessagesManager();
    this.camera           = new GeoPerspectiveCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    this.entities = new EntityManagerBuilder()
            .withMessageDispatcher(messages)
            .withCamera(camera)
            .withModelBatch(modelBatch)
            .build();

    createPlayer();
    createBoxModel();
  }

  private void createBoxModel() {
    Entity boxEntity                 = this.entities.createEntity();
    PositionComponent positionComponent = entities.createComponent(PositionComponent.class);

    positionComponent.visible = true;
    boxEntity.add(positionComponent);

    ModelBuilder modelBuilder = new ModelBuilder();
    modelBuilder.begin(); {
      MeshPartBuilder box = modelBuilder.part("box", GL30.GL_LINES, VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(Color.RED)));
      box.box(1,1,1);
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
    cameraComponent.rotation = 0.3f;

    playerEntity.add(positionComponent);
    playerEntity.add(cameraComponent);

    entities.addEntity(playerEntity);
  }

  @Override
  public void show() {

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
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    entities.update(Gdx.graphics.getDeltaTime());
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
