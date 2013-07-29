package au.com.dius.resilience.service;

import android.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class ProgressNotifier {

  private final NotificationManager notificationManager;
  private final NotificationCompat.Builder notificationBuilder;

  public ProgressNotifier(Context context, String title) {

    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationBuilder = new NotificationCompat.Builder(context);
    notificationBuilder.setContentTitle(title)
      .setSmallIcon(R.drawable.ic_menu_upload);
  }

  public void setProgress(int progress) {
    notificationBuilder.setProgress(100, progress, false);
    notificationManager.notify(0, notificationBuilder.build());
  }

  public void setText(String text) {
    notificationBuilder.setProgress(0, 100, true);
    notificationBuilder.setContentText(text);
    notificationManager.notify(0, notificationBuilder.build());
  }

  public void setFailureAction(PendingIntent intent) {
    notificationBuilder.setContentIntent(intent);
  }
}
