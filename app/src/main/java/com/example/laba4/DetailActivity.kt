package com.example.laba4

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var carousel_view: RecyclerView
    private lateinit var carousel_adapter: CarouselAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(this)

        setContentView(R.layout.detail_activity_layout)

        carousel_view = findViewById(R.id.image_carousel)

        carousel_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        carousel_view.setHasFixedSize(true)

        val e_list: List<String> = emptyList()

        carousel_adapter = CarouselAdapter(e_list)
        carousel_view.adapter = carousel_adapter

        fetchData()

    }

    private fun fetchData() {
        val breedId = intent.getStringExtra("breed_id")
        val req = Request.Builder()
            .url("https://mobile-apps-programming-labs.firebaseapp.com/task4/breeds/${breedId}")
            .build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Thread {
                    breedId?.let { db.breedDataDao().getBreedById(it) }?.let { breedData ->
                        runOnUiThread {
                            updateInterface(breedData)
                        }
                    }
                }.start()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let {
                        responseBody ->
                    try {
                        val breedData : BreedData = gson.fromJson(responseBody, BreedData::class.java)

                        Thread {
                            db.breedDataDao().insert(breedData)
                        }.start()

                        runOnUiThread {
                            updateInterface(breedData)
                        }


                    }catch (e: Exception) {
                        Thread {
                            breedId?.let { db.breedDataDao().getBreedById(it) }?.let { breedData ->
                                runOnUiThread {
                                    updateInterface(breedData)
                                }
                            }
                        }.start()
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
        carousel_adapter.updateData(data.photos)
    }

}