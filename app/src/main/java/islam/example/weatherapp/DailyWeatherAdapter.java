
package islam.example.weatherapp;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.awareness.state.Weather;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;
import java.util.Locale;

class DailyWeatherAdapter extends ArrayAdapter<weath> {

    private Context context;
    private List<weath>  weatherList;

    public DailyWeatherAdapter(@NonNull Context context, @NonNull List<weath> weatherList) {
        super(context, 0, weatherList);
        this.context = context;
        this.weatherList = weatherList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_wether, parent, false);

        TextView tvDate = convertView.findViewById(R.id.tv_deat);
        TextView tvTemp = convertView.findViewById(R.id.tv_deat);
        ImageView iconWeather = convertView.findViewById(R.id.icon_wih);

        weath weath = weatherList.get(position);
        tvTemp.setText(weath.getTemp()+" °C");

        Ion.with(context)
                .load("http://openweathermap.org/img/w/" + weath.getIcon() + ".png")
                .intoImageView(iconWeather);

        Date date = new Date(weath.getDate()*1000);
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM yy", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(weath.getTimeZone()));
        tvDate.setText(dateFormat.format(date));

        return convertView;
    }

}