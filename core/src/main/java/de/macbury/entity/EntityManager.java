package de.macbury.entity;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.entity.components.CameraComponent;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.entity.systems.*;

/**
 * Manage all {@link com.badlogic.ashley.core.Entity} in game
 */
public class EntityManager extends PooledEngine implements Disposable, EntityListener {
  private MessagesManager messages;
  private TileSystem tileSystem;
  private CameraSystem cameraSystem;
  private SceneOffsetSystem sceneOffsetSystem;
  private CameraControllerSystem cameraControllerSystem;
  /**
   * Handler to entity with camera
   */
  private Entity cameraEntity;

  public EntityManager(MessagesManager messages) {
    super();
    this.messages = messages;
    this.addEntityListener(Family.all(CameraComponent.class).get(), this);
  }
  /**
   * Update {@link MessageDispatcher} and {@link EntitySystem}s
   * @param deltaTime
   */
  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);
    messages.update();
  }

  /**
   * Remove all entities, clear pools and dispose all {@link EntitySystem} that implements {@link Disposable}
   */
  @Override
  public void dispose() {
    messages.clear();
    removeAllEntities();
    clearPools();

    for (EntitySystem system: this.getSystems()) {
      if (Disposable.class.isInstance(system)) {
        Disposable dis = (Disposable)system;
        dis.dispose();
      }
    }

    messages = null;
  }

  public void setTileSystem(TileSystem tileSystem) {
    this.tileSystem = tileSystem;
    addSystem(tileSystem);
  }

  public TileSystem getTileSystem() {
    return tileSystem;
  }

  public void setRenderingSystem(RenderingSystem renderingSystem) {
    addSystem(renderingSystem);
  }

  public void setCameraSystem(CameraSystem cameraSystem) {
    this.cameraSystem = cameraSystem;
    this.sceneOffsetSystem = new SceneOffsetSystem();
    addSystem(cameraSystem);
    addSystem(sceneOffsetSystem);
    addEntityListener(sceneOffsetSystem);
  }

  public void setCameraControllerSystem(CameraControllerSystem cameraControllerSystem) {
    this.cameraControllerSystem = cameraControllerSystem;
    this.addSystem(cameraControllerSystem);
  }

  public CameraControllerSystem getCameraControllerSystem() {
    return cameraControllerSystem;
  }

  @Override
  public void entityAdded(Entity entity) {
    if (Components.Camera.has(entity)) {
      setCameraEntity(entity);
    }
  }

  /**
   * Current camera entity
   * @return
   */
  public Entity getCameraEntity() {
    return cameraEntity;
  }

  private void setCameraEntity(Entity newEntity) {
    if (cameraEntity != null) {
      throw new RuntimeException("There is already one entity with CameraComponent!!!");
    }
    this.cameraEntity = newEntity;
  }

  @Override
  public void entityRemoved(Entity entity) {
    if (entity == cameraEntity) {
      cameraEntity = null;
    }
  }
}
