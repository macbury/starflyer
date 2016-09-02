package de.macbury.tests.support;

import de.macbury.CoreGame;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public abstract class BaseGameTest {
  protected CoreGame game;

  @Before
  public void configureRPGGame() {
    this.game = new CoreGame();
    game.create();
  }

  @After
  public void disposeRPGGame() {
    game.dispose();
    game = null;
  }
}
