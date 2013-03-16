package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.Themer;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_create_incident)
public class CreateIncidentActivity extends RoboActivity {

  @Inject
  private Themer themer;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
}