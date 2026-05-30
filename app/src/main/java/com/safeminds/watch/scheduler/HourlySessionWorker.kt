package com.safeminds.watch.scheduler

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.safeminds.watch.service.Bridge
import com.safeminds.watch.storage.ScheduleStorage
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class HourlySessionWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Controller.checkNightSessionNow(applicationContext)

            val startedNow = Controller.handleHourlyCheckAction(applicationContext)
            val hourlyRunning = SessionStatePref.isHourlyCheckRunning(applicationContext)

            Log.d(
                "HourlySessionWorker",
                "startedNow=$startedNow hourlyRunning=$hourlyRunning"
            )

            if (!startedNow && !hourlyRunning) {
                Log.d(
                    "HourlySessionWorker",
                    "No hourly session started or running. Worker finished safely."
                )
                return Result.success()
            }

            val duration = ScheduleStorage
                .getSchedule(applicationContext)
                .hourlyCheckDuration

            Log.d("HourlySessionWorker", "Waiting duration=$duration minutes")

            delay(TimeUnit.MINUTES.toMillis(duration.toLong()))

            Log.d("HourlySessionWorker", "Delay finished")

            if (SessionStatePref.isHourlyCheckRunning(applicationContext)) {
                Log.d("HourlySessionWorker", "Stopping hourly session")
                Bridge.stopSession(applicationContext)
            }

            Result.success()

        } catch (ex: Exception) {
            Log.e("HourlySessionWorker", "Worker failed", ex)
            Result.success()
        }
    }
}