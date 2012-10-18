package au.com.dius.resilience.ui.activity;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.ui.activity.ViewIncidentActivity;
import au.com.dius.resilience.ui.adapter.ListViewAdapter;
import com.google.inject.Inject;
import roboguice.activity.RoboListActivity;
import roboguice.fragment.RoboListFragment;

import java.util.Collections;

public class IncidentListActivity extends RoboListActivity implements RepositoryCommandResultListener<Incident> {

  @Inject
  private IncidentRepository incidentRepository;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setListAdapter(new ListViewAdapter(this, R.layout.fragment_incident_list_view_item, Collections.EMPTY_LIST));

    //should show loading view...
    loadIncidents();
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Incident incident = (Incident) getListAdapter().getItem(position);
    Intent viewIncident = new Intent(this, ViewIncidentActivity.class);
    viewIncident.putExtra("incident", incident);
    startActivityForResult(viewIncident, 0);
  }

  @Override
  public void onResume() {
    super.onResume();
    loadIncidents();
  }

  private void loadIncidents() {
    incidentRepository.findAll(this);
  }

  @Override
  public void commandComplete(final RepositoryCommandResult<Incident> result) {
    this.setListAdapter(
            new ListViewAdapter(this, R.layout.fragment_incident_list_view_item, result.getResults()));
  }

}
