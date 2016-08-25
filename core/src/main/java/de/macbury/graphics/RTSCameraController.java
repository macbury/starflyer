package de.macbury.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 05.03.14.
 */
public class RTSCameraController implements Disposable {
  private static final float LERP_SPEED = 15.0f;
  public static final int MAX_ZOOM = 100;
  private static int CAMERA_MOVE_PADDING = 16;
  private final InputListener overlayInputListener;
  private Camera cam;

  private float currentZoom;
  private float maxZoom;
  private float minZoom;
  private int scrollAmount;
  private float scrollSpeed;

  private double rotationSpeed;
  private double rotation = -Math.PI;

  private double tiltSpeed;
  private double tilt = 1.20f;
  private float minTilt;
  private float maxTilt;

  private float sideSpeed;
  private float forwardSpeed;

  private Vector3 position;
  private Vector3 center;
  private Vector3 oldPosition;
  private Vector3 oldCenter;
  private Vector2 mouseRotationDrag;
  private Vector2 mouseDrag;

  private boolean tiltBackward;
  private boolean forwardPressed;
  private boolean backwardPressed;
  private boolean leftPressed;
  private boolean rightPressed;
  private boolean rotateLeftPressed;
  private boolean rotateRightPressed;
  private boolean tiltForward;

  private boolean leftHotCorent;
  private boolean rightHotCorent;
  private boolean topHotCorent;
  private boolean bottomHotCorent;
  private boolean keyboardEnabled = true;
  private boolean enabled = true;

  private Vector3 tempVec = new Vector3();
  private boolean rotateMouseButtonPressed;
  private boolean dragMouseButtonPressed;
  private float alpha;

  private RTSCameraListener listener;
  private Overlay overlay;
  private BoundingBox tempBoundingBox = new BoundingBox();

  public RTSCameraController() {
    super();
    position          = new Vector3(0,0,0);
    center            = new Vector3(0, 0, 0);
    oldPosition       = new Vector3(0,0,0);
    oldCenter         = new Vector3(0, 0, 0);
    mouseRotationDrag = new Vector2();
    mouseDrag         = new Vector2();

    sideSpeed = 35.0f;
    forwardSpeed = 30f;
    rotationSpeed = 2.0f;
    tiltSpeed = 1.0f;

    minZoom = 3;
    maxZoom = MAX_ZOOM;
    scrollSpeed = 200.0f;

    minTilt = 0.6f;
    maxTilt = (float) (Math.PI / 2) - 0.006f;

    currentZoom = 10;

    overlayInputListener = new InputListener() {
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        //blur();
      }

      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        //RTSCameraController.this.overlay.focus();
      }

      @Override
      public boolean scrolled(InputEvent event, float x, float y, int amount) {
        if (!enabled)
          return false;
        if (amount == 0 || rotateMouseButtonPressed) {
          return false;
        } else {
          scrollAmount = amount;
          return true;
        }
      }

      @Override
      public boolean keyDown(InputEvent event, int keycode) {
        if (!enabled || !keyboardEnabled)
          return false;
        return changePressStateFor(keycode, true);
      }

      @Override
      public boolean keyUp(InputEvent event, int keycode) {
        if (!enabled || !keyboardEnabled)
          return false;
        return changePressStateFor(keycode, false);
      }

      @Override
      public boolean mouseMoved(InputEvent event, float screenX, float screenY) {
        screenX = overlay.getOriginX() + screenX;
        screenY = overlay.getOriginY() + screenY;

        if (RTSCameraController.this.overlay.focused()) {
          leftHotCorent = (screenX <= CAMERA_MOVE_PADDING);
          rightHotCorent = (overlay.getWidth() - CAMERA_MOVE_PADDING <= screenX);
          topHotCorent = (overlay.getHeight() - CAMERA_MOVE_PADDING <= screenY);
          bottomHotCorent = (screenY <= CAMERA_MOVE_PADDING);

          return true;
        }
        return false;
      }

      @Override
      public boolean touchDown(InputEvent event, float screenX, float screenY, int pointer, int button) {
        if (!enabled)
          return false;
        RTSCameraController.this.overlay.focus();
        if (button == Input.Buttons.RIGHT) {
          dragMouseButtonPressed = true;
          //HashBot.ui.grabCursor();
          mouseDrag.set(Gdx.input.getX(), Gdx.input.getY());
          return true;
        } else if (button == Input.Buttons.MIDDLE) {
          rotateMouseButtonPressed = true;
          //HashBot.ui.grabCursor();
          mouseRotationDrag.set(Gdx.input.getX(), Gdx.input.getY());
          return true;
        } else {
          return false;
        }
      }

