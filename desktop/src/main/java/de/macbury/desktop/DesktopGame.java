package de.macbury.desktop;

import com.kotcrab.vis.ui.VisUI;
import de.macbury.CoreGame;
import de.macbury.desktop.screens.TestOcculsionScreen;

/**
 * Explore game content
 */
public class DesktopGame extends CoreGame {
  private static final String TAG = "DesktopGame";

  @Override
  public void create() {
    super.create();
    VisUI.load(VisUI.SkinScale.X1);
    this.screens.set(new TestOcculsionScreen());
    //screens.set(new TestCameraScreen());
  }

  @Override
  public void dispose () {
    VisUI.dispose();
    super.dispose();
  }
}
