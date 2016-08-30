package de.macbury.desktop;

import com.kotcrab.vis.ui.VisUI;
import de.macbury.Starflyer;
import de.macbury.desktop.screens.TestCameraScreen;
import de.macbury.desktop.screens.WorldExploreScreen;

/**
 * Explore game content
 */
public class GameExplorer extends Starflyer {
  private static final String TAG = "GameExplorer";

  @Override
  public void create() {
    super.create();
    VisUI.load(VisUI.SkinScale.X1);
    //this.screens.set(new WorldExploreScreen());
    screens.set(new TestCameraScreen());
  }

  @Override
  public void dispose () {
    VisUI.dispose();
    super.dispose();
  }
}
