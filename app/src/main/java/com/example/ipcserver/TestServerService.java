package com.example.ipcserver;

import static com.example.ipcserver.RecentClient.DATA;
import static com.example.ipcserver.RecentClient.PACKAGE_NAME;
import static com.example.ipcserver.RecentClient.PID;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class TestServerService extends Service {
    public TestServerService() {
    }

    // Messenger IPC - Messenger object contains binder to send to client
    private Messenger mMessenger = new Messenger(new IncomingHandler());

    // Messenger IPC - Message Handler
    class IncomingHandler extends Handler {

        IncomingHandler() {
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Get message from client. Save recent connected client info.
            Log.e("TAGG", "Received data ");
            Bundle receivedBundle = msg.getData();
            RecentClient.INSTANCE.setClient(new Client(
                    receivedBundle.getString(PACKAGE_NAME),
                    String.valueOf(receivedBundle.getInt(PID)),
                    receivedBundle.getString(DATA),
                    "Messenger"
            ));

            // Send message to the client. The message contains server info
            //Message message = Message.obtain(this, 0);
            //Bundle bundle = new Bundle();
            //bundle.putInt(CONNECTION_COUNT, connectionCount)
            //bundle.putInt(PID, Process.myPid())
            //message.setData(bundle);
            // The service can save the msg.replyTo object as a local variable
            // so that it can send a message to the client at any time
            /*try {
                msg.replyTo.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}