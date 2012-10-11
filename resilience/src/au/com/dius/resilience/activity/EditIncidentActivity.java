package au.com.dius.resilience.activity;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.IncidentFactory;
import au.com.dius.resilience.persistence.Repository;
import au.com.dius.resilience.persistence.RepositoryFactory;

public class EditIncidentActivity extends Activity implements OnSeekBarChangeListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_incident);

    Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
    ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
        R.array.categories, android.R.layout.simple_spinner_item);
    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(categoryAdapter);
    
    Spinner subCategorySpinner = (Spinner) findViewById(R.id.sub_category_spinner);
    ArrayAdapter<CharSequence> subCategoryadapter = ArrayAdapter.createFromResource(this,
        R.array.subcategories, android.R.layout.simple_spinner_item);
    subCategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    subCategorySpinner.setAdapter(subCategoryadapter);
    
    SeekBar impactScale = (SeekBar) findViewById(R.id.impact_scale);
    impactScale.setOnSeekBarChangeListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_edit_incident, menu);
    return true;
  }

  public void onSubmitClick(View button) {
    String incidentNote = ((EditText) findViewById(R.id.notes)).getText().toString();

    String category = ((Spinner) findViewById(R.id.category_spinner)).getSelectedItem().toString();
    String subCategory = ((Spinner) findViewById(R.id.sub_category_spinner)).getSelectedItem().toString();
    ImpactScale impact = ImpactScale.fromCode(((SeekBar) findViewById(R.id.impact_scale)).getProgress());
    
    Repository repository = RepositoryFactory.create(this);
    Incident incident = IncidentFactory.createIncident(category, Long.valueOf(new Date().getTime()), incidentNote, category, subCategory, impact);

    Log.d(getClass().getName(), "Saving incident: " + incident.toString());
    repository.save(incident);
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    ImpactScale scale = ImpactScale.fromCode(progress);
    
    Log.d(getClass().getName(), "scale = " + scale);
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    
  }
}
