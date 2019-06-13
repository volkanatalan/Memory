package com.volkanatalan.memory.sections

import android.view.View
import com.volkanatalan.memory.models.Memory
import kotlinx.android.synthetic.main.activity_add_memory.view.*

class TextSection(root: View, isEditing: Boolean, editMemory: Memory?) {
  
  
  private val mRoot = root
  private val mIsEditing = isEditing
  private val mEditMemory: Memory? = editMemory
  
  
  
  fun setup(){
    if (mIsEditing && mEditMemory != null) {
      mRoot.titleEditText.setText(mEditMemory.title)
      mRoot.textEditText.setText(mEditMemory.text)
    }
  }
}