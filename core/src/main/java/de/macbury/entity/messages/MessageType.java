package de.macbury.entity.messages;


import com.badlogic.gdx.ai.msg.Telegram;

public enum MessageType {
  CenterCamera;

  public static MessageType get(Telegram msg) {
    return MessageType.values()[msg.message];
  }
}
