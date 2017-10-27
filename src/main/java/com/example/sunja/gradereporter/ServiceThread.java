package com.example.sunja.gradereporter;

import android.content.Context;
import android.os.AsyncTask;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sunja on 10/13/2016.
 */

public class ServiceThread implements Runnable{
    public static Context context;
    private static final UUID appUuid = UUID.fromString("f3b4c310-5cfb-4b37-99a8-c7740526b182");
    private static final int button_event = 0, up = 1, down = 2, select = 3;


    int service_id;
    boolean running;
    public static String[] gradeArray = {"", "", "", "", "", "", "", ""};

    public ServiceThread (int service_id, boolean running) {
        this.service_id = service_id;
        this.running = running;

        PebbleKit.registerReceivedDataHandler(context, dataReceiver);
    }

    private static void clearArray() {
        for (int i = 0; i < gradeArray.length; i++) {
            gradeArray[i] = "";
        }
    }

    private PebbleKit.PebbleDataReceiver dataReceiver = new PebbleKit.PebbleDataReceiver(appUuid) {

        @Override
        public void receiveData(Context context, int transaction_id,
                                PebbleDictionary dict) {

            if (dict.getUnsignedIntegerAsLong(button_event) != null) {
                int button = dict.getUnsignedIntegerAsLong(button_event).intValue();

                switch (button) {
                    case select:
                        System.out.println("Getting grades");
                        getGrades();
                        break;
                }
            }
        }
    };

    public static void getGrades () {
        clearArray();
        new GradeScraper().execute();
    }

    public void run () {
    }


    private static class GradeScraper extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Connection.Response login = Jsoup.connect("https://myccs.ccs.k12.in.us/sessions")
                        .data("username", "armaangoel", "password", "asdfjk")
                        .method(Connection.Method.POST)
                        .execute();

                Map<String, String> cookie = login.cookies();
                Document doc = Jsoup.connect("https://myccs.ccs.k12.in.us/webgrades").cookies(cookie).get();

                String unparsedGrades = doc.getElementsByClass("score").toString();
                boolean bool = false;

                int elementNum = 0;
                for (int i = 1; i < unparsedGrades.length(); i++) {
                    if (bool==false && unparsedGrades.substring(i-1, i+1).equals("\">"))  bool = true;
                    else if (bool == true && unparsedGrades.substring(i, i+2).equals("</")) {
                        //parsedGrades += " ";
                        bool = false;
                        elementNum++;
                    }
                    else if (bool == true) gradeArray[elementNum] += unparsedGrades.substring(i,i+1);
                }

                for (int i = 0; i < gradeArray.length; i++){
                    System.out.println(gradeArray[i]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute() {

        }


    }
}
