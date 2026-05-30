package com.safeminds.watch.sessionTransfer

import android.content.Context

object PendingTransferStorage {

    private const val PREF_NAME = "pending_session_transfers"
    private const val KEY_PENDING_IDS = "pending_session_ids"

    fun addPending(context: Context, sessionId: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = prefs.getStringSet(KEY_PENDING_IDS, emptySet()) ?: emptySet()

        val updated = current.toMutableSet()
        updated.add(sessionId)

        prefs.edit()
            .putStringSet(KEY_PENDING_IDS, updated)
            .apply()
    }

    fun removePending(context: Context, sessionId: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = prefs.getStringSet(KEY_PENDING_IDS, emptySet()) ?: emptySet()

        val updated = current.toMutableSet()
        updated.remove(sessionId)

        prefs.edit()
            .putStringSet(KEY_PENDING_IDS, updated)
            .apply()
    }

    fun getPending(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_PENDING_IDS, emptySet())
            ?.toList()
            ?: emptyList()
    }
}