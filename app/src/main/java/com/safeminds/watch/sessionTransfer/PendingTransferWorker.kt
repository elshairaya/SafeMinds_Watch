package com.safeminds.watch.sessionTransfer

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.safeminds.watch.logging.AppLogger
import com.safeminds.watch.storage.SafeMindsStorage

class PendingTransferWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "PendingTransferWorker"
    }

    override suspend fun doWork(): Result {
        val storage = SafeMindsStorage(applicationContext)
        val sender = WearMessageSender(applicationContext, storage)

        val pendingSessions = PendingTransferStorage.getPending(applicationContext)

        if (pendingSessions.isEmpty()) {
            AppLogger.d(TAG, "No pending sessions to retry")
            return Result.success()
        }

        AppLogger.i(TAG, "Retrying ${pendingSessions.size} pending session transfers")

        var hasRecoverableFailure = false

        for (sessionId in pendingSessions) {
            when (val result = sender.sendSession(sessionId)) {
                is TransferResults.Success -> {
                    PendingTransferStorage.removePending(applicationContext, sessionId)
                    AppLogger.i(TAG, "Pending session sent successfully: $sessionId")
                }

                is TransferResults.CoverableError -> {
                    hasRecoverableFailure = true
                    AppLogger.e(TAG, "Pending session recoverable error: ${result.errorReason}")
                }

                is TransferResults.UncoverableError -> {
                    PendingTransferStorage.removePending(applicationContext, sessionId)
                    AppLogger.e(TAG, "Pending session unrecoverable error: ${result.errorReason}")
                }
            }
        }
        return if (hasRecoverableFailure) {
            Result.retry()
        } else {
            Result.success()
        }
    }
}