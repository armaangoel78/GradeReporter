package com.example.sunja.gradereporter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;


/**
 * Created by sunja on 9/28/2016.
 */

public class GradeFinder extends Service {
    public static boolean isRunning = false;
    private Thread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRunning == false) Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        else if (isRunning == true) Toast.makeText(this, "Already Started", Toast.LENGTH_SHORT).show();
        isRunning = true;

        thread = new Thread(new ServiceThread(startId, isRunning));
        thread.start();
        return START_STICKY;
    }
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        thread.stop();
    }
}
