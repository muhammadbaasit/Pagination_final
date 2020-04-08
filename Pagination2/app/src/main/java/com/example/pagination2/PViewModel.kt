package com.example.pagination2

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PViewModel : ViewModel(){

    var al = ArrayList<Model>()
    var alMutableLiveData = MutableLiveData<MutableList<Model>>()
    var mo:Model?=null

    fun getApiData(currentPage:Int,PAGES_LIMIT:Int) : MutableLiveData<MutableList<Model>> {

        val model: ArrayList<Model> = ArrayList()

        val apiinterface= ApiInterface.create().getData(currentPage,PAGES_LIMIT)

        apiinterface.enqueue(object : Callback<MutableList<Model>> {

            override fun onResponse(
                call: Call<MutableList<Model>>,
                response: Response<MutableList<Model>>
            ) {

                // Log.d("DATA","info>>>>>"+response.body())

                if(response?.body() != null){

                    for(items in response?.body()!!){

                         Log.d("DATASSS","info>>>>>"+response.body())

                        mo=Model(items.id,items.name,items.description)
                        al.add(mo!!)

                        alMutableLiveData.value = al

                    }
                }
            }

            override fun onFailure(call: Call<MutableList<Model>>, t: Throwable) {
                Log.d("FAILED","failed")
            }
        })

        return alMutableLiveData
    }
}