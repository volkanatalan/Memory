package com.volkanatalan.memory.sections

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.volkanatalan.memory.activities.AddMemoryActivity
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.AddMemoryView
import kotlinx.android.synthetic.main.activity_add_memory.view.*

/**
 * Sets up text section of [AddMemoryActivity].
 */
class TextSection(root: View, isEditing: Boolean, editMemory: Memory?) {
  
  
  private val mRoot = root
  private val mIsEditing = isEditing
  private val mEditMemory: Memory? = editMemory
  
  
  
  
  /**
   * Start setting up the text section of [AddMemoryActivity].
   */
  fun setup(){
    if (mIsEditing && mEditMemory != null) {
      mRoot.titleEditText.setText(mEditMemory.title)
      mRoot.textEditText.setText(mEditMemory.text)
    }
  
    setupTitleEditText()
    setupTextEditText()
  }
  
  
  
  
  /**
   * Remove rich text format, when user paste a text.
   */
  private fun setupTitleEditText(){
    mRoot.titleEditText.addTextChangedListener { text ->
      // Paste without rich text formatting
      AddMemoryView.removeSpans(text!!)
    }
  }
  
  
  
  
  /**
   * Remove rich text format, when user paste a text.
   */
  private fun setupTextEditText(){
    mRoot.textEditText.addTextChangedListener { text ->
      // Paste without rich text formatting
      AddMemoryView.removeSpans(text!!)
    }
  }
}