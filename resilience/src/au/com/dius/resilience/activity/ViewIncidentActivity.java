package au.com.dius.resilience.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import au.com.dius.resilience.R;

public class ViewIncidentActivity extends Activity {
  
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