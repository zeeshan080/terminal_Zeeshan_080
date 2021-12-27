package com.example.kotlin_terminal_app

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.kotlin_terminal_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    lateinit var receiver:MyReciever
    lateinit var titleArray:ArrayList<String>
    lateinit var quantityArray:ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        receiver= MyReceiver()

        IntentFilter(Intent.ACTION_BOOT_COMPLETED).also {

            registerReceiver(receiver, it)
        }
        
        bind = ActivityMainBinding.inflate(layoutInflater)

        var root = bind.root
        setContentView(root)
        var recView = bind.mainRecycle

        val queue= Volley.newRequestQueue(this)

        val url = "https://run.mocky.io/v3/0085e3ac-c7d6-4a93-ba92-925f65be6fb3"

    var jsonArrayRequest = JsonArrayRequest(
        Request.Method.GET,
        url,
        null,
        Response.Listener{res->
            titleArray = ArrayList()
            quantityArray = ArrayList()
            for (i in 0 until res.length()) {
                val jsonObject = res.getJSONObject(i)
                val title = jsonObject.optString("t")
                val quantity = jsonObject.optString("q").toInt()
                titleArray.add(title)
                quantityArray.add(quantity)
            }

            var adapter1 = newAdapter(titleArray,quantityArray)
            recView.adapter= adapter1
            adapter1.setOnItemClickListener(object:newAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    send_SMS(position)
                }
            })
            recView.layoutManager = GridLayoutManager(this,1)

        },
        Response.ErrorListener { err->
            Log.d("error-aray",err.toString())
        })
        queue.add(jsonArrayRequest)


    }

    private fun send_SMS(position: Int) {
        val SENT_ACTION="org.example.kotlin_terminal_app.SENT_ACTION"
        val DELIVERY_ACTION="org.example.DELIVERY_ACTION"
            /*PendinIntents for Sent and Delivery*/
            val sentIntent = PendingIntent.getBroadcast(this,
                100,
                Intent(SENT_ACTION), 0)

            val deliveryIntent = PendingIntent.getBroadcast(this, 200, Intent(DELIVERY_ACTION), 0)
            val sentRecvr=object: BroadcastReceiver()
            {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    Log.d("SMS ", "sent")
                }

            }
            val it= IntentFilter(SENT_ACTION)
            registerReceiver(sentRecvr, it)

            registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    Log.d("SMS ", "delivered")
                }
            }, IntentFilter(DELIVERY_ACTION))
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.RECEIVE_SMS)== PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)

            {
                var sms= SmsManager.getDefault()
                var tNumb = "(650) 555-1212"

                sms.sendTextMessage(tNumb,
                    "1234",
                    "Product bought",
                    sentIntent,deliveryIntent)
            }
            else
            {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS),111)
            }
        }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                recv_MSG()
            }
            else
            {
                Toast.makeText(applicationContext,"No permissions", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun recv_MSG() {
        val recvBroadcast:BroadcastReceiver=object:BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1))
                {
                   Log.d("address", sms.originatingAddress.toString())
                   Log.d("body",sms.displayMessageBody)
                }
            }

        }
        /*register receiver for Received SMS*/
        val it:IntentFilter= IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(recvBroadcast,it)
    }
    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"I am destroyed",Toast.LENGTH_LONG).show()
    }

}

