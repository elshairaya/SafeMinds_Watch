package com.safeminds.watch.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.safeminds.watch.scheduler.MonitoringSessionType

object Bridge {

    private const val TAG = "Bridge"

    fun startSession(context: Context, sessionType: MonitoringSessionType): Boolean {
        return try {
            Log.d(TAG, "startSession requested type=$sessionType")

            val intentMessage = Intent(
                context,
                MonitoringService::class.java
            ).apply {
                action = MonitoringService.ACTION_START_SESSION
                putExtra(MonitoringService.EXTRA_SESSION_TYPE, sessionType.name)
            }

            ContextCompat.startForegroundService(context, intentMessage)

            Log.d(TAG, "startForegroundService called successfully")

            true

        } catch (ex: Exception) {
            Log.e(TAG, "Failed to start MonitoringService", ex)

            false
        }
    }

    fun stopSession(context: Context) {
        try {
            Log.d(TAG, "stopSession requested")

            val intentMessage = Intent(
                context,
                MonitoringService::class.java
            ).apply {
                action = MonitoringService.ACTION_STOP_SESSION
            }
            context.startService(intentMessage)

        } catch (ex: Exception) {
            Log.e(TAG, "Failed to stop MonitoringService", ex)
            throw ex
        }
    }
}