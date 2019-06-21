package com.volkanatalan.memory.sections.memory_view

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.volkanatalan.memory.R
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.MemoryView
import kotlinx.android.synthetic.main.view_tag.view.*


/**
 * Sets up the tag section of [MemoryView].
 */
class TagSection (val rootView: LinearLayout, val memory: Memory, var onTagClickListener: OnTagClickListener) {
  
  
  private val mContext = rootView.context
  private val mTags = memory.tags
  
  
  
  
  /**
   * Start setting up the tag section of [MemoryView].
   */
  fun setup(){
    rootView.removeAllViews()
    
    
    val layoutParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )
    layoutParams.setMargins(0, 0, 5, 5)
    
    
    // Firstly add "Tags:" label
    val tagsLabelTextView = TextView(mContext)
    tagsLabelTextView.setPadding(0, 5, 5, 0)
    val tagsLabel = mContext.resources.getString(R.string.tags_semicolon)
    tagsLabelTextView.text = tagsLabel
    
    val labelLayout = LinearLayout(mContext)
    labelLayout.addView(tagsLabelTextView, layoutParams)
    rootView.addView(labelLayout)
    
    
    // Then add the tag(s)
    for (tag in mTags) {
      val tagLayout = LinearLayout(mContext)
      
      val view = LayoutInflater.from(mContext).inflate(R.layout.view_tag, null)
      val textView = view.textView
      
      textView.text = tag
      
      view.setOnClickListener{
        val text = ((it as RelativeLayout).getChildAt(0) as TextView).text.toString()
        onTagClickListener.onClick(text)
      }
      
      tagLayout.addView(view, layoutParams)
      rootView.addView(tagLayout)
    }
  }
  
  
  
  /**
   * A listener to handle user's click events on tags.
   */
  interface OnTagClickListener{
    fun onClick(tag: String)
  }
}