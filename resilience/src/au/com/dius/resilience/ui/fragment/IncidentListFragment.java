package au.com.dius.resilience.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.RepositoryCommandResult;
import au.com.dius.resilience.persistence.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.RepositoryCommands;
import au.com.dius.resilience.persistence.RepositoryFactory;
import au.com.dius.resilience.persistence.async.BackgroundDataOperation;
import au.com.dius.resilience.ui.activity.ViewIncidentActivity;
import au.com.dius.resilience.ui.adapter.ListViewAdapter;
import com.google.inject.Inject;
import roboguice.fragment.RoboListFragment;

import java.util.Collections;

public class IncidentListFragment extends RoboListFragment implements RepositoryCommandResultListener<Incident> {

  @Inject
  private RepositoryFactory repositoryFactory;

  @Inject
  private RepositoryCommands repositoryCommands;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    super.setListAdapter(new ListViewAdapter(getActivity(), R.layout.fragment_incident_list_view_item, Collections.EMPTY_LIST));

    //should show loading view...
    loadIncidents();
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Incident incident = (Incident) getListAdapter().getItem(position);
    Intent viewIncident = new Intent(getActivity(), ViewIncidentActivity.class);
    viewIncident.putExtra("incident", incident);
    startActivityForResult(viewIncident, 0);
  }

  @Override
  public void onResume() {
    super.onResume();
    loadIncidents();
  }

  private void loadIncidents() {
    new BackgroundDataOperation<Incident>().execute(
            this,
            repositoryCommands.findAll(repositoryFactory.createIncidentRepository(getActivity())));
  }

  @Override
  public void commandComplete(final RepositoryCommandResult<Incident> result) {
    this.setListAdapter(
            new ListViewAdapter(getActivity(), R.layout.fragment_incident_list_view_item, result.getResults()));
  }

}
