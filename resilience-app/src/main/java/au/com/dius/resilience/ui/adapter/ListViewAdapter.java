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
import au.com.dius.resilience.loader.ImageLoader;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.inject.Inject;
import roboguice.RoboGuice;

import java.util.List;

/**
 * @author georgepapas
 */
public class ListViewAdapter extends ArrayAdapter<ServiceRequest> {

  @Inject
  private ImageLoader imageLoader;

  public ListViewAdapter(Context context, int textViewResourceId, List<ServiceRequest> incidents) {
    super(context, textViewResourceId, incidents);
    RoboGuice.injectMembers(context, this);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(R.layout.service_request_list_view_item, null);

    ServiceRequest serviceRequest = getItem(position);

    TextView nameField = (TextView) rowView.findViewById(R.id.list_view_item_name);
    nameField.setText(serviceRequest.getDescription());

    TextView locationField = (TextView) rowView.findViewById(R.id.list_view_item_location);
    locationField.setText(formatAddress(serviceRequest));

    TextView reportedTime = (TextView) rowView.findViewById(R.id.list_view_item_time_reported);
    reportedTime.setText(DateUtils.getRelativeDateTimeString(
      getContext(),
      serviceRequest.getRequestedDatetime().getTime(),
      DateUtils.SECOND_IN_MILLIS,
      DateUtils.YEAR_IN_MILLIS, 0).toString().toUpperCase());

    ImageView previewImage = (ImageView) rowView.findViewById(R.id.list_view_preview_icon);
    imageLoader.loadThumbnailImage(previewImage, serviceRequest.getMediaUrl());

    return rowView;
  }

  private String formatAddress(ServiceRequest serviceRequest) {
    return serviceRequest.getAddress();
  }

  public void setData(List<ServiceRequest> incidents) {
    clear();
    if (incidents == null) {
      return;
    }

    addAll(incidents);
  }
}
