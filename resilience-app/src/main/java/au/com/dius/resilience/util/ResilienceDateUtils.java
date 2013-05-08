package au.com.dius.resilience.util;

import android.content.Context;
import android.text.format.DateUtils;
import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import roboguice.inject.ContextSingleton;

import java.text.SimpleDateFormat;
import java.util.Date;

@ContextSingleton
public class ResilienceDateUtils {

  @Inject
  private Context context;

  private static final String FILENAME_FRIENDLY_DATE_FORMAT = "yyyyMMdd_HHmmss";

  private static final SimpleDateFormat sdf = new SimpleDateFormat(FILENAME_FRIENDLY_DATE_FORMAT);

  public String formatRelativeDate(Date date) {

    if (date == null) {
      return StringUtils.EMPTY;
    }

    return DateUtils.getRelativeDateTimeString(
      context,
      date.getTime(),
      DateUtils.SECOND_IN_MILLIS,
      DateUtils.YEAR_IN_MILLIS, 0).toString();
  }

  public String formatFilenameFriendly(Date date) {
    return sdf.format(date);
  }
}
