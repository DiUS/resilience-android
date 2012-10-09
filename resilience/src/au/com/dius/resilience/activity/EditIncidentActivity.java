package au.com.dius.resilience.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
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
    
    public void onSubmitClick(View button) {
      EditText incidentNote = (EditText) findViewById(R.id.incident_note);
      System.out.println("Note contains text: \"" + incidentNote.getText().toString()
          + "\"");
    }
}
