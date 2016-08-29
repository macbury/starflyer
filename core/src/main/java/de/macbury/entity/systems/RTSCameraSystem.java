package de.macbury.entity.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.entity.messages.MessageType;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.graphics.GeoPerspectiveCamera;
import de.macbury.graphics.Overlay;
import de.macbury.graphics.RTSCameraController;

/**
 * Allows user to move camera freely around the world
 */
public class RTSCameraSystem extends EntitySystem implements Disposable, Telegraph {
  private final RTSCameraController controller;
  private MessagesManager messages;

  public RTSCameraSystem(GeoPerspectiveCamera camera, MessagesManager messages) {
    this.controller = new RTSCameraController();
    this.controller.setCamera(camera);
    this.messages = messages;

    messages.addListener(this, MessageType.CenterCamera);
  }

  @Override
  public void update(float deltaTime) {
    this.controller.update(deltaTime);
  }

  @Override
  public void dispose() {
    this.controller.dispose();
    messages = null;
  }

  public Overlay getOverlay() {
    return controller.getOverlay();
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    if (MessageType.get(msg) == MessageType.CenterCamera) {
      controller.setCenter((Vector2) msg.extraInfo);
      return true;
    }
    return false;
  }
}
