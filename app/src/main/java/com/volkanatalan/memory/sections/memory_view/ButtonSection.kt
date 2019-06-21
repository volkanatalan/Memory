package com.volkanatalan.memory.sections.memory_view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.volkanatalan.memory.activities.AddMemoryActivity
import com.volkanatalan.memory.activities.MainActivity
import com.volkanatalan.memory.databases.MemoryDatabase
import com.volkanatalan.memory.fragments.DeleteConfirmationFragment
import com.volkanatalan.memory.interfaces.SearchViewInterface
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.MemoryView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_memory.view.*
import java.io.File



/**
 * Sets up the button section of [MemoryView].
 */
class ButtonSection ( val memoryViewContainer: LinearLayout,
                      val rootView: LinearLayout,
                      val memories: MutableList<Memory>,
                      var listener: SearchViewInterface.Listener ) {
  
  
  
  private val mContext = rootView.context
  private val mDeleteButton = rootView.deleteButtonImageView
  private val mEditButton = rootView.editButtonImageView
  
  
  
  
  
  
  /**
   * Start setting up the button section of [MemoryView].
   */
  fun setup(){
  
    mDeleteButton.setOnClickListener {
      mContext.hideKeyboard(mDeleteButton)
      
      (mContext as MainActivity).interlayer.visibility = View.VISIBLE
      
      val memoryView = mDeleteButton.parent.parent.parent as LinearLayout
      val position = memoryViewContainer.indexOfChild(memoryView)
      val memory = memories[position]
      
      val deleteConfirmationFragment = DeleteConfirmationFragment.newInstance(memory)
      deleteConfirmationFragment.onDeleteClickListener = object : DeleteConfirmationFragment.OnDeleteClickListener{
        override fun onClick(memory: Memory) {
          
          // Delete files from storage
          for (image in memory.images) deleteFilesFromStorage(image)
          for (doc in memory.documents) deleteFilesFromStorage(doc)
          
          // Delete memory from database
          val database = MemoryDatabase(mContext, null)
          database.forget(memory)
          
          // Remove memory from list
          memories.removeAt(position)
          
          // Finally remove memory from memory container linear layout
          memoryViewContainer.removeView(memoryView)
        }
      }
      
      listener.onClickDeleteButton(deleteConfirmationFragment)
    }
  
  
    mEditButton.setOnClickListener {
      mContext.hideKeyboard(mEditButton)
      
      val memoryView = mDeleteButton.parent.parent.parent as LinearLayout
      val position = memoryViewContainer.indexOfChild(memoryView)
      val memory = memories[position]
      
      val intent = Intent(mContext, AddMemoryActivity::class.java)
      intent.putExtra("editMemory", memory.id)
      listener.onClickEditButton(intent)
    }
  }
  
  
  
  
  
  private fun deleteFilesFromStorage(path: String){
    val file = File(path)
    if (file.exists()) file.delete()
  }
  
  
  
  
  
  private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
  }
}