package com.volkanatalan.memory.sections.memory_view

import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.volkanatalan.memory.R
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.MemoryView
import com.volkanatalan.memory.views.TagView
import kotlinx.android.synthetic.main.view_memory.view.*
import kotlinx.android.synthetic.main.view_tag.view.*


/**
 * Sets up the tag section of [MemoryView].
 */
class TagSection (rootView: LinearLayout,
                  memory: Memory,
                  private var onTagClickListener: OnTagClickListener ) {
  
  
  private val mContext = rootView.context
  private val mTags = memory.tags
  private val mTagContainer = rootView.tagContainer
  
  
  
  
  /**
   * Start setting up the tag section of [MemoryView].
   */
  fun setup(){
    mTagContainer.removeAllViews()
    
    
    // Firstly add "Tags:" label
    val tagsLabelTextView = TextView(mContext)
    tagsLabelTextView.setPadding(0, 3, 5, 0)
    val tagsLabel = mContext.resources.getString(R.string.tags_semicolon)
    tagsLabelTextView.text = tagsLabel
    
    val labelView = LinearLayout(mContext)
    labelView.addView(tagsLabelTextView)
    mTagContainer.addView(labelView)
    
    
    // Then add the tag(s)
    for (tag in mTags) {
      
      val tagView = TagView(mContext, tag).rootView
  
      tagView.setOnClickListener{
        val textView = it.textView
        val text = textView.text.toString()
        onTagClickListener.onClick(text)
      }
      
      mTagContainer.addView(tagView)
    }
  }
  
  
  
  /**
   * A listener to handle user's click events on tags.
   */
  interface OnTagClickListener{
    fun onClick(tag: String)
  }
}