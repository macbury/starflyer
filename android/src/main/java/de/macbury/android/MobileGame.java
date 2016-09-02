package de.macbury.android;

import com.kotcrab.vis.ui.VisUI;
import de.macbury.CoreGame;
import de.macbury.screens.TestCameraScreen;

/**
 * Created by macbury on 02.09.16.
 */
public class MobileGame extends CoreGame {
  @Override
  public void create() {
    super.create();
    VisUI.load();
    screens.set(new TestCameraScreen());
  }
}
