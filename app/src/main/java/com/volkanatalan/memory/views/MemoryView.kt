package com.volkanatalan.memory.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.volkanatalan.memory.R
import com.volkanatalan.memory.interfaces.SearchViewInterface
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.sections.memory_view.TagSection.OnTagClickListener
import com.volkanatalan.memory.sections.memory_view.*

class MemoryView( val context: Context,
                  val memory: Memory,
                  val container: LinearLayout,
                  val memories: MutableList<Memory>,
                  val listener: SearchViewInterface.Listener,
                  onTagClickListener: OnTagClickListener ) {
  
  
  private val TAG = "MemoryView"
  var rootView: LinearLayout
  
  
  
  init {
    
    // Inflate root view
    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val margin = context.resources.getDimensionPixelSize(R.dimen.memory_top_bottom_margin)
    params.setMargins(0, margin, 0, margin)
    rootView = LinearLayout(context)
    rootView.layoutParams = params
    rootView = LayoutInflater.from(context).inflate(R.layout.view_memory, rootView) as LinearLayout
  
  
    // Setup sections
    TextSection(rootView, memory).setup()
    ImageSection(rootView, memory).setup()
    LinkSection(rootView, memory).setup()
    DocumentSection(rootView, memory).setup()
    TagSection(rootView, memory, onTagClickListener).setup()
    ButtonSection(container, rootView, memories, listener).setup()
  
  }
}