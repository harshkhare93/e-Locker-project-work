package harshkhare.e_locker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.SyncStateContract;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Hi ! HARSH on 09-Apr-17.
 */

public class AlarmReceiver extends BroadcastReceiver implements TextToSpeech.OnInitListener {
    final public static String ONE_TIME = "trainedge.cashtrack.onetime";
    final public static String REPEATING = "trainedge.cashtrack.reapeating";
    private SharedPreferences pref;
    private TextToSpeech engine;
    private String msg;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
            //Make sure this intent has been sent by the one-time timer button.
            msgStr.append("Add your Documents ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));
        msg = "Time to add some Documents for " + msgStr;
        NewMessageNotification.notify(context, msg, 0);
        pref = context.getSharedPreferences(Constants.SETTING_PREF, MODE_PRIVATE);
        boolean isTTSenabled = pref.getBoolean(Constants.KEY_TTS, true);

        if (isTTSenabled) {
            engine = new TextToSpeech(context, this);
        }
        //Release the lock
        wl.release();
    }

    public void SetAlarm(Context context, int hourOfDay, int minute) {
        Calendar calSet = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            calSet.set(Calendar.DAY_OF_WEEK, i);
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, new Intent("trainedge.cashtrack.MY_ALARM"), PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
            Log.i("SERVICE", "alarm set" + i);
        }
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, Notificationsetting.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, Notificationsetting.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.ENGLISH);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                engine.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                engine.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }
}