package com.graphicless.newsapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.graphicless.newsapp.viewmodel.NewsViewModel
import makeStatusNotification

private const val TAG = "NewsWorker"
class SyncWorker(context: Context, params:WorkerParameters): Worker(context, params) {

    @SuppressLint("WrongThread")
    override fun doWork(): Result {
        return try {
            Log.d(TAG, "doWork: updated")

            // Get the application context
            val appContext = applicationContext

            // Get the viewModel
            val viewModel = ViewModelProvider(appContext as FragmentActivity)[NewsViewModel::class.java]

            if(CheckNetwork().isConnected){
                // Perform work using the viewModel
                viewModel.insertTopNews(AppConstants.COUNTRY)
                makeStatusNotification("News is Updated, Please check", MyApplication.instance)
            }else{
                makeStatusNotification("Please connect the internet for get Updated News", MyApplication.instance)
            }
            Result.success()
        }catch (throwable: Throwable){
            Log.e(TAG, "Error fetching news")
            throwable.printStackTrace()
            Result.failure()
        }

    }
}