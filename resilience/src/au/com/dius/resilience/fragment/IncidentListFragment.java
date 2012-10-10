package au.com.dius.resilience.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import au.com.dius.resilience.R;

public class IncidentListFragment extends ListFragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
 
    ListView listView = (ListView) super.onCreateView(inflater, container, savedInstanceState);
    setListAdapter(new ArrayAdapter<String>(getActivity(),  R.layout.incident_list_item, new String[]{"Foo", "Bar", "Haha"}));

    return listView;
  }

  
  
  
}
