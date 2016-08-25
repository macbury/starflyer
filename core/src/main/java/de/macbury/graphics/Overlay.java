package de.macbury.graphics;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class Overlay extends Widget {
  public Overlay() {
    super();
    setFillParent(false);
    setZIndex(2);
  }

  private void blur() {
    Stage s = getStage();
    if (s != null) {
      s.unfocus(this);
    }
  }


  public boolean focused() {
    return (getStage() != null) && (getStage().getKeyboardFocus() == this);
  }

  public void focus() {
    if (getStage() != null) {
      getStage().setKeyboardFocus(this);
      getStage().setScrollFocus(this);
    }
  }
}