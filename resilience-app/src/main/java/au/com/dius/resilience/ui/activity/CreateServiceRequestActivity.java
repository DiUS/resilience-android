package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.ServiceListDefaults;
import au.com.dius.resilience.ui.ResilienceActionBarThemer;
import au.com.dius.resilience.ui.adapter.ServiceListSpinnerAdapter;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_create_service_request)
public class CreateServiceRequestActivity extends RoboActivity
                                          implements AdapterView.OnItemSelectedListener {

  @Inject
  private ResilienceActionBarThemer themer;

  @InjectView(R.id.service_spinner)
  private Spinner serviceSpinner;

  private ServiceListSpinnerAdapter serviceListSpinnerAdapter;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupAdapter();
    setupListeners();
  }

  private void setupAdapter() {
    serviceListSpinnerAdapter = new ServiceListSpinnerAdapter(this);
    serviceListSpinnerAdapter.setData(ServiceListDefaults.DEFAULT_SERVICES);
    serviceSpinner.setAdapter(serviceListSpinnerAdapter);
  }

  private void setupListeners() {
    serviceSpinner.setOnItemSelectedListener(this);
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