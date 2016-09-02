package de.macbury.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.CoreGame;

import java.util.Stack;

/**
 * Manages in game screenStack.
 */
public class ScreenManager implements Disposable {
  protected final CoreGame game;

  public class Exception extends RuntimeException {
    public Exception(String s) {
      super(s);
    }
  };

  private static final String TAG = "ScreenManager";
  /**
   * List of screenStack
   */
  private Stack<AbstractScreen> screenStack;

  public ScreenManager(CoreGame game) {
    this.game   = game;
    screenStack = new Stack<AbstractScreen>();
  }

  /**
   * Reference to current screen
   * @return
   */
  public AbstractScreen getCurrent() {
    if (screenStack.empty()) {
      return null;
    }
    return screenStack.peek();
  }

  /**
   * Hides screen, unlinks it dependency, and dispose if {@link AbstractScreen#isDisposedAfterHide()} is true
   * @param screen
   */
  private void hide(AbstractScreen screen) {
    screen.hide();
    if (screen.isDisposedAfterHide()) {
      screen.dispose();
      screenStack.remove(screen);
      Gdx.app.debug(TAG, "Hided and disposed screen: " + screen.toString());
    } else {
      Gdx.app.debug(TAG, "Hide " + screen.toString());
    }
    //screen.unlink();
  }

  /**
   * Removes screen from stack and replace it with nextScreen
   * @param nextScreen
   */
  public void set(AbstractScreen nextScreen) {
    pop();
    push(nextScreen);
  }

  /**
   * Removes current screen from stack
   * @return current screen or null
   */
  public AbstractScreen pop() {
    AbstractScreen currentScreen = getCurrent();
    if (currentScreen != null) {
      Gdx.app.debug(TAG, "Removed old screen from stack " + currentScreen.toString());
      hide(screenStack.pop());
    }
    return currentScreen;
  }

  /**
   * Adds next screen to stack, and hides old one
   * @param nextScreen
   */
  public void push(AbstractScreen nextScreen) {
    if (getCurrent() != null) {
      hide(getCurrent());
    }

    if (screenStack.contains(nextScreen)) {
      throw new Exception("Cannot add the same screen twice!");
    }

    if (nextScreen == null) {
      throw new Exception("Passed null screen");
    }

    //nextScreen.link(game);
    screenStack.push(nextScreen);
    if (!nextScreen.isCreated()) {
      Gdx.app.debug(TAG, "Preload and create screen " + nextScreen.toString());
      nextScreen.preload();
      //assets.finishLoading(); //TODO show loading screen
      nextScreen.create();
      nextScreen.setCreated(true);
    } else {
      Gdx.app.debug(TAG, "Show screen " + nextScreen.toString());
    }
    nextScreen.show();
    nextScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  /**
   * Update size of current screen
   * @param width
   * @param height
   */
  public void resize(int width, int height) {
    if (haveCurrentScreen()) {
      getCurrent().resize(width, height);
    }
  }

  /**
   * Update and render current screen
   * @param delta
   */
  public void tick(float delta) {
    if (haveCurrentScreen()) {
      AbstractScreen screen = getCurrent();
      screen.render();
    }
  }

  private boolean haveCurrentScreen() {
    return getCurrent() != null;
  }

  /**
   * Pause current screen
   */
  public void pause() {
    if (haveCurrentScreen()) {
      getCurrent().pause();
    }
  }

  /**
   * Resume current screen
   */
  public void resume() {
    if (haveCurrentScreen()) {
      getCurrent().resume();
    }
  }

  @Override
  public void dispose() {
    while (!screenStack.empty()) {
      screenStack.pop().dispose();
    }
  }
}