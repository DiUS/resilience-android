package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
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
public class CreateServiceRequestActivity extends RoboActivity {

  @Inject
  private ResilienceActionBarThemer themer;

  @InjectView(R.id.service_spinner)
  private Spinner serviceSpinner;

  private ServiceListSpinnerAdapter serviceListSpinnerAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupAdapter();
  }

  private void setupAdapter() {
    serviceListSpinnerAdapter = new ServiceListSpinnerAdapter(this);
    serviceListSpinnerAdapter.setData(ServiceListDefaults.DEFAULT_SERVICES);
    serviceSpinner.setAdapter(serviceListSpinnerAdapter);
  }
}