package com.example.laba4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class DetailFragment : Fragment() {
    private val gson = Gson()
    private val client = OkHttpClient()

    private lateinit var carouselView: RecyclerView
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var db: AppDatabase
    private lateinit var breedNameView: TextView
    private lateinit var breedCountryView: TextView
    private lateinit var breedDescriptionView: TextView
    private lateinit var breedHeightView: TextView
    private lateinit var breedWeightView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.breed_detail_fragment, container, false)
        carouselView = view.findViewById(R.id.image_carousel)
        breedNameView = view.findViewById(R.id.breed_name)
        breedCountryView = view.findViewById(R.id.breed_country)
        breedDescriptionView = view.findViewById(R.id.breed_description)
        breedHeightView = view.findViewById(R.id.breed_height)
        breedWeightView = view.findViewById(R.id.breed_weight)

        val eList: List<String> = emptyList()
        carouselAdapter = CarouselAdapter(eList)
        carouselView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        carouselView.adapter = carouselAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().intent.getStringExtra("breed_id")?.let {
            fetchData(it)
        }
    }

    private fun fetchData(breedId: String) {
        val req = Request.Builder()
            .url("https://mobile-apps-programming-labs.firebaseapp.com/task4/breeds/${breedId}")
            .build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Thread {
                    breedId.let { db.breedDataDao().getBreedById(it) }?.let { breedData ->
                        requireActivity().runOnUiThread {
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

                        requireActivity().runOnUiThread {
                            updateInterface(breedData)
                        }


                    }catch (e: Exception) {
                        Thread {
                            breedId.let { db.breedDataDao().getBreedById(it) }?.let { breedData ->
                                requireActivity().runOnUiThread {
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
        breedNameView.text = data.breedName
        breedCountryView.text = data.originCountry
        breedDescriptionView.text = data.description
        breedHeightView.text = data.height
        breedWeightView.text = data.weight
        carouselAdapter.updateData(data.photos)
    }

}