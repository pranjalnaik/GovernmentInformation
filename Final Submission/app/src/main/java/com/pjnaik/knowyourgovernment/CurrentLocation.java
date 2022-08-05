package com.pjnaik.knowyourgovernment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.net.Uri;




class CurrentLocation extends AsyncTask<Void, Void,String> {
    private MainActivity mainActivity;
    private ArrayList<com.pjnaik.knowyourgovernment.Government> govtarrlist;
    private String location;

    CurrentLocation(MainActivity mainActivity, ArrayList<com.pjnaik.knowyourgovernment.Government> govarray, String location){
        this.mainActivity = mainActivity;
        this.govtarrlist = govarray;
        this.location = location;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String begin = "https://www.googleapis.com/civicinfo/v2/representatives?key=" ;

        String addr = "&address="+ location;

        String  url = begin + "AIzaSyA7AfWUHhPc-nziL2zWhnOuupl9J-Pg5JE" + addr;

        String location = null;

        JSONObject jsonObject = downloadData(url);
        if (jsonObject == null){
            govtarrlist.clear();
            return "Data Unavailable";
        }

        else {
            govtarrlist.clear();
            location = DataChecker(jsonObject);
            return location;
        }

    }

    @Override
    protected void onPostExecute(String location) {
        mainActivity.parachange(location);
    }

    private JSONObject downloadData(String url){
        JSONObject jsonObject = null;
        String formatted_url = Uri.parse(url).toString();

        try {
            URL urlToUse = new URL(formatted_url);
            HttpURLConnection connection = (HttpURLConnection) urlToUse.openConnection();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw (new Exception());

            connection.setRequestMethod("GET");

            InputStream inputs = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(inputs)));

            String code;

            StringBuilder stringb = new StringBuilder();

            while ((code = reader.readLine()) != null) {
                stringb.append(code).append('\n');
            }

            jsonObject = new JSONObject(stringb.toString());
            return jsonObject;
        }

        catch (Exception e) {
            e.printStackTrace();
            return jsonObject;
        }

    }

    private String DataChecker(JSONObject jsonObject){
        JSONObject normalizedInput;
        String location = null;

        try {
            normalizedInput = jsonObject.getJSONObject("normalizedInput");
            String cityname,statename,zipcode;

            cityname = normalizedInput.getString("city");
            statename = normalizedInput.getString("state");
            zipcode = normalizedInput.getString("zip");

            location = cityname + ", " + statename + ", " + zipcode;

            JSONArray jobdetail = jsonObject.getJSONArray("offices");

            int i = 0;
            for (i = 0; i < jobdetail.length(); ++i) {
                JSONObject office = jobdetail.getJSONObject(i);
                String nameoffice = office.getString("name");

                JSONArray indices = office.getJSONArray("officialIndices");

                int j = 0;
                for (j = 0; j < indices.length(); ++j) {

                    int index = indices.getInt(j);
                    JSONArray officials = jsonObject.getJSONArray("officials");

                    JSONObject off = officials.getJSONObject(index);

                    String name = off.getString("name");

                    com.pjnaik.knowyourgovernment.Government government = new com.pjnaik.knowyourgovernment.Government(nameoffice, name);

                    if (! off.isNull("address")) {

                        JSONObject address = off.getJSONArray("address").getJSONObject(0);
                        String line2=" ",line3=" ";
                        String line1 = address.getString("line1");

                        if (! address.isNull("line2")){
                            line2 = address.getString("line2");
                        }

                        if (! address.isNull("line3")){
                            line3 = address.getString("line3");
                        }

                        String city = address.getString("city");
                        String state = address.getString("state");
                        String zip = address.getString("zip");

                        government.setAddr(line1 + " " + line2 + " " + line3+" "+city+" "+state+" "+" "+zip);
                    }
                    if (!off.isNull("party")){ government.setParty( off.getString("party")); }

                    if (!off.isNull("phones")) { government.setPhone(off.getJSONArray("phones").getString(0)); }

                    if (!off.isNull("urls")){ government.setUrl(off.getJSONArray("urls").getString(0)); }

                    if (!off.isNull("emails")){ government.setEmail(off.getJSONArray("emails").getString(0)); }

                    if (!off.isNull("photoUrl")) { government.setPicturelink(off.getString("photoUrl")); }

                    if (! off.isNull("channels")) {
                        JSONArray channels = off.getJSONArray("channels");
                        int k = 0;
                        for (k = 0; k < channels.length(); ++k){
                            JSONObject channelObject = channels.getJSONObject(k);
                            government.setHasher(channelObject.getString("type"),channelObject.getString("id"));
                        }

                    }
                    govtarrlist.add(government);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return location;
    }
}