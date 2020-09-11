package com.app.armygyan.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

abstract class InternetBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action !=null && intent.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            onConnectionChanged();
        }
    }

    abstract fun onConnectionChanged()
}