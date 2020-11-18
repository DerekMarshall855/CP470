package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private TextView currTemp;
    private TextView minTemp;
    private TextView maxTemp;
    private ProgressBar progBar;
    private ImageView currWeather;
    private List <String> cityList;
    private TextView cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        Log.i(ACTIVITY_NAME, "In onCreate");

        progBar = findViewById(R.id.progressBar);
        currTemp = findViewById(R.id.currTemp);
        minTemp = findViewById(R.id.minTemp);
        maxTemp = findViewById(R.id.maxTemp);
        cityName = findViewById(R.id.cityName);
        currWeather = findViewById(R.id.currWeather);

        progBar.setVisibility(View.VISIBLE);

        get_a_city();
    }

    public void get_a_city() {

        // Get the list of cities.
        cityList = Arrays.asList(getResources().getStringArray(R.array.cities));

        // Make a handler for the city list.
        final Spinner citySpinner = findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.cities, android.R.layout.simple_spinner_dropdown_item);
        //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  android.R.layout.simple_spinner_item
        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                new ForecastQuery(cityList.get(i)).execute("this will go to background");
                cityName.setText(cityList.get(i) + " " + getResources().getString(R.string.weather_string));
                Log.i(ACTIVITY_NAME, "CityName Set");
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String min;
        private String max;
        private String currT;
        protected String city;
        private Bitmap currW;


        public ForecastQuery(String city) {
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(ACTIVITY_NAME, "onPreExecute is called");
        }

        protected String doInBackground(String ...urls) {

            try {
                URL url = new URL(
                        "https://api.openweathermap.org/data/2.5/weather?" +
                                "q=" + this.city + "," + "ca&" +
                                "APPID=79cecf493cb6e52d25bb7b7050ff723c&" +
                                "mode=xml&" +
                                "units=metric");

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                // If the timeout expires before there is data available for read,
                // a java.net.SocketTimeoutException is raised.
                httpsURLConnection.setReadTimeout(10000);
                // If the timeout expires before the connection can be established,
                // a java.net.SocketTimeoutException is raised.
                httpsURLConnection.setConnectTimeout(15000);
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.connect();

                InputStream in = httpsURLConnection.getInputStream();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);
                    int type;
                    //While you're not at the end of the document:
                    while ((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        //Are you currently at a Start Tag?
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("temperature")) {
                                currT = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                min = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                max = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                            } else if (parser.getName().equals("weather")) {
                                String iconName = parser.getAttributeValue(null, "icon");
                                String fileName = iconName + ".png";

                                Log.i(ACTIVITY_NAME, "Looking for file: " + fileName);
                                if (fileExistance(fileName)) {
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(fileName);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(ACTIVITY_NAME, "Found the file locally");
                                    currW = BitmapFactory.decodeStream(fis);
                                } else {
                                    String iconUrl = "https://openweathermap.org/img/w/" + fileName;
                                    currW = getImage(new URL(iconUrl));

                                    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    currW.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    Log.i(ACTIVITY_NAME, "Downloaded the file from the Internet");
                                    outputStream.flush();
                                    outputStream.close();
                                }
                                publishProgress(100);
                            }
                        }
                        // Go to the next XML event
                        parser.next();
                    }
                } finally {
                    httpsURLConnection.disconnect();
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return " do background ended";
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }

        public XmlPullParser parse(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                return parser;
            } finally {
                in.close();
            }
        }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public void onProgressUpdate(Integer ...value) {
            progBar.setVisibility(View.VISIBLE);
            progBar.setProgress(value[0]);
        }

        public void onPostExecute(String a) {
            Log.i ("post", a.toString() + "------------------");
            progBar.setVisibility(View.INVISIBLE);

            String newMin = getResources().getString(R.string.minT) + ": " + min + "C\u00b0";
            String newMax = getResources().getString(R.string.maxT) + ": " + max + "C\u00b0";
            String newCurr = getResources().getString(R.string.currT) + ": " + currT + "C\u00b0";

            currWeather.setImageBitmap(currW);
            minTemp.setText(newMin);
            maxTemp.setText(newMax);
            currTemp.setText(newCurr);

        }

    }



}