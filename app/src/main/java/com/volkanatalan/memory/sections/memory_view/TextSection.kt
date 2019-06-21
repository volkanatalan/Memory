package com.volkanatalan.memory.sections.memory_view

import android.view.View
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.MemoryView
import kotlinx.android.synthetic.main.view_memory.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Sets up the text section of [MemoryView].
 */
class TextSection(rootView: View, val memory: Memory) {
  
  val titleTextView = rootView.titleTextView
  val textTextView = rootView.textTextView
  val dateTextView = rootView.dateTextView
  
  
  
  /**
   * Start setting up the text section of [MemoryView].
   */
  fun setup(){
    // Setup title
    titleTextView.text = memory.title
  
    // Setup date
    val dateText = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault()).format(memory.date)
    dateTextView.text = dateText
    
    // Setup text
    if (memory.text != "") {
      textTextView.visibility = View.VISIBLE
      textTextView.text = memory.text
    }
  }
}