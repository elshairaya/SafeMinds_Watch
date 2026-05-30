package com.safeminds.watch.sessionTransfer

//it is storage interface to show how the app can read and retrieve transferred data

interface TransferSessionRepository {
    fun getTransferredRecord(sessionID: String): TransferEntry?

    fun updateRecord(updatedRecord: TransferEntry)

    fun listAllRecords(): List<TransferEntry>

    fun listWaitingRecords(): List<TransferEntry> //returns list of records that need to be sent again "pending state"




}