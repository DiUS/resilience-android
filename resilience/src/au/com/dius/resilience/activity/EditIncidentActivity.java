package au.com.dius.resilience.activity;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.IncidentFactory;
import au.com.dius.resilience.persistence.Repository;
import au.com.dius.resilience.persistence.RepositoryFactory;

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
      EditText incidentName = (EditText) findViewById(R.id.incident_name);
      EditText incidentNote = (EditText) findViewById(R.id.incident_note);
      
      Repository repository = RepositoryFactory.create(this);
      Incident incident = IncidentFactory.createIncident(incidentName.getText().toString(), Long.valueOf(new Date().getTime()), incidentNote.getText().toString());
      
      Log.d("EditIncidentActivity", "Saving incident: " + incident.toString());
      
      repository.save(incident);
    }
}
