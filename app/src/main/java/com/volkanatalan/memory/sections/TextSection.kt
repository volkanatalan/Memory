package com.volkanatalan.memory.sections

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.AddMemoryView
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
  
    setupTitleEditText()
    setupTextEditText()
  }
  
  
  
  
  private fun setupTitleEditText(){
    mRoot.titleEditText.addTextChangedListener { text ->
      // Paste without rich text formatting
      AddMemoryView.removeSpans(text!!)
    }
  }
  
  
  
  
  private fun setupTextEditText(){
    mRoot.textEditText.addTextChangedListener { text ->
      // Paste without rich text formatting
      AddMemoryView.removeSpans(text!!)
    }
  }
}