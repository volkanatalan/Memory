package com.volkanatalan.memory.services

import android.content.Intent
import android.app.PendingIntent
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.volkanatalan.memory.R
import com.volkanatalan.memory.activities.MainActivity
import com.volkanatalan.memory.databases.MemoryDatabase


class Notification: BroadcastReceiver() {
  
  val TAG = "Notification"
  
  
  override fun onReceive(context: Context?, intent: Intent?) {
    val requestCode = context!!.resources.getInteger(R.integer.random_memory_notification)
    val database = MemoryDatabase(context, null)
    val randomMemory = database.rememberRandomMemory()
  
    if (randomMemory != null) {
      val title = randomMemory.title
      val text = randomMemory.text
  
  
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      
      val repeatingIntent = Intent(context, MainActivity::class.java)
      repeatingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
      repeatingIntent.putExtra("memoryTitle", title)
      
      val pendingIntent = PendingIntent.getActivity(context, requestCode, repeatingIntent,
        PendingIntent.FLAG_UPDATE_CURRENT)
    
      val notification = NotificationCompat.Builder(context)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setContentTitle(title)
        .setContentText(text)
        .setSound(null)
        .setSmallIcon(R.drawable.brain_status_bar)
        .setTicker(context.getString(R.string.remember_something))
  
      notificationManager.notify(requestCode, notification.build())
    } else {
      Log.d(TAG, "Memory is null!")
    }
  }
}