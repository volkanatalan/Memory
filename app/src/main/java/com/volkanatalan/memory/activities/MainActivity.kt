package com.volkanatalan.memory.activities

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.animation.doOnEnd
import com.volkanatalan.memory.R
import com.volkanatalan.memory.helpers.ReminiscenceHelper
import com.volkanatalan.memory.classes.Memory
import com.volkanatalan.memory.databases.MemoryDatabase
import com.volkanatalan.memory.fragments.OpeningFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {


  private var TAG = "MainActivity"
  private var mMemories = mutableListOf<Memory>()
  private lateinit var reminiscenceHelper: ReminiscenceHelper
  private val EDIT_MEMORY = 102
  private var isEditTextAnimating = false
  private var isEditTextHidden = false
  
  
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainer, OpeningFragment(), "OpeningFragment")
      .addToBackStack("OpeningFragment")
      .commit()

    setSupportActionBar(toolbar)

    setupSearchEditText()
    setupSearchResultsContainer()
  }





  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)

    if (resultCode == Activity.RESULT_OK && intent != null) {

      if (requestCode == EDIT_MEMORY) {
        val memoryId = intent.getIntExtra("updateMemory", -1)
        if (memoryId > -1) {
          val database = MemoryDatabase(this@MainActivity, null)
          mMemories = database.rememberMemories(searchEditText.text.toString())
          database.close()
          reminiscenceHelper.remember(mMemories)
        }
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
    searchEditText.addTextChangedListener(object:TextWatcher{
      override fun afterTextChanged(s: Editable?) {
        val searchText = s.toString()

        if (searchText.length > 1) {
          val database = MemoryDatabase(this@MainActivity, null)
          mMemories = database.rememberMemories(searchText)
          database.close()
          reminiscenceHelper.remember(mMemories)
        }
        else reminiscenceHelper.forget()
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
    
    
    // Hide keyboard and search edit text
    var firstTouchY = 0
    val density = resources.displayMetrics.density
    
    
    scrollView.setOnTouchListener { v, event ->
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
      val params = searchEditText.layoutParams
      params.height = value.toInt()
      searchEditText.layoutParams = params
      Log.d(TAG, "searchEditTextHeight: ${value.toInt()}")
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
}
