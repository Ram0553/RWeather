package com.example.dnld_web_content_prac;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    public  class down extends AsyncTask<String, Void ,String>
    {

        @Override
        protected String doInBackground(String... z) {
            Log.d("TAG", "TAG"+z[0]);
            String s="http://api.weatherapi.com/v1/forecast.json?key=4fe492dfc0224b478f3160537212306&q=";
            String l="&aqi=no";
            s+=z[0];
            s+=l;
            Log.d("TAG", "TAG"+s);
            String r="";
            try {
                URL u = new URL(s);
                Log.d("tag", "onCreate:0 ");
                HttpURLConnection h = (HttpURLConnection) u.openConnection();
                Log.d("tag", "onCreate:1 ");
                InputStream i = h.getInputStream();
                Log.d("tag", "onCreate:2 ");
                int d;
                InputStreamReader x = new InputStreamReader(i);
                d = x.read();
                Log.d("tag", "onCreate: ");
                while (d != -1) {
                    r += (char) d;
                    d = x.read();
                    //Log.d("Tag", ""+d);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("Tag", "doInBackground: "+r);
            return r;
        }
    }

    EditText city;
    TextView t1,mt1,mt2,at,c1,c2,ft,h,ws,dt;
    ImageView im1,im2;
    Button search;
    JSONObject j=null;
    JSONObject location=null, current=null, condition=null, forecast=null, day=null, date=null,condition2=null;
    JSONArray forecastday=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        search=findViewById(R.id.search);
        //t1,mt1,mt2,at,c1,c2,ft,h,ws,dt;
        dt=findViewById(R.id.date);
        t1=findViewById(R.id.temp);
        ft=findViewById(R.id.feel1);
        c1=findViewById(R.id.cond);
        h=findViewById(R.id.hum1);
        ws=findViewById(R.id.wind);
        mt1=findViewById(R.id.maxtemp);
        mt2=findViewById(R.id.mintemp);
        at=findViewById(R.id.avgtemp);
        c2=findViewById(R.id.cond1);
        city=findViewById(R.id.city);
        im1=findViewById(R.id.im1);
        im2=findViewById(R.id.im2);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String z="";
                    z=city.getText().toString();
                    String r=new down().execute(z).get();
                    j = new JSONObject(r);
                    location = j.getJSONObject("location");
                    current = j.getJSONObject("current");
                    condition = current.getJSONObject("condition");
                    forecast = j.getJSONObject("forecast");
                    forecastday = forecast.getJSONArray("forecastday");
                    date = forecastday.getJSONObject(0);
                    day = date.getJSONObject("day");
                    condition2=day.getJSONObject("condition");
                    city.setText(location.getString("name")+","+location.getString("region")+","+location.getString("country"));
                    dt.setText(location.getString("localtime"));
                    t1.setText("Temp: "+current.getString("temp_c"));
                    ft.setText("Feels Like: "+current.getString("feelslike_c"));
                    c1.setText("Condition: "+condition.getString("text"));
                    im1.setImageBitmap(get("http:"+condition.getString("icon")));
                    h.setText("Humidity: "+current.getString("humidity"));
                    ws.setText("Wind Speed: "+current.getString("wind_kph"));
                    mt1.setText("Max Temp: "+day.getString("maxtemp_c"));
                    mt2.setText("Min Temp: "+ day.getString("mintemp_c"));
                    at.setText("Avg. Temp: "+day.getString("avgtemp_c"));
                    c2.setText("Condition: "+condition2.getString("text"));
                    im2.setImageBitmap(get("http:"+condition2.getString("icon")));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static Bitmap get(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}