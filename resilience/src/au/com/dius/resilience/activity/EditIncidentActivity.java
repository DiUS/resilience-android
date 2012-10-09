package au.com.dius.resilience.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import au.com.dius.resilience.R;

public class EditIncidentActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_incident);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_incident, menu);
        return true;
    }
}
