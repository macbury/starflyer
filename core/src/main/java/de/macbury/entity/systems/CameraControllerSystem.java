package de.macbury.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.entity.Components;
import de.macbury.entity.components.CameraComponent;
import de.macbury.entity.components.WorldPositionComponent;
import de.macbury.entity.messages.MessageType;
import de.macbury.entity.messages.MessagesManager;
import de.macbury.geo.MercatorProjection;
import de.macbury.geo.core.GeoPoint;
import de.macbury.graphics.GeoPerspectiveCamera;

/**
 * Handles user input in controlling the {@link de.macbury.entity.components.CameraComponent}
 */
public class CameraControllerSystem extends EntitySystem implements Disposable, GestureDetector.GestureListener, Telegraph {
  private static final String TAG = "CameraControllerSystem";
  private final Family family;
  private final Vector2 screenCenter;
  private final Vector2 currentTouch;
  private final Vector2 tempDir;
  private final Vector2 prevTouch;
  private final GestureDetector gestureDetector;
  private final Vector2 delta;
  private final GeoPerspectiveCamera camera;
  private final MessagesManager messages;
  private InputMultiplexer inputMultiplexer;
  private ImmutableArray<Entity> entities;
  private boolean dragging;
  private float startTouchRotation;
  private Entity targetCameraEntity;
  private float startCameraRotation;
  private boolean pinching;
  private float previousZoom;
  private float zoomFactor;

  public CameraControllerSystem(InputMultiplexer inputMultiplexer, GeoPerspectiveCamera camera, MessagesManager messages) {
    this.messages         = messages;
    this.camera           = camera;
    this.inputMultiplexer = inputMultiplexer;
    this.family = Family.all(CameraComponent.class).get();

    this.screenCenter = new Vector2();
    this.prevTouch = new Vector2();
    this.currentTouch = new Vector2();
    this.tempDir = new Vector2();
    this.delta = new Vector2();
    this.gestureDetector = new GestureDetector(this);

    inputMultiplexer.addProcessor(gestureDetector);

    messages.addListener(this, MessageType.CenterCamera);
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(family);
  }

  @Override
  public void removedFromEngine(Engine engine) {
    entities = null;
  }

  @Override
  public void update(float deltaTime) {
    screenCenter.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    this.targetCameraEntity = entities.get(0);

    if (entities.size() == 1) {
      CameraComponent cameraComponent = Components.Camera.get(targetCameraEntity);

      if (dragging) {
        float currentTouchRotation = getRotationRad(currentTouch);
        float targetRotation       = startCameraRotation + (startTouchRotation - currentTouchRotation);
        cameraComponent.rotation   = targetRotation;
      }

      if (pinching) {
        cameraComponent.zoom -= zoomFactor * deltaTime * cameraComponent.zoomSpeed;
      }
    }

    if (entities.size() > 1) {
      throw new RuntimeException("There are more than one Entity with CameraComponet");
    }
  }

  /**
   * Calculate rotation from touch to screen center
   * @param touch
   * @return
   */
  private float getRotationRad(Vector2 touch) {
    return tempDir.set(touch).sub(screenCenter).nor().angleRad();
  }

  @Override
  public void dispose() {
    messages.removeListener(this, MessageType.CenterCamera);
    inputMultiplexer.removeProcessor(gestureDetector);
    inputMultiplexer = null;
  }

  @Override
  public boolean touchDown(float x, float y, int pointer, int button) {
    return false;
  }

  @Override
  public boolean tap(float x, float y, int count, int button) {
    return false;
  }

  @Override
  public boolean longPress(float x, float y) {
    return false;
  }

  @Override
  public boolean fling(float velocityX, float velocityY, int button) {
    return false;
  }

  @Override
  public boolean pan(float x, float y, float deltaX, float deltaY) {
    if (targetCameraEntity != null) {
      currentTouch.set(x,y);
      if (!dragging) {
        startCameraRotation = Components.Camera.get(targetCameraEntity).rotation;
        startTouchRotation  = getRotationRad(currentTouch);
      }
      dragging = true;
    } else {
      dragging = false;
    }

    return dragging;
  }

  @Override
  public boolean panStop(float x, float y, int pointer, int button) {
    dragging = false;
    return dragging;
  }

  @Override
  public boolean zoom(float originalDistance, float currentDistance) {
    float newZoom = currentDistance - originalDistance;
    float amount  = newZoom - previousZoom;
    previousZoom  = newZoom;

    float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
    zoomFactor = amount / ((w > h) ? h : w);

    return pinching;
  }

  @Override
  public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
    this.pinching = true;
    return pinching;
  }

  @Override
  public void pinchStop() {
    pinching = false;
  }

  @Override
  public boolean handleMessage(Telegram msg) {
    if (targetCameraEntity == null)
      return false;
    switch (MessageType.get(msg)) {
      case CenterCamera:
        WorldPositionComponent worldPosition = Components.WorldPosition.get(targetCameraEntity);
        if (GeoPoint.class.isInstance(msg.extraInfo)) {
          GeoPoint centerPoint = (GeoPoint)msg.extraInfo;
          MercatorProjection.project(centerPoint, worldPosition);
        } else {
          throw new RuntimeException("Payload not supported: " + msg.extraInfo);
        }
        return true;
    }

    return false;
  }
}
