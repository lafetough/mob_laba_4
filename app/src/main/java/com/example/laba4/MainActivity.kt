package com.example.laba4

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import com.google.gson.reflect.TypeToken


class MainActivity : ComponentActivity() {
    private val client = OkHttpClient()
    private val gson = Gson()
    private lateinit var adapter: MainListAdapter
    private lateinit var breed_list_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_screen_layout)

        breed_list_view = findViewById(R.id.main_list_rv)

        breed_list_view.layoutManager = LinearLayoutManager(this)

        val breeds : List<BreedPreview> = emptyList()

        adapter = MainListAdapter(breeds) { item ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("breed_id", item.id)
            startActivity(intent)
        }
        breed_list_view.adapter = adapter

        fetchData()

    }


    private fun fetchData() {

        val req = Request.Builder()
            .url("https://mobile-apps-programming-labs.firebaseapp.com/task4/breeds")
            .build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let {
                    responseBody ->
                    try {
                        val listType = object :TypeToken<List<BreedPreview>>() {}.type
                        val breeds : List<BreedPreview> = gson.fromJson(responseBody, listType)

                        runOnUiThread {
                            adapter.updateData(breeds)
                        }


                    }catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })

    }

}
