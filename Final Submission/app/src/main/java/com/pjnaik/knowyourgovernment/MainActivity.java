package com.pjnaik.knowyourgovernment;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Intent;
import android.view.Gravity;
import android.os.Bundle;
import android.text.InputType;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;




import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recycler;
    private GovernmentAdapter governmentAdapter;
    private LocationManager locationManager;
    private ArrayList<Government> govtList = new ArrayList<>();


    private TextView currentlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerview);
        currentlocation = findViewById(R.id.locationidcurrent);

        governmentAdapter = new GovernmentAdapter(this, govtList);

        recycler.setAdapter(governmentAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        if (!checkNetworkConnection())
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("No Network Connection");
            alertDialogBuilder.setMessage("Data cannot be accessed / loaded without an internet connection.");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else
        {
            GovernmentLoaderVolley.getSourceData(this);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
            { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 329); }
            else { new CurrentLocation(this, govtList, "60616").execute(); }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.abouticon)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.locationchange)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText textdisplay = new EditText(this);
            textdisplay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textdisplay.setInputType(InputType.TYPE_CLASS_TEXT);
            textdisplay.setInputType(Gravity.CENTER);
            builder.setView(textdisplay);
            textdisplay.getText().toString();

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { new CurrentLocation(MainActivity.this, govtList, textdisplay.getText().toString()).execute(); }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.setTitle("Enter Address");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int layoutplaceholder = recycler.getChildLayoutPosition(v);
        Government government = govtList.get(layoutplaceholder);
        String loc = currentlocation.getText().toString();

        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra(Intent.EXTRA_REFERRER,loc);
        intent.putExtra(Intent.ACTION_CALL, government);
        startActivity(intent);
    }


    public void parachange(String loc)
    {
        governmentAdapter.notifyDataSetChanged();
        currentlocation.setText(loc);
    }

    private boolean checkNetworkConnection()
    {
        ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connection == null)
        { return false; }

        NetworkInfo networkcheck =connection.getActiveNetworkInfo();

        return networkcheck != null &&networkcheck.isConnected();
    }

    public void locationtMessenger()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("No Location Access!");
        alertDialogBuilder.setMessage("Data cannot be accessed / loaded without Location access on Application startup.");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void updateData(ArrayList<Government> cList) {
        govtList.addAll(cList);
        governmentAdapter.notifyItemRangeChanged(0, cList.size());
    }


    @Override
    public void onRequestPermissionsResult(int returncode, @NonNull String[] permissions, @NonNull int[] savedata)
    {
        super.onRequestPermissionsResult(returncode, permissions, savedata);
        if (returncode == 329) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && savedata[0] == PERMISSION_GRANTED)
            {
                new CurrentLocation(this, govtList, "60616").execute();
                return;
            }
            else
            { locationtMessenger(); }
        }
    }
}