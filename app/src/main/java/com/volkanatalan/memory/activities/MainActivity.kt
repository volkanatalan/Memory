package com.volkanatalan.memory.activities

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.google.android.gms.ads.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.volkanatalan.memory.R
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.databases.MemoryDatabase
import com.volkanatalan.memory.fragments.OpeningFragment
import com.volkanatalan.memory.helpers.ReminiscenceHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {


  private var TAG = "MainActivity"
  private var mMemories = mutableListOf<Memory>()
  private lateinit var reminiscenceHelper: ReminiscenceHelper
  private var EDIT_MEMORY = 0
  private var CHOOSE_SEARCH_ITEM = 0
  private var isEditTextAnimating = false
  private var isEditTextHidden = false
  private lateinit var mFireBaseAnalytics: FirebaseAnalytics
  private lateinit var mInterstitialAd: InterstitialAd
  private var isRememberedRandom = false
  
  
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  
    EDIT_MEMORY = resources.getInteger(R.integer.edit_memory)
    CHOOSE_SEARCH_ITEM = resources.getInteger(R.integer.choose_search_item)
  
    
    showIntro()
    setSupportActionBar(toolbar)
    setupSearchEditText()
    setupSearchResultsContainer()
    setupAds()
    setupAnalytics()
  }





  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)

    if (resultCode == Activity.RESULT_OK && intent != null) {

      if (requestCode == EDIT_MEMORY) {
        val memoryId = intent.getIntExtra("updateMemory", -1)
        if (memoryId > -1) {
          updatemMemories(memoryId)
          reminiscenceHelper.remember(mMemories)
        }
      }
      
      else if (requestCode == CHOOSE_SEARCH_ITEM){
        val searchItem = intent.getStringExtra("searchItem")
        searchEditText.setText(searchItem)
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
    val searchText = searchEditText.text.toString()
    val currentFragments = supportFragmentManager.fragments
  
    when {
      currentFragments.size > 0 -> {
        super.onBackPressed()
        interlayer.visibility = View.GONE
      }
      
      searchText == "" -> super.onBackPressed()
      
      else -> searchEditText.setText("")
    }
  }





  private fun setupSearchEditText() {
    // Show saved tags and titles
    search_image_view.setOnClickListener {
      val intent = Intent(this, SearchablesActivity::class.java)
      startActivityForResult(intent, CHOOSE_SEARCH_ITEM)
    }
    
    
    // Clear edit text when clicked on x button
    cleanSearch.setOnClickListener {
      searchEditText.setText("")
    }
    
    // Set on text changed listener to edit text
    searchEditText.addTextChangedListener(object:TextWatcher{
      override fun afterTextChanged(s: Editable?) {
        var searchText = s.toString()

        if (searchText.length > 1) {
          
          while (searchText.contains("'")){
            val index = searchText.indexOf("'")
            searchText = searchText.substring(0, index) + " " + searchText.substring(index + 1, searchText.length)
          }
          
          val database = MemoryDatabase(this@MainActivity, null)
          mMemories = database.rememberMemories(searchText)
          database.close()
          reminiscenceHelper.remember(mMemories)
          isRememberedRandom = false
        }
        
        else if (!isRememberedRandom){
          // Clear memories
          reminiscenceHelper.forget()
          
          // Remember random memory
          rememberRandom()
        }
  
  
  
        // Show ad
        if (mInterstitialAd.isLoaded) {
          mInterstitialAd.show()
        } else {
          Log.d(TAG, "The interstitial wasn't loaded yet.")
        }
        
        
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      }

    })
  }





  private fun setupSearchResultsContainer() {
    reminiscenceHelper = ReminiscenceHelper(this, searchResultsContainer, supportFragmentManager)
    reminiscenceHelper.onTagClickListener = object : ReminiscenceHelper.OnTagClickListener {
      override fun onClick(tag: String) {
        val currentSearch = searchEditText.text.toString()
        if (currentSearch != tag) searchEditText.setText(tag)
      }
    }
    
    
    // Remember random memory
    rememberRandom()
    
    
    // Hide keyboard and search edit text
    var firstTouchY = 0
    val density = resources.displayMetrics.density
    
    scrollView.setOnTouchListener { _, event ->
      hideKeyboard(true)
      
      when(event.action){
        MotionEvent.ACTION_DOWN -> {
          firstTouchY = event.y.toInt()
          //Log.d(TAG, "firstTouchY: $firstTouchY")
        }
        
        MotionEvent.ACTION_MOVE -> {
          val currentTouchY = event.y.toInt()
          val distance = abs(currentTouchY - firstTouchY)
          //Log.d(TAG, "scrollY: $currentTouchY")
          
          if (distance > 30 * density){
            if (firstTouchY > currentTouchY && !isEditTextHidden && !isEditTextAnimating){
              hideEditText(true)
            }
            
            else if (firstTouchY < currentTouchY && isEditTextHidden && !isEditTextAnimating) {
              hideEditText(false)
            }
          }
        }
      }
      
      false
    }
  }
  
  
  
  
  
  private fun hideEditText(hide: Boolean){
    
    isEditTextAnimating = true
    val searchEditTextHeight = resources.getDimension(R.dimen.search_edit_text_height)
    val valueAnimator = if (hide){
      ValueAnimator.ofFloat(searchEditTextHeight, 0f)
    } else {
      ValueAnimator.ofFloat(0f, searchEditTextHeight)
    }
    
    valueAnimator.interpolator = LinearInterpolator()
    valueAnimator.duration = 300
    valueAnimator.doOnEnd {
      isEditTextAnimating = false
      isEditTextHidden = hide
    }
    
    valueAnimator.addUpdateListener {
      val value = it.animatedValue as Float
      val params = searchContainer.layoutParams
      params.height = value.toInt()
      searchContainer.layoutParams = params
      //Log.d(TAG, "searchEditTextHeight: ${value.toInt()}")
    }
    
    valueAnimator.start()
  }





  fun hideKeyboard(hide: Boolean) {
    if (hide) {
      val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
      inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
    else {
      val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
      searchEditText.requestFocus()
      inputMethodManager.showSoftInput(searchEditText, 0)
    }
  }
  
  
  
  
  
  private fun showIntro(){
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainer, OpeningFragment(), "OpeningFragment")
      .addToBackStack("OpeningFragment")
      .commit()
  }
  
  
  
  
  
  private fun rememberRandom(){
    val database = MemoryDatabase(this, null)
    val randomMemory = database.rememberRandomMemory()
    database.close()
    if (randomMemory != null) {
      mMemories.clear()
      mMemories.add(randomMemory)
      reminiscenceHelper.remember(mMemories)
      isRememberedRandom = true
    }
  }
  
  
  
  
  
  private fun updatemMemories(id: Int){
    
    // Get the index of the memory to update
    var index = -1
    for (i in 0 until mMemories.size){
      if (mMemories[i].id == id) index = i
      break
    }
    
    // if there is a memory with the same id, update it
    if (index > -1){
      val updatedMemory = MemoryDatabase(this, null).remember(id)
      mMemories[index] = updatedMemory
    }
  }
  
  
  
  
  
  private fun setupAds(){
    
    MobileAds.initialize(this, resources.getString(R.string.ad_app_id))
    
    // Banner ad
    val mAdView = AdView(this)
    mAdView.adSize = AdSize.BANNER
    mAdView.adUnitId = resources.getString(R.string.banner_ad)
    adBannerContainer.addView(mAdView)
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
