/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("WorkerUtils")


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.graphicless.newsapp.R
import com.graphicless.newsapp.utils.*
import java.util.concurrent.TimeUnit

private const val TAG = "WorkerUtils"
@SuppressLint("MissingPermission")
fun makeStatusNotification(message: String, context: Context) {

    // Make a channel if necessary
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        // Add the channel
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    Log.d(TAG, "makeStatusNotification: $message")
    // Create the notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.news_image)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

    // Show the notification
    try {
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }catch (e: Exception){
        Log.d(TAG, "makeStatusNotification: $e")
    }
}

class WorkManagerUtils {
    fun syncData(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val constraints =
            androidx.work.Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(SyncWorker::class.java, 300, TimeUnit.MINUTES)
                .setConstraints(constraints).setInitialDelay(300, TimeUnit.MINUTES).addTag("Sync_Data").build()
        /*val req =
            PeriodicWorkRequestBuilder<SyncWorker>(20, TimeUnit.MINUTES).setConstraints(constraints)
                .build()*/
        workManager.enqueueUniquePeriodicWork(
            "Sync_Data",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}