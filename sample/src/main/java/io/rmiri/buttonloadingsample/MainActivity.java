package io.rmiri.buttonloadingsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button sampleOne, sampleTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleOne = (Button) findViewById(R.id.sampleOne);
        sampleTwo = (Button) findViewById(R.id.sampleTwo);

        sampleOne.setOnClickListener(this);
        sampleTwo.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sampleOne:
                startActivity(new Intent(this, SampleOne.class));
                break;
            case R.id.sampleTwo:
                startActivity(new Intent(this, SampleTwo.class));
                break;
        }
    }
}
