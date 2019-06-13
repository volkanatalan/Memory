package com.volkanatalan.memory.sections

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.volkanatalan.memory.R
import com.volkanatalan.memory.models.Memory
import kotlinx.android.synthetic.main.activity_add_memory.view.*
import kotlinx.android.synthetic.main.list_item_tag_container.view.*

class TagSection(root: View,
                 memory: Memory,
                 isEditing: Boolean ) {
  
  
  private val mContext = root.context
  private val mRoot = root
  private val mMemory = memory
  private val mIsEditing = isEditing
  
  
  
  
  fun setup(){
    if (mIsEditing){
      for (tag in mMemory.tags)
        addTagToContainer(tag)
    }
  
  
    mRoot.addTagImageView.setOnClickListener{
      
      when (val tag = getTag()) {
        "" -> Toast.makeText(mContext, "Tag cannot be empty!", Toast.LENGTH_SHORT).show()
        
        in mMemory.tags -> Toast.makeText(mContext, "Same tag!", Toast.LENGTH_SHORT).show()
        
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
  
  
  
  
  fun getTag(): String{
    return mRoot.tagEditText.text.toString()
  }
}