package com.volkanatalan.memory.activities

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
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

    setSupportActionBar(toolbar)

    setupSearchEditText()
    setupSearchResultsContainer()
  }





  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)

    if (resultCode == Activity.RESULT_OK && intent != null) {

      if (requestCode == EDIT_MEMORY) {
        reminiscenceHelper.editMemory(intent.getSerializableExtra("editMemory") as Memory)
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
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      scrollView.setOnScrollChangeListener{ v, _, _, _, _->
        hideKeyboard(v as View)
      }
    }
    else {
      scrollView.setOnTouchListener { v, event ->
        hideKeyboard(v as View)
        
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
    valueAnimator.duration = 500
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





  private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
  }
}
