package com.quislisting.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SignoutService extends Service {

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Log.d("SignoutService", "Service Started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("SignoutService", "Service Destroyed");
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
        Log.e("SignoutService", "END");
        super.onTaskRemoved(rootIntent);
        this.stopSelf();
    }
}
