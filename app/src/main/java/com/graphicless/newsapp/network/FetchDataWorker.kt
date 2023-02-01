package com.graphicless.newsapp.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class FetchDataWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // Do your work here

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val time = "$hour:$minute:$second"

        withContext(Dispatchers.Main) {
            Toast.makeText(applicationContext, "Data saved", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "doWork: called at $time")
        }
        // return Result.success() or Result.failure()
        return Result.success()
    }
}
