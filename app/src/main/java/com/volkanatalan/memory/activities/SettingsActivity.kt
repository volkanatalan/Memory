package com.volkanatalan.memory.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import android.content.Context
import com.volkanatalan.memory.R


class SettingsActivity : AppCompatActivity() {
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    
    setupAllowNotifications()
  }
  
  @SuppressLint("ApplySharedPref")
  private fun setupAllowNotifications() {
    val notifications = resources.getString(R.string.notifications)
    val allowNotifications = resources.getString(R.string.allow_random_notifications)
    val sharedPreferences = getSharedPreferences(notifications, Context.MODE_PRIVATE)
    val isNotificationsAllowed = sharedPreferences.getBoolean(allowNotifications, true)
    allow_notifications_switch.isChecked = isNotificationsAllowed
    
    allow_notifications_switch.setOnCheckedChangeListener{_, isChecked ->
      
      // Save changed setting
      val editor = sharedPreferences.edit()
      editor.putBoolean(allowNotifications, isChecked)
      editor.commit()
  
      // Disallow notifications
      MainActivity.setRandomMemoryNotifications(this, isChecked)
    }
  }
}
