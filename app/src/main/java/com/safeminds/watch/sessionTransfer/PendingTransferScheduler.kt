package com.safeminds.watch.sessionTransfer

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object PendingTransferScheduler {

    private const val UNIQUE_RETRY_WORK_NAME = "pending_session_transfer_retry"

    fun scheduleRetry(context: Context) {
        val request = OneTimeWorkRequestBuilder<PendingTransferWorker>()
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            UNIQUE_RETRY_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}