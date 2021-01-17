package com.example.myapplication.Service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.DTO.DeviceDataDTO;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Tasks.MainTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BackService extends Service {

    private Thread mainThread;
    public static Intent serviceIntent = null;
    String device_id;
    final String TAG = "BackService";

    public BackService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        device_id = pref.getString("device_id", null);
        Log.d(TAG, "BackService: " + device_id);

        serviceIntent = intent;
        showToast(getApplication(), "Start Service");

        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {

                SimpleDateFormat sdf = new SimpleDateFormat("aa hh:mm");
                boolean run = true;

                while (run) {

                    try {

                        //해당 아이디를 넘겨야함
                        MainTask networkMainTask = new MainTask(handler);
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("device_id", device_id);

                        networkMainTask.execute(params);

//                        Date date = new Date();
//                        sendNotification(sdf.format(date));
                        Thread.sleep(1000 * 3600);
                    } catch (InterruptedException e) {
                        run = false;
                        Log.d(TAG, "run: " + e.getMessage());
                        e.printStackTrace();
                    }

                }

            }
        });

        mainThread.start();

        return START_NOT_STICKY;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case -1:
                    Log.e("BackService-handler", "온습도 값을 가져오지 못했습니다.");
                    break;
                case 0:
                    Log.d("msg.obj", msg.obj.toString());
                    DeviceDataDTO dto = (DeviceDataDTO) msg.obj;

                    String data = "온도 : " + dto.getTemp() + " 습도 : " + dto.getHumi() + " 수분 : " + dto.getMois();

                    sendNotification(data);
                    if (dto.getHumi() <= 15) {
                        data = "너무 건조해요!";
                        sendNotification2(data);
                    }


                    break;

            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        serviceIntent = null;

        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);


        if(pref.getString("device_id", null) != null)
            setAlarmTimer();

        Thread.currentThread().interrupt();

        if (mainThread != null) {

            mainThread.interrupt();
            mainThread = null;

        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void showToast(final Application application, final String msg) {

        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "application : "+ application + " msg : " + msg);
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();

            }
        });

    }

    private void setAlarmTimer() {

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);

    }

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.dodamicon01)
                .setContentTitle(device_id)
                .setContentText(messageBody)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        manager.notify(0, builder.build());

    }

    private void sendNotification2(String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.dodamicon01)
                .setContentTitle(device_id)
                .setContentText(messageBody)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        manager.notify(1, builder.build());

    }

}