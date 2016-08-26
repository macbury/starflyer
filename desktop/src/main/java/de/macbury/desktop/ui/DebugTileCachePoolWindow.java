package de.macbury.desktop.ui;

import com.kotcrab.vis.ui.widget.VisWindow;
import de.macbury.ActionTimer;
import de.macbury.tiles.TileCachePool;

/**
 * Created by macbury on 26.08.16.
 */
public class DebugTileCachePoolWindow extends VisWindow implements ActionTimer.TimerListener {
  public DebugTileCachePoolWindow(TileCachePool tileCachePool) {
    super("Tile cache pool");
  }

  @Override
  public void onTimerTick(ActionTimer timer) {

  }

  public void toggle() {

  }
}
