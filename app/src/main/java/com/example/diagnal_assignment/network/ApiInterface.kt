package com.example.diagnal_assignment.network

import com.example.diagnal_assignment.MainApp
import com.google.gson.Gson
import java.io.InputStream
import java.lang.Exception

/*
* @ApiInterface class created to simulate a real case where data will be consumed from network using Retrofit instance
* */
open class ApiInterface {

  open  fun <T> readFileAsJson(filename: String, myClass: Class<T>): Any? {
        try {
            val `is`: InputStream = MainApp.appContext.getAssets().open(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            val data = String(buffer, Charsets.UTF_8)
            var gson = Gson()
            return gson.fromJson(data, myClass)

        } catch (e: Exception) {
            return e
        }
    }
}