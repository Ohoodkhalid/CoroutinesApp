package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var textV :TextView
    lateinit var getButton :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getButton = findViewById(R.id.getButton)
        textV = findViewById(R.id.textView)

        getButton.setOnClickListener{
            requestAPI()


        }
    }


    private fun requestAPI(){

        CoroutineScope(IO).launch {
            // we fetch the data
            val data = async { fetchData() }.await()
            // once the data comes back, we populate our Recycler View
            if(data.isNotEmpty()){
                getAdvice(data)
            }else{
                Log.d("MAIN", "Unable to get data")
            }
        }
    }

    private fun fetchData(): String{

        var response = ""
        try{
            response = URL(" https://api.adviceslip.com/advice").readText()
        }catch(e: Exception){
            Log.d("MAIN", "ISSUE: $e")
        }
        // our response is saved as a string and returned
        return response
    }

    private suspend fun getAdvice (result:String){
        withContext(Main){


            val jsonObject= JSONObject(result)


            val advice  =jsonObject.getJSONObject("slip").getString("advice")
            textV.text = advice

        }
    }

}