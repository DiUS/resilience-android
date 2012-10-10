package au.com.dius.resilience.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import au.com.dius.resilience.R;

public class IncidentListFragment extends ListFragment {

  private static final String TAG = "IncidentListFrag";

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setListAdapter(ArrayAdapter.createFromResource(
            getActivity(),
            R.array.incident_list_name,
            R.layout.fragment_incident_list_view_item));
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
}
