package com.volkanatalan.memory.activities

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.volkanatalan.memory.R
import com.volkanatalan.memory.fragments.DeleteConfirmationFragment
import com.volkanatalan.memory.fragments.IntroFragment
import com.volkanatalan.memory.helpers.ReminiscenceHelper
import com.volkanatalan.memory.interfaces.SearchViewInterface
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.services.Notification
import com.volkanatalan.memory.views.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import java.util.*

class MainActivity : AppCompatActivity(), SearchViewInterface.Listener {


  private var TAG = "MainActivity"
  private var EDIT_MEMORY = 0
  private var CHOOSE_SEARCH_ITEM = 0
  private lateinit var mFireBaseAnalytics: FirebaseAnalytics
  private lateinit var mInterstitialAd: InterstitialAd
  lateinit var mSearchView: SearchView
  
  
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    mSearchView = SearchView(layoutInflater, this)
    setContentView(mSearchView.getRootView())
  
    EDIT_MEMORY = resources.getInteger(R.integer.edit_memory)
    CHOOSE_SEARCH_ITEM = resources.getInteger(R.integer.choose_search_item)
  
    setRandomMemoryNotifications(this, true)
    
    showIntro()
    setSupportActionBar(toolbar)
    setupAds()
    setupAnalytics()
  
    val notificationMemoryTitle = intent.getStringExtra("memoryTitle")
    if (notificationMemoryTitle != null) {
      mSearchView.setSearchText(notificationMemoryTitle)
      mSearchView.hideKeyboard(true)
    }
  }
  
  
  
  
  
  override fun onClickSearchButton() {
    // Open SearchablesActivity
    val intent = Intent(this, SearchablesActivity::class.java)
    startActivityForResult(intent, CHOOSE_SEARCH_ITEM)
  }
  
  
  
  
  
  override fun onClickDeleteButton(fragment: DeleteConfirmationFragment) {
    supportFragmentManager.beginTransaction()
      .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_bottom, R.anim.exit_to_top)
      .add(R.id.fragment_container, fragment, "DeleteConfirmationFragment")
      .addToBackStack("DeleteConfirmationFragment")
      .commit()
  }
  
  
  
  
  
  override fun onClickEditButton(intent: Intent) {
    startActivityForResult(intent, EDIT_MEMORY)
  }
  
  
  
  
  
  override fun onRemember(reminiscenceHelper: ReminiscenceHelper, memories: MutableList<Memory>) {
    runOnUiThread { reminiscenceHelper.remember(memories) }
  }
  
  
  
  
  
  override fun afterSearch() {
    // Show ad
    if (mInterstitialAd.isLoaded) mInterstitialAd.show()
    else Log.d(TAG, "The interstitial wasn't loaded yet.")
  }





  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)

    if (resultCode == Activity.RESULT_OK && intent != null) {

      if (requestCode == EDIT_MEMORY) {
        val memoryId = intent.getIntExtra("updateMemory", -1)
        if (memoryId > -1) {
          mSearchView.updateMemory(memoryId)
        }
      }
      
      else if (requestCode == CHOOSE_SEARCH_ITEM){
        val searchItem = intent.getStringExtra("searchItem")
        search_edit_text.setText(searchItem)
      }
    }
  }





  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main_activity_menu, menu)
    return true
  }





  override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
    R.id.action_mainactivity_add -> {
      val intent = Intent(this, AddMemoryActivity::class.java)
      startActivity(intent)
      true
    }
    
    R.id.action_mainactivity_about -> {
      val intent = Intent(this, AboutActivity::class.java)
      startActivity(intent)
      true
    }
    
    R.id.action_mainactivity_settings -> {
      val intent = Intent(this, SettingsActivity::class.java)
      startActivity(intent)
      true
    }

    else -> {
      super.onOptionsItemSelected(item)
    }
  }





  override fun onBackPressed() {
    val searchText = mSearchView.getSearchText()
    val currentFragments = supportFragmentManager.fragments
  
    when {
      currentFragments.size > 0 -> {
        super.onBackPressed()
        mSearchView.showInterlayer(false)
      }
      
      searchText == "" -> super.onBackPressed()
      
      else -> mSearchView.setSearchText("")
    }
  }
  
  
  
  
  
  private fun showIntro(){
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragment_container, IntroFragment(), "IntroFragment")
      .addToBackStack("IntroFragment")
      .commit()
  }
  
  
  
  
  
  companion object {
    fun setRandomMemoryNotifications(context: Context, set: Boolean) {
      val TAG = "RandmMemoryNotification"
      val randomMemoryReqCode = context.resources.getInteger(R.integer.random_memory_notification)
      val intent = Intent(context, Notification::class.java)
      val pendingIntent =
        PendingIntent.getBroadcast(context, randomMemoryReqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
      val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
      val recurring = (60 * 60000).toLong()  // in milliseconds
  
  
      
  
      if (set) {
        val notifications = context.resources.getString(R.string.notifications)
        val allowNotifications = context.resources.getString(R.string.allow_random_notifications)
        val sharedPreferences = context.getSharedPreferences(notifications, Context.MODE_PRIVATE)
        val isNotificationsAllowed = sharedPreferences.getBoolean(allowNotifications, true)
        
        if (isNotificationsAllowed) {
          alarmManager.cancel(pendingIntent)
          alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis, recurring, pendingIntent
          )
          Log.d(TAG, "Random memory notification triggered for every ${recurring / 60000} minutes.")
        }
        else {
          Log.d(TAG, "Random memory notification are not allowed.")
        }
      }
      
      else {
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "Random memory notification canceled.")
      }
    }
  }
  
  
  
  
  
  private fun setupAds(){
    
    MobileAds.initialize(this, resources.getString(R.string.ad_app_id))
    
    // Banner ad
    val mAdView = AdView(this)
    mAdView.adSize = AdSize.BANNER
    mAdView.adUnitId = resources.getString(R.string.banner_ad)
    ad_banner_container.addView(mAdView)
    val adRequest = AdRequest.Builder().build()
    mAdView.loadAd(adRequest)
  
  
    // Interstitial ad
    mInterstitialAd = InterstitialAd(this)
    mInterstitialAd.adUnitId = resources.getString(R.string.interstitial_ad)
    mInterstitialAd.loadAd(AdRequest.Builder().build())
  
    mInterstitialAd.adListener = object: AdListener() {
      override fun onAdClosed() {
        // Code to be executed when the interstitial ad is closed.
        mInterstitialAd.loadAd(AdRequest.Builder().build())
      }
    }
  }
  
  
  
  
  
  private fun setupAnalytics(){
    mFireBaseAnalytics = FirebaseAnalytics.getInstance(this)
    val bundle = Bundle()
    mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
  }
}
