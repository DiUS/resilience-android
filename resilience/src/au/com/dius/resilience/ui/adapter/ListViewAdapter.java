package au.com.dius.resilience.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Incident;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;

/**
 * @author georgepapas
 */
public class ListViewAdapter extends ArrayAdapter<Incident> {

  private List<Incident> incidents;

  public ListViewAdapter(Context context, int textViewResourceId, List<Incident> incidents) {
    super(context, textViewResourceId, incidents);
    this.incidents = incidents;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(R.layout.fragment_incident_list_view_item, null);

    Incident incident = incidents.get(position);

    TextView nameField = (TextView) rowView.findViewById(R.id.list_view_item_name);
    nameField.setText(incident.getName());

    TextView reportedTime = (TextView) rowView.findViewById(R.id.list_view_item_time_reported);
    reportedTime.setText(getFriendlyDate(new DateTime(incident.getDateCreated())));

    return rowView;
  }

  private String getFriendlyDate(DateTime dateCreated) {
    DateTime now = new DateTime();
    Period period = new Period(now, dateCreated);

    return "some time ago...";
  }

}