      @Override
      public void touchUp(InputEvent event, float screenX, float screenY, int pointer, int button) {
        if (!enabled)
          return;
        if (button == Input.Buttons.RIGHT) {
          dragMouseButtonPressed = false;
          return;
        } else if (button == Input.Buttons.MIDDLE) {
          rotateMouseButtonPressed = false;
          //HashBot.ui.normalCursor();
          return;
        } else {
          return;
        }
      }

    };
  }

  public void setOverlay(Overlay overlay) {
    this.overlay = overlay;
    this.overlay.addCaptureListener(overlayInputListener);

    overlay.focus();
  }

  public boolean isKeyboardEnabled() {
    return keyboardEnabled;
  }

  public void setKeyboardEnabled(boolean keyboardEnabled) {
    this.keyboardEnabled = keyboardEnabled;
  }

  public void setCamera(Camera camera) {
    this.cam = camera;
  }

  public void update(final float delta) {
    if (this.cam == null) {
      return;
    }
    cam.update();
    double offX = 0;
    double offY = 0;

    currentZoom += scrollAmount * delta * scrollSpeed;

    if (currentZoom > maxZoom)
      currentZoom = maxZoom;
    else if (currentZoom < minZoom)
      currentZoom = minZoom;

    if (leftPressed || leftHotCorent)
      offX += sideSpeed * delta;
    else if (rightPressed || rightHotCorent)
      offX -= sideSpeed * delta;

    if (forwardPressed || topHotCorent)
      offY -= forwardSpeed * delta;
    else if (backwardPressed || bottomHotCorent)
      offY += forwardSpeed * delta;

    if (rotateLeftPressed || rotateRightPressed) {
      if (rotateLeftPressed)
        rotation += delta * rotationSpeed;
      else
        rotation -= delta * rotationSpeed;
    }

    if (tiltForward || tiltBackward) {
      if (tiltForward)
        tilt += delta * tiltSpeed;
      else
        tilt -= delta * tiltSpeed;
    }

    if (rotateMouseButtonPressed) {
      tempVec.set(mouseRotationDrag.x, mouseRotationDrag.y, 0).sub(Gdx.input.getX(), Gdx.input.getY(), 0).nor();
      tilt += delta * -tempVec.y;
      rotation += delta * tempVec.x;
    }

    if (dragMouseButtonPressed) {
      //tempVec.set(mouseDrag.x, mouseDrag.y, 0).sub(Gdx.input.getX(), Gdx.input.getY(), 0).nor();

      //center.x += tempVec.x * delta * 25;
      //center.z += tempVec.y * delta * 25;
    }

    if (tilt > maxTilt)
      tilt = maxTilt;
    else if (tilt < minTilt)
      tilt = minTilt;

    center.x += offX * Math.cos(-rotation) + offY * Math.sin(rotation);
    center.y += offX * Math.sin(-rotation) + offY * Math.cos(rotation);

    position.x = center.x + (float) (currentZoom * Math.cos(tilt) * Math.sin(rotation));
    position.z = center.z + (float) (currentZoom * Math.sin(tilt));
    position.y = center.y + (float) (currentZoom * Math.cos(tilt) * Math.cos(rotation));


    if (listener != null) {
      position.z = Math.max(listener.getCameraElevation(this, position), position.z);
      listener.getCameraBounds(tempBoundingBox);
      if (!tempBoundingBox.contains(center)) {
        center.set(oldCenter);
      }
    }

    if (!position.equals(oldPosition)) {
      oldPosition.set(position);
      alpha = 0;
    }
    if (!center.equals(oldCenter)) {
      oldCenter.set(center);
      alpha = 0;
    }

    alpha += LERP_SPEED * delta;
    alpha = Math.min(alpha, 1);

    this.cam.position.set(position);
    tempVec.set(center).sub(position).nor();

    cam.direction.set(tempVec);
    scrollAmount = 0;

  }

  public Camera getCamera() {
    return cam;
  }

  public void setCenter(float x, float y) {
    this.position.set(x,y,0);
    this.center.x = x;
    this.center.y = y;
  }


  private boolean changePressStateFor(int keycode, boolean state) {
    boolean acted = false;
    if (!enabled) {
      return acted;
    }

    if (Input.Keys.Q == keycode) {
      rotateLeftPressed = state;
      acted = true;
    }

    /*if (input.isEqual(InputManager.Action.CameraRotateLeft, keycode)) {

    }

    if (input.isEqual(InputManager.Action.CameraRotateRight, keycode)) {
      rotateRightPressed = state;
      acted = true;
    }

    if (input.isEqual(InputManager.Action.CameraForward, keycode)) {
      forwardPressed = state;
      acted = true;
    }

    if (input.isEqual(InputManager.Action.CameraBackward, keycode)) {
      backwardPressed = state;
      acted = true;
    }

    if (input.isEqual(InputManager.Action.CameraLeft, keycode)) {
      leftPressed = state;
      acted = true;
    }

    if (input.isEqual(InputManager.Action.CameraRight, keycode)) {
      rightPressed = state;
      acted = true;
    }

    if (input.isEqual(InputManager.Action.CameraTiltBackward, keycode)) {
      tiltBackward = state;
      acted = true;
    }

    if (input.isEqual(InputManager.Action.CameraTiltForward, keycode)) {
      tiltForward = state;
      acted = true;
    }*/

    return acted;
  }



  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public float getZoom() {
    return currentZoom;
  }

  public void setZoom(float zoom) {
    this.currentZoom = zoom;
  }

  public void setRotation(float rotation) {
    this.rotation = rotation;
  }

  public void setTilt(float tilt) {
    this.tilt = tilt;
  }

  public Vector3 getCenter() {
    return center;
  }

  public float getCurrentZoom() {
    return currentZoom;
  }

  public void setCurrentZoom(float currentZoom) {
    this.currentZoom = currentZoom;
  }

  public void setCenter(Vector2 center) {
    this.setCenter(center.x, center.y);
  }

  public void setMaxZoom(float nz) {
    maxZoom = nz;
  }

  public RTSCameraListener getListener() {
    return listener;
  }

  public void setListener(RTSCameraListener listener) {
    this.listener = listener;
  }

  @Override
  public void dispose() {
    if (overlay != null)
      this.overlay.removeCaptureListener(overlayInputListener);
    overlay = null;
    listener  = null;
  }
}