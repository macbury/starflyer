package de.macbury.entity;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.entity.systems.CameraSystem;
import de.macbury.entity.systems.RTSCameraSystem;
import de.macbury.entity.systems.RenderingSystem;
import de.macbury.entity.systems.TileSystem;

/**
 * Manage all {@link com.badlogic.ashley.core.Entity} in game
 */
public class EntityManager extends PooledEngine implements Disposable {
  private MessagesManager messages;
  private RTSCameraSystem rtsCameraSystem;
  private TileSystem tileSystem;
  private CameraSystem cameraSystem;

  public EntityManager(MessagesManager messages) {
    super();
    this.messages = messages;
  }

  public RTSCameraSystem getRtsCameraSystem() {
    return rtsCameraSystem;
  }

  public void setRtsCameraSystem(RTSCameraSystem rtsCameraSystem) {
    this.rtsCameraSystem = rtsCameraSystem;
    addSystem(rtsCameraSystem);
  }

  /**
   * Update {@link MessageDispatcher} and {@link EntitySystem}s
   * @param deltaTime
   */
  @Override
  public void update(float deltaTime) {
    messages.update();
    super.update(deltaTime);
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
    addEntityListener(renderingSystem);
  }

  public void setCameraSystem(CameraSystem cameraSystem) {
    this.cameraSystem = cameraSystem;
    addSystem(cameraSystem);
  }
}
