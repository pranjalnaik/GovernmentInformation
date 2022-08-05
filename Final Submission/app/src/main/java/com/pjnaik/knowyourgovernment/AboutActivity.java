package com.pjnaik.knowyourgovernment;

import android.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.graphics.Color;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    TextView apiUrlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Civil Advocacy");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        apiUrlText = findViewById(R.id.civicapiurl);
        final String urlapi = "https://developers.google.com/civic-information/";
        apiUrlText.setTextSize(35);
        apiUrlText.setTextColor(Color.WHITE);
        apiUrlText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        apiUrlText.setGravity(Gravity.CENTER);

        apiUrlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentabout = new Intent(Intent.ACTION_VIEW,Uri.parse(urlapi));
                startActivity(intentabout);
            }
        });
    }
}