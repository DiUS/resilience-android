package au.com.dius.resilience.fragment;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.RuntimeProperties;
import au.com.dius.resilience.adapter.ListViewAdapter;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.ParseRepository;
import au.com.dius.resilience.persistence.Repository;
import au.com.dius.resilience.persistence.RepositoryFactory;
import au.com.dius.resilience.persistence.SqlLiteRepository;

import java.util.Collections;
import java.util.List;

public class IncidentListFragment extends ListFragment {

  private static final String TAG = "IncidentListFrag";

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    super.setListAdapter(new ListViewAdapter(getActivity(), R.layout.fragment_incident_list_view_item, Collections.EMPTY_LIST));
    //should show loading view...
    loadIncidents();
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

  private void loadIncidents() {

    Log.d("frag", "load incidents");

    final ListFragment fragment = this;
    final DataListener<Incident> dataListener = new DataListener<Incident>() {
      @Override
      public void listLoaded(final List<Incident> items) {
        fragment.getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            fragment.setListAdapter(new ListViewAdapter(getActivity(), R.layout.fragment_incident_list_view_item, items));
          }
        });
      }

      @Override
      public void itemSaved() {
      }

      @Override
      public void itemLoaded(Incident item) {
      }
    };


    AsyncTask<DataListener<Incident>, Integer, List<Incident>> backgroundDataLoader = new AsyncTask<DataListener<Incident>, Integer, List<Incident>>(){

      DataListener<Incident>[] dataListeners = null;

      @Override
      protected List<Incident> doInBackground(DataListener<Incident>... dataListeners) {
        Log.d("async", "do in background");
        this.dataListeners = dataListeners;
        return RepositoryFactory.create(getActivity()).findAll();
      }

      @Override
      protected void onPostExecute(List<Incident> items) {
        Log.d("async", "post execute method");
        for (DataListener<Incident> listener : dataListeners) {
          Log.d("async", "calling listener");
          listener.listLoaded(items);
        }
      }
    };

    backgroundDataLoader.execute(dataListener);
  }

  public interface DataListener<T> {
    void listLoaded(List<T> items);
    void itemSaved();
    void itemLoaded(T item);
  }
}
