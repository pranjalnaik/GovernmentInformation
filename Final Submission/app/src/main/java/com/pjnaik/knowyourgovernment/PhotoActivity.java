package com.pjnaik.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    TextView location;
    TextView jobrole;
    TextView politicianname;
    ImageView politicianpic;
    ImageView partylogo;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        location = findViewById(R.id.locationphotoid);
        jobrole = findViewById(R.id.jobrolephotoid);
        politicianname = findViewById(R.id.namephotoid);
        politicianpic = findViewById(R.id.personphotoid);
        partylogo = findViewById(R.id.partylogoid);
        layout = findViewById(R.id.layout);

        Intent photointent = getIntent();
        if (photointent.hasExtra(Intent.ACTION_CALL)) {
            com.pjnaik.knowyourgovernment.Government govobj = (com.pjnaik.knowyourgovernment.Government) photointent.getSerializableExtra(Intent.ACTION_CALL);
            String link = govobj.getPicturelink();
            if (link == null) { politicianpic.setImageDrawable(getDrawable(R.drawable.missing)); }
            else {
                Picasso picasso = new Picasso.Builder(this).build();
                picasso.load(link).error(R.drawable.missing).placeholder(R.drawable.placeholder).into(politicianpic);
            }
            jobrole.setText(govobj.getJob());
            politicianname.setText(govobj.getName() + "\n(" + govobj.getParty() + ")");
            String partydist = govobj.getParty();
            if (partydist.contains("Republican")){
                partylogo.setImageDrawable(getDrawable(R.drawable.rep_logo));
                layout.setBackgroundColor(Color.RED);
            }
            else if (partydist.contains("Democratic")){
                partylogo.setImageDrawable(getDrawable(R.drawable.dem_logo));
                layout.setBackgroundColor(Color.BLUE);
            }
            else { layout.setBackgroundColor(Color.BLACK); }
        }
        if (photointent.hasExtra(Intent.EXTRA_REFERRER)){
            String loc = photointent.getStringExtra(Intent.EXTRA_REFERRER);
            this.location.setText(loc);
        }
    }
}