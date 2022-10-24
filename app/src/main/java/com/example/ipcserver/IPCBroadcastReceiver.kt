package com.example.ipcserver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.ipcserver.RecentClient.DATA
import com.example.ipcserver.RecentClient.PACKAGE_NAME
import com.example.ipcserver.RecentClient.PID

class IPCBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        RecentClient.client = intent?.getStringExtra(PID)?.let {
            Client(
                intent?.getStringExtra(PACKAGE_NAME),
                it,
                intent?.getStringExtra(DATA),
                "Broadcast"
            )
        }
    }
}