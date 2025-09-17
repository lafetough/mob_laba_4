package com.example.laba4

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class DetailActivity : ComponentActivity() {
    private val gson = Gson()
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.detail_activity_layout)

        fetchData()

    }

    private fun fetchData() {
        val breedId = intent.getStringExtra("breed_id")
        val req = Request.Builder()
            .url("https://mobile-apps-programming-labs.firebaseapp.com/task4/breeds/${breedId}")
            .build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let {
                        responseBody ->
                    try {
                        val breedData : BreedData = gson.fromJson(responseBody, BreedData::class.java)

                        runOnUiThread {
                            updateInterface(breedData)
                        }


                    }catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    private fun updateInterface(data: BreedData) {
        findViewById<TextView>(R.id.breed_name).text = data.breedName
        findViewById<TextView>(R.id.breed_country).text = data.originCountry
        findViewById<TextView>(R.id.breed_description).text = data.description
        findViewById<TextView>(R.id.breed_height).text = data.height
        findViewById<TextView>(R.id.breed_weight).text = data.weight
    }

}