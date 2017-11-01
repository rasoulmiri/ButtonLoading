package io.rmiri.buttonloadingsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.rmiri.buttonloading.ButtonLoading;

public class SampleOne extends AppCompatActivity {

    ButtonLoading buttonLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_1);

        buttonLoading = (ButtonLoading) findViewById(R.id.buttonLoading);

        buttonLoading.setOnButtonLoadingListener(new ButtonLoading.OnButtonLoadingListener() {
            @Override
            public void onClick() {
                Toast.makeText(SampleOne.this, "onClick", Toast.LENGTH_SHORT).show();
                finishLoading();
            }

            @Override
            public void onStart() {
                Toast.makeText(SampleOne.this, "onStart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Toast.makeText(SampleOne.this, "onFinish", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void finishLoading() {
        //call setProgress(false) after 5 second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonLoading.setProgress(false);
            }
        }, 5000);
    }
}
