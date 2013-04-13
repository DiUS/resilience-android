package au.com.dius.resilience.ui.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.ServiceListDefaults;
import au.com.justinb.open311.model.ServiceList;
import au.com.justinb.open311.model.ServiceRequest;

import java.util.List;

public class ServiceListSpinnerAdapter extends ArrayAdapter<ServiceList> {

  public ServiceListSpinnerAdapter(Context context) {
    super(context, R.layout.spinner_item);

    addAll(ServiceListDefaults.DEFAULT_SERVICES);
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
    View spinnerRow = inflater.inflate(R.layout.spinner_item, null);

    ServiceList serviceList = getItem(position);
    TextView textView = (TextView) spinnerRow.findViewById(R.id.spinner_item_text);

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
