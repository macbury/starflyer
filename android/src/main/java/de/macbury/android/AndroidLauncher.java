package de.macbury.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.macbury.CoreGame;
import de.macbury.android.gps.GPSTracker;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
  private GPSTracker gps;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
    configuration.hideStatusBar = false;
    initialize(new MobileGame(), configuration);
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }
}