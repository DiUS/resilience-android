package au.com.dius.resilience.ui.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Category;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;

import java.util.List;

/**
 * @author georgepapas
 */
public class ListViewAdapter extends ArrayAdapter<Incident> {

  public static final String DRAWABLE = "drawable";
  public static final String DARK_THEME_SUFFIX = "_white";
  private PreferenceAdapter preferenceAdapter;

  public ListViewAdapter(Context context, int textViewResourceId, List<Incident> incidents) {
    super(context, textViewResourceId, incidents);
    preferenceAdapter = new PreferenceAdapter(context);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(R.layout.incident_list_view_item, null);
    Incident incident = getItem(position);

    assignIcon(rowView, incident);

    TextView nameField = (TextView) rowView.findViewById(R.id.list_view_item_name);
    nameField.setText(incident.getName());

    TextView reportedTime = (TextView) rowView.findViewById(R.id.list_view_item_time_reported);
    reportedTime.setText(DateUtils.getRelativeDateTimeString(
            getContext(),
            incident.getDateCreated().longValue(),
            DateUtils.SECOND_IN_MILLIS,
            DateUtils.YEAR_IN_MILLIS, 0));

    return rowView;
  }

  private void assignIcon(View rowView, Incident incident) {
    ImageView categoryIcon = (ImageView) rowView.findViewById(R.id.list_view_category_icon);
    Category category = Category.asCategory(incident.getCategory());
    String categoryFilename = category.getImageFilename();

    Boolean isLightTheme = (Boolean) preferenceAdapter.getCommonPreference(R.string.use_light_theme_key);
    if (!isLightTheme) {
      categoryFilename = categoryFilename + DARK_THEME_SUFFIX;
    }

    int drawableId = rowView.getResources().getIdentifier(categoryFilename, DRAWABLE
      , rowView.getContext().getPackageName());

    if (drawableId <= 0) {
      drawableId = isLightTheme ? R.drawable.unknown : R.drawable.unknown_white;
    }

    categoryIcon.setImageResource(drawableId);
  }

  public void setData(List<Incident> incidents) {
    clear();
    if (incidents == null) {
      return;
    }

    addAll(incidents);
  }
}
