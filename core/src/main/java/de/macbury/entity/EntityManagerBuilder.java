package de.macbury.entity;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.entity.systems.CameraSystem;
import de.macbury.entity.systems.RTSCameraSystem;
import de.macbury.entity.systems.RenderingSystem;
import de.macbury.entity.systems.TileSystem;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.tiles.TileCachePool;

/**
 * Helps with building {@link EntityManager} instance
 */
public class EntityManagerBuilder {
  private GeoPerspectiveCamera camera;
  private boolean createRTSCamera;
  private MessagesManager messages;
  private TileCachePool tileCachePool;
  private ModelBatch modelBatch;

  /**
   * Camera that will be used to display game entities
   * Required
   * @param camera
   * @return
   */
  public EntityManagerBuilder withRTSGameCamera(GeoPerspectiveCamera camera) {
    this.createRTSCamera = true;
    return withCamera(camera);
  }

  /**
   * Create {@link EntityManager} with passed options
   * @return
   */
  public EntityManager build() {
    validate();
    EntityManager manager = new EntityManager(
            messages
    );
    manager.setCameraSystem(new CameraSystem(camera));
    if (createRTSCamera)
      manager.setRtsCameraSystem(new RTSCameraSystem(camera, messages));
    if (tileCachePool != null)
      manager.setTileSystem(new TileSystem(tileCachePool, camera));

    manager.setRenderingSystem(new RenderingSystem(camera, modelBatch));
    return manager;
  }

  /**
   * Validate if we have all ingredients to build new {@link EntityManager}
   */
  private void validate() {
    if (camera == null)
      throw new RuntimeException("Camera is required!");
    if (messages == null)
      throw new RuntimeException("MessageDispatcher is required!");

    if (modelBatch == null)
      throw new RuntimeException("ModelBatch is required!");
  }

  /**
   * Used for communication between systems
   * @param messages
   * @return
   */
  public EntityManagerBuilder withMessageDispatcher(MessagesManager messages) {
    this.messages = messages;
    return this;
  }

  public EntityManagerBuilder withModelBatch(ModelBatch modelBatch) {
    this.modelBatch = modelBatch;
    return this;
  }

  public EntityManagerBuilder withTileCachePool(TileCachePool tileCachePool) {
    this.tileCachePool = tileCachePool;
    return this;
  }

  public EntityManagerBuilder withCamera(GeoPerspectiveCamera camera) {
    this.camera = camera;
    return this;
  }
}
