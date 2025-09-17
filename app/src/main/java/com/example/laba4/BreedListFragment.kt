package com.example.laba4

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

class BreedListFragment : Fragment() {
    private val client = OkHttpClient()
    private val gson = Gson()
    private lateinit var adapter: MainListAdapter
    private lateinit var breedListView: RecyclerView
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.breeds_list_fragment, container, false)
        breedListView = view.findViewById(R.id.main_list_rv)
        breedListView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getDatabase(requireContext())

        adapter = MainListAdapter(emptyList()) { item ->
            // Обработка клика — переход к деталям (можно сделать в отдельном фрагменте)
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("breed_id", item.id)
            startActivity(intent)
        }
        breedListView.adapter = adapter

        fetchData()
    }

    private fun fetchData() {

        val req = Request.Builder()
            .url("https://mobile-apps-programming-labs.firebaseapp.com/task4/breeds")
            .build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Thread {
                    val breeds = db.breedPreviewDao().getAllBreedPreviews()
                    requireActivity().runOnUiThread {
                        adapter.updateData(breeds)
                    }
                }.start()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let {
                        responseBody ->
                    try {
                        val listType = object : TypeToken<List<BreedPreview>>() {}.type
                        val breeds : List<BreedPreview> = gson.fromJson(responseBody, listType)

                        Thread {
                            for (breed in breeds) {
                                db.breedPreviewDao().insert(breed)
                            }
                        }.start()

                        requireActivity().runOnUiThread {
                            adapter.updateData(breeds)
                        }


                    }catch (e: Exception) {
                        Thread {
                            val breeds = db.breedPreviewDao().getAllBreedPreviews()
                            requireActivity().runOnUiThread {
                                adapter.updateData(breeds)
                            }
                        }.start()
                    }
                }
            }

        })

    }

}