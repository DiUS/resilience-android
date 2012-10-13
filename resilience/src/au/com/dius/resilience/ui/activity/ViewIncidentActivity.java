package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import au.com.dius.resilience.R;
import roboguice.activity.RoboActivity;

public class ViewIncidentActivity extends RoboActivity {
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incident);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_incident, menu);
        return true;
    }
}