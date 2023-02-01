package com.graphicless.newsapp.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.graphicless.newsapp.utils.MyApplication

class DataFetchReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Perform data fetching here
        Toast.makeText(MyApplication.instance, "Alarm got called", Toast.LENGTH_LONG).show()
    }
}
