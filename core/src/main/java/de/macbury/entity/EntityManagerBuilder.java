package de.macbury.entity;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.entity.systems.RTSCameraSystem;
import de.macbury.graphics.GeoPerspectiveCamera;

/**
 * Helps with building {@link EntityManager} instance
 */
public class EntityManagerBuilder {
  private GeoPerspectiveCamera camera;
  private boolean createRTSCamera;
  private MessagesManager messages;

  /**
   * Camera that will be used to display game entities
   * Required
   * @param camera
   * @return
   */
  public EntityManagerBuilder withRTSGameCamera(GeoPerspectiveCamera camera) {
    this.createRTSCamera = true;
    this.camera = camera;
    return this;
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
    if (createRTSCamera)
      manager.setRtsCameraSystem(new RTSCameraSystem(camera, messages));
    return manager;
  }

  /**
   * Validate if we have all ingredients to build new {@link EntityManager}
   */
  private void validate() {
    if (messages == null)
      throw new RuntimeException("MessageDispatcher is required!");
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
}
