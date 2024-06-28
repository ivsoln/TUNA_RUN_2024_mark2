package com.inventive.tunarun

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type


class Connect constructor(context: Context) {
    var dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
    var context = context
    inline fun <reified T> jObj(response: JSONObject): T {
        val str = response.toString()
        val gson = GsonBuilder().setDateFormat(dateFormat).create()
        val type: Type = object : com.google.gson.reflect.TypeToken<T?>() {}.type
        return gson.fromJson(str, type)
    }


    private inline fun <reified T> jObj(response: JSONArray): List<T> {
        val jStr = response.toString()
        val gson = GsonBuilder().setDateFormat(dateFormat).create()
        val type: Type = object : com.google.gson.reflect.TypeToken<List<T?>?>() {}.type
        return gson.fromJson<MutableList<T>>(jStr, type)
    }

    fun get(url: String, callback: StringCallback) {
        Log.i("___GET<T>", "..........")
        Log.i("___URL", url)
        val mQueue = Volley.newRequestQueue(context)
        val jRequest: StringRequest = object : StringRequest(
            Method.GET,
            url,
            Response.Listener { response ->
                Log.i("___RESULT<T>", response)
                Log.i("___END", "..........")
                callback.onSuccess(response)
            },
            Response.ErrorListener { error -> callback.onError(error.toString()) }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }
        }
        val socketTimeout = 30000 //30 seconds - change to what you want
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jRequest.setRetryPolicy(policy)
        mQueue.add(jRequest)
    }

    fun get(url: String, callback: NumberCallback) {
        Log.i("___GET<T>", "..........")
        Log.i("___URL", url)
        val mQueue = Volley.newRequestQueue(context)

        val jRequest: StringRequest = object : StringRequest(
            Method.GET,
            url,
            Response.Listener { response ->
                Log.i("___RESULT<T>", response)
                Log.i("___END", "..........")
                val result = response.toDouble()
                callback.onSuccess(result)
            },
            Response.ErrorListener { error -> callback.onError(error.toString()) }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }
        }
        val socketTimeout = 30000 //30 seconds - change to what you want
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jRequest.setRetryPolicy(policy)
        mQueue.add(jRequest)

    }

    fun get(url: String, callback: JSONCallback) {
        Log.i("___GET<T>", "..........")
        Log.i("___URL", url)
        val mQueue = Volley.newRequestQueue(context)

        val jRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            Response.Listener { response ->
                Log.i("___RESULT<T>", "$response")
                callback.onSuccess(response)
            },
            Response.ErrorListener { error -> callback.onError(error.toString()) }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }
        }
        val socketTimeout = 30000 //30 seconds - change to what you want
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jRequest.setRetryPolicy(policy)
        mQueue.add(jRequest)
    }

    inline fun <reified T> post(url: String, obj: T, callback: JSONCallback) {
        Log.i("___POST<T>", "..........")
        Log.i("___URL", url)

        val jStr = Gson().toJson(obj)
        val jObj = JSONObject(jStr)
        Log.i("___DATA", jObj.toString())

        val queue = Volley.newRequestQueue(context)
        val jRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jObj,
            Response.Listener { response ->
                Log.i("___RESULT<T>", "$response")
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                callback.onError(error.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }
        }
        val socketTimeout = 30000 //30 seconds - change to what you want
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jRequest.setRetryPolicy(policy)
        queue.add(jRequest)
    }

    fun search(url: String, arg: ActionArgument, callback: JSONCallback) {
        Log.i("___SEARCH<T>", "..........")
        Log.i("___URL", url)

        val jStr = Gson().toJson(arg)
        val jObj = JSONObject(jStr)
        Log.i("___DATA", jObj.toString())

        val mQueue = Volley.newRequestQueue(context)
        val jRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jObj,
            Response.Listener { response ->
                Log.i("___RESULT<T>", "$response")
                callback.onSuccess(response)
            },
            Response.ErrorListener { error -> callback.onError(error.toString()) }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                return params
            }
        }
        mQueue.add(jRequest)
    }
}


