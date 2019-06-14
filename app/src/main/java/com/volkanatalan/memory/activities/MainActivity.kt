package com.volkanatalan.memory.activities

import android.app.Activity
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
import com.volkanatalan.memory.views.SearchView
import kotlinx.android.synthetic.main.activity_main.*

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
    
    showIntro()
    setSupportActionBar(toolbar)
    setupAds()
    setupAnalytics()
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
