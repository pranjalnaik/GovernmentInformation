package com.pjnaik.knowyourgovernment;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GovernmentLoaderVolley {

    private static final String TAG = "Loader--";
    private static final String begin = "https://www.googleapis.com/civicinfo/v2/representatives" ;

    //////////////////////////////////////////////////////////////////////////////////

    private static final String yourAPIKey = "AIzaSyA7AfWUHhPc-nziL2zWhnOvupl9J-Pg5JE";
    //
    //////////////////////////////////////////////////////////////////////////////////

    public static void getSourceData(MainActivity mainActivity) {
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(begin).buildUpon();
        buildURL.appendQueryParameter("key", yourAPIKey);
        buildURL.appendQueryParameter("address", "60616");
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONArray> listener =
                response -> handleResults(mainActivity, response.toString());
        Response.ErrorListener error = error1 -> {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(new String(error1.networkResponse.data));
                handleResults(mainActivity, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, urlToUse,
                        null, listener, error);


        queue.add(jsonArrayRequest);
    }

    private static void handleResults(MainActivity mainActivity, String s) {

        if (s == null) {
            return;
        }

        final ArrayList<Government> govtarraylist = parseJSON(s);
        if (govtarraylist != null)
//            Toast.makeText(mainActivity, "Loaded " + govtarraylist.size() + " Names", Toast.LENGTH_LONG).show();
        mainActivity.updateData(govtarraylist);
    }

    private static ArrayList<Government> parseJSON(String s) {
        ArrayList<Government> govtarraylist = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {

                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                JSONObject nameObj = jCountry.getJSONObject("normalizedInput");
                String name1 = nameObj.getString("line1");
                String name2 = nameObj.getString("city");
                String name3 = nameObj.getString("state");
                String name4 = nameObj.getString("zip");
                String combname = name1 + name2 + name3 + name4;

                String capital = "None";
                String officialindices = "None";
                if (jCountry.has("offices")) {
                    JSONArray capArr = jCountry.getJSONArray("name");
                    capital = capArr.getString(0);
                    JSONArray offInd = jCountry.getJSONArray("officialIndices");
                    officialindices = offInd.getString(0);
                }

                String personname = "None";
                String combpersonnelname = "None";
                String partypersonnel = "None";
                String phonenumbers = "None";
                String saveurl = "None";
                String typesoc  = "None";
                String idsoc = "None";
                String emailid = "None";
                if (jCountry.has("officials")) {
                    JSONArray personnel = jCountry.getJSONArray("name");
                    personname = personnel.getString(Integer.parseInt(officialindices));
                    JSONObject addrobj = jCountry.getJSONObject("address");
                    String personnelname1 = addrobj.getString("line1");
                    String personnelname2 = addrobj.getString("city");
                    String personnelname3 = addrobj.getString("state");
                    String personnelname4 = addrobj.getString("zip");
                    combpersonnelname = personnelname1 + personnelname2 + personnelname3 + personnelname4;


                    if (personnel.equals("party")){
                        partypersonnel = jCountry.getString("party");
                    }

                    if (personnel.equals("phones")){
                        JSONArray phoneobj = jCountry.getJSONArray("phones");
                        phonenumbers = phoneobj.getString(0);
                    }

                    if (personnel.equals("urls")){
                        JSONArray urlobj = jCountry.getJSONArray("urls");
                        saveurl = urlobj.getString(0);
                    }

                    if (personnel.equals("emails")){
                        JSONArray mailobj = jCountry.getJSONArray("mails");
                        emailid = mailobj.getString(0);
                    }

                    if (personnel.equals("channels")){
                        JSONArray chanarr = jCountry.getJSONArray("channels");
                        typesoc = chanarr.getString(Integer.parseInt("type"));
                        idsoc = chanarr.getString(Integer.parseInt("id"));
                    }

                }

                govtarraylist.add(
                        new Government(combname,  personname , phonenumbers, capital, combpersonnelname, saveurl, partypersonnel, emailid,
                                 typesoc, idsoc));
            }
            return govtarraylist;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}