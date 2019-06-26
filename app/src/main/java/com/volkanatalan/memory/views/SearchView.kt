package com.volkanatalan.memory.views

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.core.animation.doOnEnd
import com.volkanatalan.memory.R
import com.volkanatalan.memory.activities.MainActivity
import com.volkanatalan.memory.databases.MemoryDatabase
import com.volkanatalan.memory.fragments.DeleteConfirmationFragment
import com.volkanatalan.memory.helpers.ReminiscenceHelper
import com.volkanatalan.memory.interfaces.SearchViewInterface
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.sections.memory_view.TagSection
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.math.abs

/**
 * SearchView is the view part of [MainActivity].
 */
class SearchView( inflater : LayoutInflater,
                  listener: SearchViewInterface.Listener): SearchViewInterface {
  
  private val TAG = "SearchView"
  private val mRootView = inflater.inflate(R.layout.activity_main, null)
  private val mContext = mRootView.context
  private val mListener = listener
  private var mMemoryDatabase: MemoryDatabase
  private var mMemories = mutableListOf<Memory>()
  private lateinit var mReminiscenceHelper: ReminiscenceHelper
  private var mIsEditTextHidden = false
  private var mIsRememberedRandom = false
  
  
  
  
  init {
    mMemoryDatabase = MemoryDatabase(mContext, null)
    
    setupSearchEditText()
    setupSearchResultsContainer()
  }
  
  
  
  
  
  override fun getRootView(): View {
    return mRootView
  }
  
  
  
  
  
  override fun updateMemory(id: Int){
    
    // Get the index of the memory to update
    var index = -1
    for (i in 0 until mMemories.size){
      if (mMemories[i].id == id) {
        index = i
        break
      }
    }
    
    // if there is a memory with the same id, update it
    if (index > -1){
      val updatedMemory = mMemoryDatabase.remember(id)
      mMemories[index] = updatedMemory
    }
  
    mReminiscenceHelper.remember(mMemories)
  }
  
  
  
  
  
  private fun setupSearchEditText() {
    
    // Show saved tags and titles on click search button
    mRootView.search_image_view.setOnClickListener {
      mListener.onClickSearchButton()
    }
    
    
    // Clear edit text when clicked on x button
    mRootView.clear_search.setOnClickListener {
      mRootView.search_edit_text.setText("")
    }
    
    // Set on text changed listener to edit text
    mRootView.search_edit_text.addTextChangedListener(object: TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        var searchText = s.toString()
        
        // If search text length is bigger than 1 and last character
        // of it is not space, search for the records on database
        if (searchText.length > 1 && searchText.last() != ' ') {
          
          while (searchText.contains("'")){
            val index = searchText.indexOf("'")
            searchText = searchText.substring(0, index) + " " + searchText.substring(index + 1, searchText.length)
          }
          
          
          remember(searchText).start()
          
        }
        
        else if (!mIsRememberedRandom){
          // Clear memories
          mReminiscenceHelper.forget()
          
          // Remember random memory
          rememberRandom()
        }
        
      }
      
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
      }
      
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      }
      
    })
  }
  
  
  
  
  /**
   * Add found memories to search results container.
   * @param text Text to find memories accordingly.
   */
  private fun remember(text: String) = Thread( Runnable {
    mMemories = mMemoryDatabase.rememberMemories(text)
    mListener.onRemember(mReminiscenceHelper, mMemories)
    mIsRememberedRandom = false
  })
  
  
  
  
  
  private fun setupSearchResultsContainer() {
    mReminiscenceHelper = ReminiscenceHelper(mContext, mRootView.search_results_container, mListener)
    mReminiscenceHelper.onTagClickListener = object : TagSection.OnTagClickListener {
      override fun onClick(tag: String) {
        val currentSearch = mRootView.search_edit_text.text.toString()
        if (currentSearch != tag) mRootView.search_edit_text.setText(tag)
      }
    }
    
    
    // Remember random memory
    rememberRandom()
    
    
    // Hide keyboard and search edit text
    val density = mContext.resources.displayMetrics.density
    val firstTouchList = mutableListOf<Int>()
  
    mRootView.scroll_view.setOnTouchListener { _, event ->
      hideKeyboard(true)
      
      when(event.action){
        MotionEvent.ACTION_MOVE -> {
          val isNeedScroll = mRootView.scroll_view.height < mRootView.search_results_container.height
          val currentTouchY = event.y.toInt()
          if (firstTouchList.size == 0) firstTouchList.add(currentTouchY)
          val firstTouchY = firstTouchList[0]
          val distance = abs(currentTouchY - firstTouchY)
          
          //Log.d(TAG, "firstTouchY: $firstTouchY")
          //Log.d(TAG, "currentTouchY: $currentTouchY")
          //Log.d(TAG, "distance: $distance")
          //Log.d(TAG, "scroll_view.height: ${scroll_view.height}")
          //Log.d(TAG, "search_results_container.height: ${search_results_container.height}")
          //Log.d(TAG, "isNeedScroll: $isNeedScroll")
          
          if (distance > 30 * density){
            if (firstTouchY > currentTouchY && !mIsEditTextHidden && isNeedScroll){
              hideEditText(true)
            }
            
            else if (firstTouchY < currentTouchY && mIsEditTextHidden) {
              hideEditText(false)
            }
          }
        }
        
        MotionEvent.ACTION_UP -> {
          //Log.d(TAG, "ACTION_UP")
          firstTouchList.clear()
        }
      }
      
      false
    }
  }
  
  
  
  
  /**
   * Add a random memory to search results container.
   */
  private fun rememberRandom(){
    val randomMemory = mMemoryDatabase.rememberRandomMemory()
    if (randomMemory != null) {
      mMemories.clear()
      mMemories.add(randomMemory)
      mReminiscenceHelper.remember(mMemories)
      mIsRememberedRandom = true
    }
  }
  
  
  
  
  
  override fun getSearchText(): String {
    return mRootView.search_edit_text.text.toString()
  }
  
  
  
  
  
  override fun setSearchText(text: String) {
    mRootView.search_edit_text.setText(text)
  }
  
  
  
  
  /**
   * Show/hide interlayer that highlights [DeleteConfirmationFragment].
   */
  override fun showInterlayer(show: Boolean) {
    val visibility: Int = if (show) View.VISIBLE
    else View.GONE
    
    mRootView.interlayer.visibility = visibility
  }
  
  
  
  
  /**
   * Hide search bar when scrolling down, and show it when scrolling up.
   * @param hide true: hide, false: show
   */
  private fun hideEditText(hide: Boolean){
    
    val searchEditTextHeight = mContext.resources.getDimension(R.dimen.search_edit_text_height)
    val searchEditTextCurrentHeight = mRootView.search_container.height.toFloat()
    val valueAnimator = if (hide){
      ValueAnimator.ofFloat(searchEditTextCurrentHeight, 0f)
    } else {
      ValueAnimator.ofFloat(searchEditTextCurrentHeight, searchEditTextHeight)
    }
    
    valueAnimator.interpolator = LinearInterpolator()
    valueAnimator.duration = 300
    valueAnimator.doOnEnd {
      mIsEditTextHidden = hide
    }
    
    valueAnimator.addUpdateListener {
      val value = it.animatedValue as Float
      val params = mRootView.search_container.layoutParams
      params.height = value.toInt()
      mRootView.search_container.layoutParams = params
      //Log.d(TAG, "searchEditTextHeight: ${value.toInt()}")
    }
    
    valueAnimator.start()
  }
  
  
  
  
  
  fun hideKeyboard(hide: Boolean) {
    if (hide) {
      val inputMethodManager = mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
      inputMethodManager.hideSoftInputFromWindow(mRootView.search_edit_text.windowToken, 0)
    }
    else {
      val inputMethodManager = mContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
      mRootView.search_edit_text.requestFocus()
      inputMethodManager.showSoftInput(mRootView.search_edit_text, 0)
    }
  }
}