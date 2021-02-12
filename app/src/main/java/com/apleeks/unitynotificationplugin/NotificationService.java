package com.apleeks.unitynotificationplugin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends Service {


    private Looper serviceLooper;
    private StoppingHandler stoppingHandler;

    private final class StoppingHandler extends Handler{
        private long seconds;
        private long gems;
        private int toy;
        private int vip;
        private int upgrade;

        public StoppingHandler(Looper looper, long secs, long gems, int toy, int vip, int upgrade){
            super(looper);
            this.seconds = secs;
            this.gems = gems;
            this.toy = toy;
            this.vip = vip;
            this.upgrade = upgrade;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            try{
                int c = 0;
                while (true){
                    if (seconds > 0 && c == 0) {
                        seconds -= 1;
                        gems += (toy + upgrade) * vip;
                        Thread.sleep(1000);
                    }else{
                        c++;
                        if(c==1){
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                CharSequence name = "Shit Channel";
                                String description = "Lmao, what description?";
                                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                                NotificationChannel channel = new NotificationChannel("SomeID", name, importance);

                                channel.setDescription(description);
                                // Register the channel with the system; you can't change the importance
                                // or other notification behaviors after this
                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(channel);
                            }
                            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "Shit Channel")
                                    .setSmallIcon(R.drawable.icon)
                                    .setContentTitle("SpeedZee")
                                    .setContentText("У Вас закончилась ЭНЕРГИЯ! Пополните как можно скорей!")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                            notificationManager.notify(0, notification.build());
                        }
                    }
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

            stopSelf(msg.arg1);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long seconds = intent.getLongExtra("seconds", 0);
        long gems = intent.getLongExtra("gems", 0);
        int toy = intent.getIntExtra("toy", 0);
        int vip = intent.getIntExtra("vip", 0);
        int upgrade = intent.getIntExtra("upgrade", 0);

        HandlerThread thread = new HandlerThread("StartService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        stoppingHandler = new StoppingHandler(serviceLooper,seconds,gems,toy,vip,upgrade);

        Message msg = stoppingHandler.obtainMessage();
        msg.arg1 = startId;

        stoppingHandler.sendMessage(msg);
        return START_STICKY;
    }
}
