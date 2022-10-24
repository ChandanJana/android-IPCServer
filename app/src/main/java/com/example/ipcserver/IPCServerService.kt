package com.example.ipcserver

import android.app.Service
import android.content.Intent
import android.os.*
import android.text.TextUtils
import android.util.Log
import com.example.ipcserver.RecentClient.CONNECTION_COUNT
import com.example.ipcserver.RecentClient.DATA
import com.example.ipcserver.RecentClient.PACKAGE_NAME
import com.example.ipcserver.RecentClient.PID


class IPCServerService : Service() {

    companion object {
        var connectionCount: Int = 0
        val NOT_SENT = "Not sent!"
    }

    // Messenger IPC - Messenger object contains binder to send to client
    private val mMessenger = Messenger(IncomingHandler())

    // Messenger IPC - Message Handler
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            // Get message from client. Save recent connected client info.
            val receivedBundle = msg.data
            RecentClient.client = Client(
                receivedBundle.getString(PACKAGE_NAME),
                receivedBundle.getInt(PID).toString(),
                receivedBundle.getString(DATA),
                "Messenger"
            )

            // Send message to the client. The message contains server info
            val message = Message.obtain(this@IncomingHandler, 0)
            val bundle = Bundle()
            bundle.putInt(CONNECTION_COUNT, connectionCount)
            bundle.putInt(PID, Process.myPid())
            message.data = bundle
            // The service can save the msg.replyTo object as a local variable
            // so that it can send a message to the client at any time
            msg.replyTo.send(message)
        }
    }

    private val binder = object : IIPCExample.Stub() {

        override fun getPid(): Int = Process.myPid()

        override fun getConnectionCount(): Int = IPCServerService.connectionCount

        override fun setDisplayedValue(packageName: String?, pid: Int, data: String?) {
            val clientData =
                if (data == null || TextUtils.isEmpty(data)) NOT_SENT
                else data

            RecentClient.client = Client(
                packageName ?: NOT_SENT,
                pid.toString(),
                clientData,
                "AIDL"
            )
        }

        override fun isValid(packageName: String?): Boolean {
            val callingPackageName: String? = packageManager.getNameForUid(
                Binder.getCallingUid()
            )
            Log.e("TAGG", "Package name "+ callingPackageName)
            if (packageName.equals(callingPackageName))
                return true

            return false
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        connectionCount++
        // Choose which binder we need to return based on the type of IPC the client makes
        return when (intent?.action) {
            "aidlexample" -> binder
            "messengerexample" -> mMessenger.binder
            else -> null
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAGG", "Service has been onDestroy ")
        stopSelf()
    }
}