package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Incident;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ViewIncidentActivity extends RoboActivity {

  @InjectView(R.id.incident_name)
  private TextView name;

  @InjectView(R.id.incident_note)
  private TextView note;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_incident);

    Incident incident = (Incident) getIntent().getSerializableExtra("incident");
    Log.d("VIEW INCIDENT", "incident retrieved with name " + incident.getName());

    name.setText(incident.getName());
    note.setText(incident.getNote());

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_view_incident, menu);
    return true;
  }
}