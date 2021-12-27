package com.example.kotlin_terminal_app

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class myService: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "I am started", Toast.LENGTH_LONG).show()
        return START_STICKY
    }

    override fun onDestroy() {
        Toast.makeText(this, "I am destoryed", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }
    override fun onBind(p0: Intent?): IBinder? {
        return  null
    }
}