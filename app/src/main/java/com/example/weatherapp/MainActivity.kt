package com.example.weatherapp

import android.icu.text.DecimalFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var weatherTextView: TextView;
    lateinit var  tempTextView: TextView;
    lateinit var  pressureTextView: TextView;
    lateinit var  humidityTextView: TextView;
    lateinit var sunsetTextView: TextView;
    lateinit var sunriseTextView: TextView;
    lateinit var cityEditText: EditText;
    lateinit var weatherImageView: ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherTextView =findViewById(R.id.weatherTextView)
        tempTextView =findViewById(R.id.tempTextView)
        pressureTextView =findViewById(R.id.pressureTextView)
        humidityTextView =findViewById(R.id.humidityTextView)
        sunsetTextView =findViewById(R.id.sunsetTextView)
        sunriseTextView =findViewById(R.id.sunriseTextView)
        cityEditText= findViewById(R.id.cityEditText)
        weatherImageView= findViewById(R.id.weatherImageView)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getWeather(view: View) {
        // Instantiate the RequestQueue.
        if (cityEditText.text.toString()!="") {
            val queue = Volley.newRequestQueue(this)
            val url =
                "https://api.openweathermap.org/data/2.5/weather?q=${cityEditText.text.toString()}&appid=9fd7a449d055dba26a982a3220f32aa2"

// Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
                    println("Response is: $response")

                    var responseObject = JSONObject(response)
                    var temperature = responseObject.getJSONObject("main").getDouble("temp")
                    tempTextView.text = String.format("%.2f C",(temperature - 273.15))


                    //[] getJSONArray
                    //{} get JSONObject

                    var weather =
                        responseObject.getJSONArray("weather").getJSONObject(0).getString("main")
                    weatherTextView.text = weather

                    var pressure = responseObject.getJSONObject("main").getInt("pressure")
                    pressureTextView.text = "$pressure hPa"

                    var humidity = responseObject.getJSONObject("main").getInt("humidity")
                    humidityTextView.text = "$humidity %"

                    var sunset = responseObject.getJSONObject("sys").getLong("sunset")
                    // Unix timestamp (in seconds)
                    // Convert timestamp to milliseconds
                    val timestampInMilliseconds = sunset * 1000L

                    // Create a Date object from the timestamp
                    val sunsetTime = Date(timestampInMilliseconds)

                    // Create a SimpleDateFormat object with a custom date and time format
                    val sunsetDateFormat = SimpleDateFormat("HH:mm:ss")

                    // Format the date using the SimpleDateFormat object
                    val sunsetFormattedTime = sunsetDateFormat.format(sunsetTime)

                    sunsetTextView.text = "Sunset: \n$sunsetFormattedTime \nGMT+08:00"

                    var sunrise = responseObject.getJSONObject("sys").getLong("sunrise")

                    // Unix timestamp (in seconds)
                    // Convert timestamp to milliseconds
                    val sunriseTmestampInMilliseconds = sunrise * 1000L

                    // Create a Date object from the timestamp
                    val sunriseTime = Date(sunriseTmestampInMilliseconds)

                    // Create a SimpleDateFormat object with a custom date and time format
                    val sunriseDateFormat = SimpleDateFormat("HH:mm:ss")

                    // Format the date using the SimpleDateFormat object
                    val sunriseFormattedTime = sunriseDateFormat.format(sunriseTime)

                    sunriseTextView.text = "Sunrise: \n$sunriseFormattedTime \nGMT+08:00"

                    var iconId =
                        responseObject.getJSONArray("weather").getJSONObject(0).getString("icon")
                    println(iconId)

                    var imageUrl = "https://openweathermap.org/img/wn/$iconId@2x.png"
                    Glide.with(this@MainActivity).load(imageUrl).into(weatherImageView)

                },
                Response.ErrorListener { println("That didn't work!") })

// Add the request to the RequestQueue.
            queue.add(stringRequest)
        }else{
            Toast.makeText(this@MainActivity, "Please enter city name", Toast.LENGTH_LONG).show()
    }
    }

}