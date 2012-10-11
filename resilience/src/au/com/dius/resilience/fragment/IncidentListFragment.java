package au.com.dius.resilience.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.adapter.ListViewAdapter;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.IncidentFactory;

import java.util.ArrayList;
import java.util.List;

public class IncidentListFragment extends ListFragment {

  private static final String TAG = "IncidentListFrag";

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setListAdapter(new ListViewAdapter(getActivity(), R.layout.fragment_incident_list_view_item, getIncidents()));
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    StringBuffer logMsg = new StringBuffer("List item selected ")
            .append(" id ")
            .append(id)
            .append(" position ")
            .append(position);

    Log.d(TAG, logMsg.toString());
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "Resumed");
  }

  private List<Incident> getIncidents() {
    List<Incident> incidents = new ArrayList<Incident>();

    incidents.add(IncidentFactory.createIncident("Foo 1", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 2", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 3", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 4", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 5", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 6", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 7", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 8", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 9", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 10", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 11", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 12", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 13", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 14", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 15", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 16", System.currentTimeMillis(), "I'm a note"));
    incidents.add(IncidentFactory.createIncident("Foo 17", System.currentTimeMillis(), "I'm a note"));

    return incidents;
  }
}
