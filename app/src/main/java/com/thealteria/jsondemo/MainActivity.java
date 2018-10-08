package com.thealteria.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.info);

        DownloadTask task = new DownloadTask();
        task.execute("http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();

                InputStream stream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);

                int data = reader.read();

                while (data != -1){

                    char current = (char) data;
                    result += current;

                    data = reader.read();

                }

                return result;
            }

            catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) { //pass the result from above method to this method
            /* the doiInBackgroung method can't interact with UI hence we update the UI in the main thread or
             in the PostExecute thread*/

            try {
                JSONObject object = new JSONObject(s);

                String weatherInfo = object.getString("weather"); //extract the weather part of the json object
                Log.i("Website content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject part = arr.getJSONObject(i);

                    String main = part.getString("main");
                    String description = part.getString("description");

                    info.setText("Main: " + main + "\n" + "\n" + "Description: "+ description);

                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
