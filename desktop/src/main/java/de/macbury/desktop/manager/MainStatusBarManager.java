package de.macbury.desktop.manager;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Configure main status bar and update its content
 */
public class MainStatusBarManager extends VisTable {

  public MainStatusBarManager() {
    super();
    defaults().pad(4, 0, 4, 3);
    setBackground(VisUI.getSkin().getDrawable("button-over"));

    add().expand();
    add(new VisLabel("Status bar"));
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }
}
