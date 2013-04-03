package au.com.dius.resilience.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.justinb.open311.model.ServiceList;

import java.util.List;

public class ServiceListSpinnerAdapter extends ArrayAdapter<ServiceList> {

  public ServiceListSpinnerAdapter(Context context, int textViewResourceId) {
    super(context, textViewResourceId);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return createServiceListItemView(position);
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    return createServiceListItemView(position);
  }

  private View createServiceListItemView(int position) {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View spinnerRow = inflater.inflate(R.layout.service_list_spinner_item, null);

    ServiceList serviceList = getItem(position);
    TextView textView = (TextView) spinnerRow.findViewById(R.id.service_list_spinner_item_label);

    textView.setText(serviceList.getServiceName());
    return spinnerRow;
  }

  public void setData(List<ServiceList> serviceLists) {
    clear();
    if (serviceLists == null) {
      return;
    }

    addAll(serviceLists);
  }
}
