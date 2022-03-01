package islam.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String API_KAY="df07195ad38e7d3cbb2f448d3aec3285";
Button But_Search;
EditText Ed_txt;
ImageView icon_wih;
ListView lv;
TextView tv_temp,cty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //find_Item
        But_Search=findViewById(R.id.But_Search);
        Ed_txt=findViewById(R.id.Ed_txt);
        icon_wih=findViewById(R.id.icon_wih);
        tv_temp=findViewById(R.id.tv_temp);
        lv=findViewById(R.id.lv);
        cty=findViewById(R.id.cty);

        But_Search.setOnClickListener(v -> {
            String city = Ed_txt.getText().toString().trim();
            if (city.isEmpty()){
                Snackbar.make(MainActivity.this,icon_wih,"Enter city name",Snackbar.LENGTH_SHORT).show();
            }else {
                //lode wither by city name
                lodeWitherByCityName(city);
            }
        });

    }

    private void lodeWitherByCityName(String city) {

        Ion.with(this)
                .load("http://api.openweathermap.org/data/2.5/weather?q="+city+"&&units=metric&appid="+API_KAY)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                    if (e!=null){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }else {
                        JsonObject main = result.get("main").getAsJsonObject();
                        double temp = main.get("temp").getAsDouble();
                        tv_temp.setText(temp + "°C");

                        JsonObject sys = result.get("sys").getAsJsonObject();
                        String country = sys.get("country").getAsString();
                        cty.setText(city + "," + country);
                        JsonArray wither =result.get("weather").getAsJsonArray();
                        String icon=wither.get(0).getAsJsonObject().get("icon").getAsString();
                        img_lode(icon);



                        JsonObject coord = result.get("coord").getAsJsonObject();
                        double lon=coord.get("lon").getAsDouble();
                        double lat=coord.get("lat").getAsDouble();


                        loadDaylicast(lon,lat);
                    }
                    }
                });

    }

    private void loadDaylicast(double lon, double lat) {
        String apiUrl="https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=hourly,minutely,current&units=metric&appid="+API_KAY;
        Ion.with(this)
                .load(apiUrl)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e!=null){
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }else {

                            List<weath> weatherList = new ArrayList<>();
                            String timeZone = result.get("timezone").getAsString();
                            JsonArray daily = result.get("daily").getAsJsonArray();
                            for(int i=1;i<daily.size();i++) {
                                Long date = daily.get(i).getAsJsonObject().get("dt").getAsLong();
                                Double temp = daily.get(i).getAsJsonObject().get("temp").getAsJsonObject().get("day").getAsDouble();
                                String icon = daily.get(i).getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("icon").getAsString();
                                weatherList.add(new weath(date, timeZone, temp, icon));
                            }
                            DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(MainActivity.this, weatherList);
                            lv.setAdapter(dailyWeatherAdapter);

                        }

                    }

    });

}



    private void img_lode(String icon) {
        Ion.with(this)
                .load("https://openweathermap.org/img/w/"+icon+".png").intoImageView(icon_wih);

    }
    }
