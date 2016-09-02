package de.macbury.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.entity.Components;
import de.macbury.entity.components.CameraComponent;
import de.macbury.graphics.GeoPerspectiveCamera;

/**
 * Handles user input in controlling the {@link de.macbury.entity.components.CameraComponent}
 */
public class CameraControllerSystem extends EntitySystem implements Disposable, GestureDetector.GestureListener {
  private static final String TAG = "CameraControllerSystem";
  private final Family family;
  private final Vector2 screenCenter;
  private final Vector2 currentTouch;
  private final Vector2 tempDir;
  private final Vector2 prevTouch;
  private final GestureDetector gestureDetector;
  private final Vector2 delta;
  private final GeoPerspectiveCamera camera;
  private InputMultiplexer inputMultiplexer;
  private ImmutableArray<Entity> entities;
  private boolean dragging;
  private float startTouchRotation;
  private Entity targetCameraEntity;
  private float startCameraRotation;

  public CameraControllerSystem(InputMultiplexer inputMultiplexer, GeoPerspectiveCamera camera) {
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
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(family);
  }

  @Override
  public void removedFromEngine(Engine engine) {
    entities = null;
  }

  private Vector3 temp = new Vector3();

  @Override
  public void update(float deltaTime) {
    screenCenter.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    this.targetCameraEntity = entities.get(0);

    if (entities.size() == 1) {
      CameraComponent cameraComponent = Components.Camera.get(targetCameraEntity);

      if (dragging) {
        float currentTouchRotation    = getRotationRad(currentTouch);
        cameraComponent.rotation = startCameraRotation + (startTouchRotation - currentTouchRotation);
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
  public boolean zoom(float initialDistance, float distance) {
    return false;
  }

  @Override
  public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
    return false;
  }

  @Override
  public void pinchStop() {

  }
}
