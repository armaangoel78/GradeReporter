package com.example.sunja.gradereporter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunja.gradereporter.GradeFinder;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    private void setup() {
        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        final Button refresh = (Button) findViewById(R.id.Refresh);

        ServiceThread.context = this;

        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startService(new Intent(getBaseContext(), GradeFinder.class));
            }
        });

        stop.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                GradeFinder.isRunning = false;
                stopService(new Intent(getBaseContext(), GradeFinder.class));
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshGrades();
            }
        });
    }


    private void refreshGrades() {
        TextView g1 = (TextView) findViewById(R.id.g1);
        TextView g2 = (TextView) findViewById(R.id.g2);
        TextView g3 = (TextView) findViewById(R.id.g3);
        TextView g4 = (TextView) findViewById(R.id.g4);
        TextView g5 = (TextView) findViewById(R.id.g5);
        TextView g6 = (TextView) findViewById(R.id.g6);
        TextView g7 = (TextView) findViewById(R.id.g7);
        TextView g8 = (TextView) findViewById(R.id.g8);

        ServiceThread.getGrades();

        while (ServiceThread.gradeArray[0].equals("")) {
            try{Thread.sleep(1);}
            catch (InterruptedException e) {e.printStackTrace();}
        }

        g1.setText(ServiceThread.gradeArray[0]);
        g2.setText(ServiceThread.gradeArray[1]);
        g3.setText(ServiceThread.gradeArray[2]);
        g4.setText(ServiceThread.gradeArray[3]);
        g5.setText(ServiceThread.gradeArray[4]);
        g6.setText(ServiceThread.gradeArray[5]);
        g7.setText(ServiceThread.gradeArray[6]);
        g8.setText(ServiceThread.gradeArray[7]);

    }


}
