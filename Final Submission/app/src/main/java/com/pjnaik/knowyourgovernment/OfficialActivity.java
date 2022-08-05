package com.pjnaik.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class OfficialActivity extends AppCompatActivity {
    private TextView locselected;
    private TextView jobname;
    private TextView namepolitician;
    private TextView nameparty;
    private ImageView logo;
    private ImageView politicianpic;
    private LinearLayout layout;
    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        jobname = findViewById(R.id.rolenameid);
        namepolitician = findViewById(R.id.personroleid);
        politicianpic = findViewById(R.id.picturepersonid);
        nameparty = findViewById(R.id.partyindividualid);
        logo = findViewById(R.id.partylogoid);
        locselected = findViewById(R.id.locationofficialid);
        scroll = findViewById(R.id.scrollview);
        layout = findViewById(R.id.linearLayout);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.ACTION_CALL)){
            com.pjnaik.knowyourgovernment.Government govtobject = (com.pjnaik.knowyourgovernment.Government) intent.getSerializableExtra(Intent.ACTION_CALL);
            TextView displaytext;

            if (govtobject.getUrl() != null){
                displaytext = new TextView(this);
                displaytext.setText("Website:  ".concat(govtobject.getUrl()).concat("\n") );
                displaytext.setTextSize(20);
                Linkify.addLinks(displaytext,Linkify.ALL);
                displaytext.setTextColor(Color.WHITE);
                layout.addView(displaytext);
            }
            if (govtobject.getAddr() != null){
                displaytext = new TextView(this);
                displaytext.setText("Address:  ".concat(govtobject.getAddr()).concat("\n") );
                displaytext.setTextSize(20);
                Linkify.addLinks(displaytext,Linkify.MAP_ADDRESSES);
                displaytext.setTextColor(Color.WHITE);
                layout.addView(displaytext);
            }
            if (govtobject.getEmail() != null){
                displaytext = new TextView(this);
                displaytext.setText("Email:   ".concat(govtobject.getEmail()).concat("\n") );
                displaytext.setTextSize(20);
                displaytext.setTextColor(Color.WHITE);
                Linkify.addLinks(displaytext,Linkify.ALL);
                layout.addView(displaytext);
            }
            if (govtobject.getPhone() != null){
                displaytext = new TextView(this);
                displaytext.setText("Phone:   ".concat(govtobject.getPhone()).concat("\n") );
                displaytext.setTextSize(20);
                displaytext.setTextColor(Color.WHITE);
                Linkify.addLinks(displaytext,Linkify.PHONE_NUMBERS);
                layout.addView(displaytext);
            }
            if (govtobject.getHasher() != null){
                HashMap<String, String> channels = govtobject.getHasher();
                ImageView displayimage = null;
                int img = 0;
                for(final String value : channels.keySet()){
                    if (img == 0)
                    {
                        displayimage = findViewById(R.id.net1id);
                    }
                    if (img == 1)
                    {
                        displayimage = findViewById(R.id.net2id);
                    }
                    if (img == 2)
                    {
                        displayimage = findViewById(R.id.net3id);
                    }
                    ++img;

                    final String channelkey = channels.get(value);
                    switch (value){
                        case "YouTube":
                            displayimage.setImageDrawable(getDrawable(R.drawable.youtube));
                            displayimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    youTubeClicked(v,channelkey);
                                }
                            });
                            break;
                        case "Facebook":
                            displayimage.setImageDrawable(getDrawable(R.drawable.facebook));
                            displayimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    facebookClicked(v,channelkey);
                                }
                            });
                            break;
                        case "Twitter":
                            displayimage.setImageDrawable(getDrawable(R.drawable.twitter));
                            displayimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    twitterClicked(v,channelkey);
                                }
                            });
                            break;
                    }
                }
            }
            String link = govtobject.getPicturelink();
            if(link == null){
                politicianpic.setImageDrawable(getDrawable(R.drawable.missing));
            }

            else {
                Picasso picasso = new Picasso.Builder(this).build();
                picasso.load(link).error(R.drawable.brokenimage).placeholder(R.drawable.placeholder).into(politicianpic);
            }

            jobname.setText(govtobject.getJob());
            namepolitician.setText(govtobject.getName());
            nameparty.setText("(" + govtobject.getParty() + ")");

            if (nameparty.getText().toString().contains("Democratic")){
                logo.setImageDrawable(getDrawable(R.drawable.dem_logo));
                scroll.setBackgroundColor(Color.BLUE);
            }

            else if (nameparty.getText().toString().contains("Republican")){
                logo.setImageDrawable(getDrawable(R.drawable.rep_logo));
                scroll.setBackgroundColor(Color.RED);
            }

            else { scroll.setBackgroundColor(Color.BLACK); }

        }
        if (intent.hasExtra(Intent.EXTRA_REFERRER)){
            String location = intent.getStringExtra(Intent.EXTRA_REFERRER);
            locselected.setText(location);
        }
    }

    public void youTubeClicked(View v, String youtubekey) {
        String name = youtubekey;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void twitterClicked(View v, String twitterkey) {
        Intent intent = null;
        String name = twitterkey;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void facebookClicked(View v,String facebookkey) {
        String FACEBOOK_URL = "https://www.facebook.com/" + facebookkey;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + facebookkey;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }


    public void onClick(View view)
    {
        Intent obj = getIntent();
        com.pjnaik.knowyourgovernment.Government govtobj = (com.pjnaik.knowyourgovernment.Government) obj.getSerializableExtra(Intent.ACTION_CALL);
        if (govtobj.getPicturelink() == null)
            return;
        String loc = obj.getStringExtra(Intent.EXTRA_REFERRER);
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra(Intent.ACTION_CALL, govtobj);
        intent.putExtra(Intent.EXTRA_REFERRER,loc);
        startActivity(intent);
    }
}


