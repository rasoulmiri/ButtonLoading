package io.rmiri.buttonloadingsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.rmiri.buttonloading.CustomButtonLoading;

public class SampleTwo extends AppCompatActivity {

    CustomButtonLoading customButtonLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_2);

        customButtonLoading = (CustomButtonLoading) findViewById(R.id.buttonLoading);
        customButtonLoading.setOnButtonLoadingListener(new CustomButtonLoading.OnButtonLoadingListener() {
            @Override
            public void onClick() {
                Toast.makeText(SampleTwo.this, "onClick", Toast.LENGTH_SHORT).show();
                finishLoading(customButtonLoading);
            }

            @Override
            public void onStart() {
                Toast.makeText(SampleTwo.this, "onStart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Toast.makeText(SampleTwo.this, "onFinish", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void finishLoading(final CustomButtonLoading buttonLoading) {
        //call setProgress(false) after 5 second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonLoading.setProgress(false);
                Toast.makeText(SampleTwo.this, "onStart", Toast.LENGTH_SHORT).show();
            }
        }, 5000);
    }
}
