package com.volkanatalan.memory.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.volkanatalan.memory.R
import com.volkanatalan.memory.helpers.ReminiscenceHelper
import com.volkanatalan.memory.classes.Memory
import com.volkanatalan.memory.databases.MemoryDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private var TAG = "MainActivity"
  private var mMemories = mutableListOf<Memory>()
  private lateinit var adapter: ReminiscenceHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)

    setupSearchEditText()
    setupSearchResultsContainer()
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

    when (searchText) {
      "" -> super.onBackPressed()

      else -> searchEditText.setText("")
    }
  }





  private fun setupSearchEditText() {
    searchEditText.addTextChangedListener(object:TextWatcher{
      override fun afterTextChanged(s: Editable?) {
        val searchText = s.toString()

        if (searchText.length > 1) {
          val database = MemoryDatabase(this@MainActivity, null)
          mMemories = database.getMemories(searchText)
          adapter.remember(mMemories)
        }
        else adapter.forget()
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      }

    })
  }





  private fun setupSearchResultsContainer() {
    adapter = ReminiscenceHelper(this, searchResultsContainer)


    adapter.onTagClickListener = object : ReminiscenceHelper.OnTagClickListener {
      override fun onClick(tag: String) {
        searchEditText.setText(tag)
      }
    }

    scrollView.setOnTouchListener(object : View.OnTouchListener{
      override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        hideKeyboard(v as View)
        return false
      }
    })
  }





  fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
  }
}
