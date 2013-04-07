package au.com.dius.resilience.util;

import android.content.Context;
import android.text.format.DateUtils;
import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import roboguice.inject.ContextSingleton;

import java.util.Date;

@ContextSingleton
public class ResilienceDateUtils {

  @Inject
  private Context context;

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
}
