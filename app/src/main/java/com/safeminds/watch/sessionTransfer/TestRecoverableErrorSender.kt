package com.safeminds.watch.sessionTransfer

class TestRecoverableErrorSender : Sender {
    override suspend fun sendSession(sessionID: String): TransferResults {
        return TransferResults.CoverableError("Temporary failure")
    }
}
//we have the case after one fail, it should resend this function will be added retryAllWaiting().