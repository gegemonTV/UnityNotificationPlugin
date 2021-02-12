package com.apleeks.unitynotificationplugin;

import android.content.Intent;

public class ServiceStarter {

    public void StartService(long secs, long gems, int toy, int vip, int upgrade){
        Intent serviceIntent = new Intent(NotificationService.class.getName());
        serviceIntent.putExtra("seconds", secs);
        serviceIntent.putExtra("gems", gems);
        serviceIntent.putExtra("toy", toy);
        serviceIntent.putExtra("vip", vip);
        serviceIntent.putExtra("upgrade", upgrade);
    }

}
