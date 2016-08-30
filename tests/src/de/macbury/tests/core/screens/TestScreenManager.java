package de.macbury.tests.core.screens;

import com.badlogic.gdx.Gdx;
import de.macbury.screens.AbstractScreen;
import de.macbury.screens.ScreenManager;
import de.macbury.tests.support.BaseGameTest;
import de.macbury.tests.support.DummyScreen;
import de.macbury.tests.support.GdxTestRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class TestScreenManager extends BaseGameTest {
  @Test
  public void setShouldPreloadAndCreateScreen() {
    AbstractScreen screen = mock(AbstractScreen.class);

    assertNull(game.screens.getCurrent());
    game.screens.set(screen);
    assertEquals(screen, game.screens.getCurrent());


    verify(screen).preload();
    verify(screen).create();
  }

  @Test
  public void pushingScreenShouldAlwaysCreateScreenOnce() {
    AbstractScreen screen = spy(new DummyScreen());
    game.screens.push(screen);
    game.screens.pop();
    game.screens.push(screen);

    verify(screen, times(1)).create();
    verify(screen, times(1)).preload();
  }

  @Test
  public void disposeScreenOnPopIfMarkedToDispose() {
    AbstractScreen screen = spy(new DummyScreen());
    when(screen.isDisposedAfterHide()).thenReturn(true);
    game.screens.push(screen);
    game.screens.pop();

    verify(screen, times(1)).dispose();
  }

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void throwExceptionOnAddingTheSameScreen() {
    exception.expect(ScreenManager.Exception.class);
    AbstractScreen screen = new DummyScreen();

    game.screens.push(screen);
    game.screens.push(screen);
  }

  @Test
  public void throwExceptionIfTriesToAddNull() {
    exception.expect(ScreenManager.Exception.class);
    game.screens.push(null);
  }

  @Test
  public void popScreenShouldUnlinkScreen() {
    AbstractScreen screen = mock(AbstractScreen.class);

    game.screens.set(screen);

    AbstractScreen popedScreen = game.screens.pop();
    assertEquals(screen, popedScreen);

    verify(screen).hide();
  }
}