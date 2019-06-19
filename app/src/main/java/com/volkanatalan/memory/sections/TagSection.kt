package com.volkanatalan.memory.sections

import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.volkanatalan.memory.R
import com.volkanatalan.memory.activities.AddMemoryActivity
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.AddMemoryView
import kotlinx.android.synthetic.main.activity_add_memory.view.*
import kotlinx.android.synthetic.main.list_item_tag_container.view.*

/**
 * Sets up tag section of [AddMemoryActivity].
 */
class TagSection(root: View,
                 memory: Memory,
                 isEditing: Boolean ) {
  
  
  private val mContext = root.context
  private val mRoot = root
  private val mMemory = memory
  private val mIsEditing = isEditing
  
  
  
  
  /**
   * Start setting up the tag section of [AddMemoryActivity].
   */
  fun setup(){
    // If it is in editing mode, add tags of the memory to the container.
    if (mIsEditing){
      for (tag in mMemory.tags)
        addTagToContainer(tag)
    }
  
  
    // Paste without rich text formatting
    mRoot.tagEditText.addTextChangedListener { text ->
      AddMemoryView.removeSpans(text!!)
    }
  
  
    mRoot.addTagImageView.setOnClickListener{
      
      when (val tag = getTag()) {
        // If an empty tag is tried to be added, inform user that this can not be done.
        "" -> Toast.makeText(mContext, "Tag cannot be empty!", Toast.LENGTH_SHORT).show()
        
        // If a tag already exists in memory tag, inform user.
        in mMemory.tags -> Toast.makeText(mContext, "Same tag!", Toast.LENGTH_SHORT).show()
        
        // Else add the tag to container.
        else -> {
          mMemory.tags.add(tag)
          
          addTagToContainer(tag)
  
          // Clear tag edit text
          mRoot.tagEditText.setText("")
        }
      }
    }
  }
  
  
  
  
  private fun addTagToContainer(tag: String){
    val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_tag_container, null)
    view.id = mRoot.tagContainer.childCount
    
    val tagTextView = view.tagTextView
    tagTextView.text = tag
    
    val deleteButton = view.deleteImageView
    deleteButton.setOnClickListener{
      val listRow = it.parent as LinearLayout
      val tagName = (listRow.getChildAt(0) as TextView).text
      
      mMemory.tags.remove(tagName)
  
      mRoot.tagContainer.removeView(listRow)
    }
  
    mRoot.tagContainer.addView(view)
  }
  
  
  /**
   * Get the text from tag [EditText]
   */
  fun getTag(): String{
    return mRoot.tagEditText.text.toString()
  }
}