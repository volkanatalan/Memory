package com.volkanatalan.memory.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.volkanatalan.memory.R
import com.volkanatalan.memory.adapters.AdapterTagsActivityListView
import com.volkanatalan.memory.databases.MemoryDatabase
import kotlinx.android.synthetic.main.activity_searchables.*

/**
 * SearchablesActivity contains a list of saved memory titles.
 * User can click on one of them to see its details on [MainActivity].
 */
class SearchablesActivity : AppCompatActivity() {
  
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_searchables)
  
    // Take saved memories from database
    val database = MemoryDatabase(this, null)
    val adapter = AdapterTagsActivityListView(this, database.getTitles())
    list_view.adapter = adapter
    list_view.setOnItemClickListener { _, _, position, _ ->
      
      // Get clicked tag
      val searchItem = adapter.getItem(position)
      
      // Send the tag to MainActivity
      val intent = Intent()
      intent.putExtra("searchItem", searchItem)
      setResult(Activity.RESULT_OK, intent)
      finish()
    }
  }
}
