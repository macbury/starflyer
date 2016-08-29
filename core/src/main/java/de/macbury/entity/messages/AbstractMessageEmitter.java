package de.macbury.entity.messages;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;

/**
 * Base class for emitters of messages
 */
public abstract class AbstractMessageEmitter {
  protected final MessagesManager messages;

  public AbstractMessageEmitter(MessagesManager messages) {
    this.messages = messages;
  }
}
