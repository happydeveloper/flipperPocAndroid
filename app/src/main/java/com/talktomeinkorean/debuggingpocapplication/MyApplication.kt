package com.talktomeinkorean.debuggingpocapplication

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkReporter.RequestInfo
import com.facebook.flipper.plugins.network.NetworkReporter.ResponseInfo
import com.facebook.soloader.SoLoader
import okhttp3.OkHttpClient
import okhttp3.*
import java.io.IOException

class MyApplication: Application() {



    companion object {
        const val TAG = "MyApplication"
        const val REQUEST_INTERVAL = 5000L // 1 second

    }

    private fun startPeriodicRequests(client: OkHttpClient) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                sendGetRequest(client)
                handler.postDelayed(this, REQUEST_INTERVAL)
            }
        }
        handler.post(runnable)
    }

    private fun buildOkHttp(appContext: Context): OkHttpClient {
        val plugin = AndroidFlipperClient
            .getInstance(appContext)
            .getPluginByClass(NetworkFlipperPlugin::class.java)
        return OkHttpClient.Builder()
            .addNetworkInterceptor(FlipperOkhttpInterceptor(plugin))
            .build()
    }

    private fun sendGetRequest(client: OkHttpClient) {
        val request = Request.Builder()
            .url("https://talktomeinkorean.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Network Request Failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Network Request Success: ${response.body?.string()}")
                } else {
                    Log.d(TAG, "Network Request Failed: ${response.code}")
                }
            }
        })
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        SoLoader.init(this, false)
        if ( FlipperUtils.shouldEnableFlipper(this)) {
            if (FlipperUtils.shouldEnableFlipper(this)) {
                val client = AndroidFlipperClient.getInstance(this)
                client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))

                val testPlugin = TestPlugin()
                client.addPlugin(testPlugin)
                val networkFlipperPlugin = NetworkFlipperPlugin()


                client.addPlugin(networkFlipperPlugin)

                client.start()

                val okHttpClient = buildOkHttp(this)
                sendGetRequest(okHttpClient) // Add this line to send the GET request
                startPeriodicRequests(okHttpClient)
            }
        }
        
    }

}