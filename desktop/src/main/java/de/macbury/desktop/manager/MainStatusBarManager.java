package de.macbury.desktop.manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import de.macbury.ActionTimer;
import de.macbury.graphics.GeoPerspectiveCamera;

/**
 * Configure main status bar and update its content
 */
public class MainStatusBarManager extends VisTable implements ActionTimer.TimerListener{
  private final VisLabel cameraPositionXLabel;
  private final VisLabel cameraPositionYLabel;
  private final VisLabel cameraPositionZLabel;
  private final VisLabel cameraLatLabel;
  private final VisLabel cameraLngLabel;
  private GeoPerspectiveCamera spyCamera;
  private final ActionTimer actionTimer;

  public MainStatusBarManager() {
    super();
    this.actionTimer = new ActionTimer(1f, this);
    actionTimer.start();
    defaults().pad(4, 0, 4, 3);
    setBackground(VisUI.getSkin().getDrawable("button-over"));

    this.cameraPositionXLabel = new VisLabel();
    cameraPositionXLabel.setColor(Color.ORANGE);
    this.cameraPositionYLabel = new VisLabel();
    cameraPositionYLabel.setColor(Color.GREEN);
    this.cameraPositionZLabel = new VisLabel();
    cameraPositionZLabel.setColor(Color.BLUE);

    this.cameraLatLabel = new VisLabel();
    this.cameraLngLabel = new VisLabel();

    add(new VisLabel("Camera: "));

    int posLabelWidth = 100;
    int latLngLabelWidth = 160;

    add(cameraPositionXLabel).width(posLabelWidth).center();
    add(new VisLabel("x"));
    add(cameraPositionYLabel).width(posLabelWidth).center();
    add(new VisLabel("x"));
    add(cameraPositionZLabel).width(posLabelWidth).center();
    add(new VisLabel("|")).center();
    add(new VisLabel("Lat: "));
    add(cameraLatLabel).width(latLngLabelWidth).center();
    add(new VisLabel("Lng: "));
    add(cameraLngLabel).width(latLngLabelWidth).center();
    add().expand();
    add(new VisLabel("Status bar"));
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    if (spyCamera != null) {
      cameraPositionXLabel.setText(String.valueOf(spyCamera.position.x));
      cameraPositionYLabel.setText(String.valueOf(spyCamera.position.y));
      cameraPositionZLabel.setText(String.valueOf(spyCamera.position.z));

      cameraLatLabel.setText(String.valueOf(spyCamera.geoPosition.lat));
      cameraLngLabel.setText(String.valueOf(spyCamera.geoPosition.lng));
    }
  }

  @Override
  public void onTimerTick(ActionTimer timer) {

  }

  public GeoPerspectiveCamera getSpyCamera() {
    return spyCamera;
  }

  public void setSpyCamera(GeoPerspectiveCamera spyCamera) {
    this.spyCamera = spyCamera;
  }
}
