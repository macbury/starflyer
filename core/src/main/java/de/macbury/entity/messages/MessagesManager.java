package de.macbury.entity.messages;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegraph;
import de.macbury.entity.messages.emitters.CameraMessageEmitter;

/**
 * Handle creating and receiving messages in game
 */
public class MessagesManager extends MessageDispatcher {

  public final CameraMessageEmitter camera;

  public MessagesManager() {
    super();
    this.camera = new CameraMessageEmitter(this);
  }

  public void addListener(Telegraph listener, MessageType ...types) {
    for (MessageType type: types) {
      addListener(listener, type.ordinal());
    }
  }

  public void removeListener(Telegraph listener, MessageType ...types) {
    for (MessageType type: types) {
      removeListener(listener, type.ordinal());
    }
  }
}
