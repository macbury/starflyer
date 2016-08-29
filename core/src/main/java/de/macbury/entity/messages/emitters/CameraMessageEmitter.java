package de.macbury.entity.messages.emitters;

import com.badlogic.gdx.math.Vector2;
import de.macbury.entity.messages.AbstractMessageEmitter;
import de.macbury.entity.messages.MessageType;
import de.macbury.entity.messages.MessagesManager;

/**
 * Handle messages that control camera
 */
public class CameraMessageEmitter extends AbstractMessageEmitter {
  public CameraMessageEmitter(MessagesManager messages) {
    super(messages);
  }

  /**
   * Center camera at location
   * Sends Telegram with {@link MessageType#CenterCamera} and payload passed vector
   * @param position world position
   */
  public void centerAt(Vector2 position) {
    messages.dispatchMessage(MessageType.CenterCamera.ordinal(), position);
  }
}
