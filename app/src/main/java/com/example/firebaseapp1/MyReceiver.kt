package com.example.firebaseapp1

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*
import kotlin.concurrent.thread

class MyReceiver : BroadcastReceiver(), ValueEventListener {

    override fun onReceive(context: Context, intent: Intent) {
        val database = Firebase.database
        val myRef = database.getReference("1")
        myRef.addListenerForSingleValueEvent(this)

        val newIntent = Intent(context, this.javaClass)
        val pi = PendingIntent.getBroadcast(context, 0, newIntent, 0)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, 5000, pi)

        database.getReference(Build.VERSION.SDK_INT.toString()).setValue("${Calendar.getInstance().time.toString()}")
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        Log.d("TAG", snapshot.toString())
        if (snapshot.value != null && snapshot.value == 0)
            return
        Firebase.database.getReference("1").setValue(0)

        thread {
            try {
                val socket = DatagramSocket()
                socket.broadcast = true
                val mac = arrayOf(0xD8, 0xCB, 0x8A, 0xEF, 0xB4, 0x4C)
                val msg = ByteArray(102, {-1})
                var i = 6
                while (i < msg.size){
                    for (b in mac){
                        msg[i++] = b.toByte()
                    }
                }
                val packet = DatagramPacket(msg, msg.size, InetAddress.getByName("255.255.255.255"), 9)
                socket.send(packet)
                socket.close()
            }
            catch (e: Exception){
                Log.d("TAG", e.toString())
            }
        }
    }

    override fun onCancelled(error: DatabaseError) {
    }
}