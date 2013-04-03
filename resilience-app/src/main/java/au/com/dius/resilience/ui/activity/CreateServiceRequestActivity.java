package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.ServiceListLoader;
import au.com.dius.resilience.ui.adapter.ServiceListSpinnerAdapter;
import au.com.justinb.open311.model.ServiceList;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.util.List;

@ContentView(R.layout.activity_create_service_request)
public class CreateServiceRequestActivity extends RoboActivity
                                          implements LoaderManager.LoaderCallbacks<List<ServiceList>>,
                                                     AdapterView.OnItemSelectedListener {

  @InjectView(R.id.service_spinner)
  private Spinner serviceSpinner;

  private ServiceListSpinnerAdapter serviceListSpinnerAdapter;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupAdapter();
    setupListeners();

    getLoaderManager().initLoader(0, null, this);
  }

  private void setupAdapter() {
    serviceListSpinnerAdapter = new ServiceListSpinnerAdapter(this, R.layout.service_list_spinner_item);
    serviceListSpinnerAdapter.setDropDownViewResource(R.layout.service_list_spinner_item);
    serviceSpinner.setAdapter(serviceListSpinnerAdapter);
  }

  private void setupListeners() {
    serviceSpinner.setOnItemSelectedListener(this);
  }

  @Override
  public Loader<List<ServiceList>> onCreateLoader(int id, Bundle args) {
    return new ServiceListLoader(this);
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceList>> loader, List<ServiceList> data) {
    serviceListSpinnerAdapter.setData(data);
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceList>> loader) {
    serviceListSpinnerAdapter.clear();
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    // TODO - plug this into something and test this shiznits
//    ServiceList item = serviceListSpinnerAdapter.getItem(position);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }
}