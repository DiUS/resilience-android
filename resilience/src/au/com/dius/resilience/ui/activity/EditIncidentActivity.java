package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.IncidentFactory;
import au.com.dius.resilience.persistence.RepositoryCommandResult;
import au.com.dius.resilience.persistence.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.RepositoryCommands;
import au.com.dius.resilience.persistence.RepositoryFactory;
import au.com.dius.resilience.persistence.async.BackgroundDataOperation;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.Date;

public class EditIncidentActivity extends RoboActivity implements OnSeekBarChangeListener, RepositoryCommandResultListener<Incident> {

  @InjectView(R.id.category_spinner)
  private Spinner categorySpinner;

  @InjectView(R.id.sub_category_spinner)
  private Spinner subCategorySpinner;

  @InjectView(R.id.impact_scale)
  private SeekBar impactScale;

  @InjectView(R.id.notes)
  private EditText notes;

  @Inject
  private RepositoryCommands repositoryCommands;

  @Inject
  private RepositoryFactory repositoryFactory;

  @Inject
  private IncidentFactory incidentFactory;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_incident);
    initialiseSpinners();
    
    impactScale.setOnSeekBarChangeListener(this);
    

    // FIXME - test this (-xxx-camera args not taking effect on my emulator)
    PackageManager pm = getPackageManager();
    boolean deviceHasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    Button cameraButton = (Button) findViewById(R.id.submit_photo);
    cameraButton.setEnabled(deviceHasCamera);
  }

  private void initialiseSpinners() {
    ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
        R.array.categories, android.R.layout.simple_spinner_item);
    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(categoryAdapter);
    
    ArrayAdapter<CharSequence> subCategoryadapter = ArrayAdapter.createFromResource(this,
        R.array.subcategories, android.R.layout.simple_spinner_item);
    subCategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    subCategorySpinner.setAdapter(subCategoryadapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_edit_incident, menu);
    updateImpactLabel(ImpactScale.LOW);
    return true;
  }

  public void onSubmitClick(View button) {
    String incidentNote = notes.getText().toString();

    String category = categorySpinner.getSelectedItem().toString();
    String subCategory = subCategorySpinner.getSelectedItem().toString();
    ImpactScale impact = ImpactScale.fromCode(impactScale.getProgress());
    
    Incident incident = incidentFactory.createIncident(
            category, Long.valueOf(new Date().getTime()), incidentNote, category, subCategory, impact);

    new BackgroundDataOperation<Incident>().execute(
            this,
            repositoryCommands.save(repositoryFactory.createIncidentRepository(this), incident));

    Log.d(getClass().getName(), "Saving incident: " + incident.toString());
  }
  
  public void onCameraClick(View button) {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    ImpactScale scale = ImpactScale.fromCode(progress);
    updateImpactLabel(scale);
  }

  private void updateImpactLabel(ImpactScale scale) {
    TextView impactDescription = (TextView) findViewById(R.id.impact_scale_desc);
    impactDescription.setText(scale.name());
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    
  }

  @Override
  public void commandComplete(RepositoryCommandResult<Incident> result) {
    Toast.makeText(this, "Incident was " + (result.isSuccess() ? " saved" : " not saved"), Toast.LENGTH_SHORT).show();
  }
}
