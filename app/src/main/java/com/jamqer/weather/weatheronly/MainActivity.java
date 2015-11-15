package com.jamqer.weather.weatheronly;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jamqer.weather.weatheronly.API.API_weather;
import com.jamqer.weather.weatheronly.Model.ResponseData;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


@SuppressWarnings("deprecated")
public class MainActivity extends AppCompatActivity {

    /*Global declarations*/
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/" ; /*Base URL to API*/

    Button Btn_search;
    EditText Edt_txt;
    TextView tv_showWeather; TextView tv_ShowTemp; TextView tv_C;
    View Move_Layout; View Info_Layout;
    Typeface typeface;
    String name_city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

    /*Links to components declaration*/
        Btn_search = (Button) findViewById(R.id.button);
        Edt_txt = (EditText)findViewById(R.id.editText);
        tv_showWeather = (TextView)findViewById(R.id.tV_city);
        tv_C = (TextView)findViewById(R.id.tV_c);
        tv_ShowTemp = (TextView) findViewById(R.id.tV_temp);

    /*Layouts declarations*/
        Move_Layout = findViewById(R.id.moveLay);
        Info_Layout = findViewById(R.id.infoLay);

    /*Setting fonts at temp*/
        typeface = Typeface.createFromAsset(getAssets(), "fonts/vertigo.ttf");
        tv_ShowTemp.setTypeface(typeface);

    /*Setting AppCompat Tollbar icon and title*/
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.toolbar);
        getSupportActionBar().setTitle("  Is it Sunny?");

        fetchWeather();
    }

    private void MoveInfoDown(){ /*Simple animation for info*/

        ObjectAnimator WeatherInfoAnim = ObjectAnimator.ofFloat(Info_Layout, "Y", 90);
        WeatherInfoAnim.setDuration(2200);
        WeatherInfoAnim.setInterpolator(new FastOutSlowInInterpolator());
        WeatherInfoAnim.start();
    }

    private void HideSoftKeyboard(Activity activity){

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Move_Layout.getWindowToken(), 0);
    }

    private void fetchWeather() {

    /*Setting listener on search button*/
        Btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HideSoftKeyboard(MainActivity.this);

                /*Building retrofit **2.0** adapter*/
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create()) /*Creating Gson converter - it convert JSON -> STRING/ STRING -> JSON*/
                        .build();

                /*Create reference to API_weather class*/
                final API_weather api_weather = retrofit.create(API_weather.class);

                        /*Get value to query from editview*/
                name_city = Edt_txt.getText().toString();

                /*Call API, start doing *magic things* in ASYNCTASK method */
                Call<ResponseData> call = api_weather.getWeather(name_city);
                call.enqueue(new Callback<ResponseData>() {

                    @Override
                    public void onResponse(Response<ResponseData> response) {

                        try {
                            double dou_temp = Double.parseDouble(response.body().getMain().getTemp());

                            tv_ShowTemp.setText("" + Math.round(dou_temp - 273.15));
                            tv_C.setText(" " + "\u00b0" + "C");
                            tv_showWeather.setText("" + response.body().getCityName() + "\n"
                                    + "Pressure: " + response.body().getMain().getPressure() + " hPa" + "\n"
                                    + "Humidity: " + response.body().getMain().getHumidity() + " %" + "\n"
                                    + "Wind speed: " + response.body().getWind().getSpeed() + " m/s" + "\n"
                                    + "Clouds: " + response.body().getClouds().getAll() + " % of the sky");




                        }catch(NullPointerException e){
                            tv_showWeather.setText("We didn't find city name: " + name_city + "\n"
                                    + "Check your internet connection");
                        }
                    }

                    @Override /*Call failure LOG*/
                    public void onFailure(Throwable t) {
                        Log.v("**OnCALLfailure:**", "Error: " + t.toString());
                    }
                }); // End of Call<ResponseData>

                MoveInfoDown(); /*Animation of moving "Info_Layout"*/

            } // End of OnClick method
        }); // End of OnClickListner

    }
}
