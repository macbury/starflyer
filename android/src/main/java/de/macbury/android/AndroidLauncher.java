package de.macbury.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.macbury.Starflyer;
import de.macbury.android.gps.GPSTracker;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
  private GPSTracker gps;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.gps = new GPSTracker(this);
    AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
    configuration.hideStatusBar = false;
    initialize(new Starflyer(), configuration);
  }

  @Override
  protected void onPause() {
    super.onPause();
    gps.stopUsingGPS();
  }

  @Override
  protected void onResume() {
    super.onResume();
    gps.s
  }
}