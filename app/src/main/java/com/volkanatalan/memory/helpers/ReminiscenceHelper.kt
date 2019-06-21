package com.volkanatalan.memory.helpers

import android.content.Context
import android.widget.LinearLayout
import com.volkanatalan.memory.interfaces.SearchViewInterface
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.sections.memory_view.*
import com.volkanatalan.memory.views.MemoryView


/**
 * ReminiscenceHelper helps to show memory views to user.
 * @param context
 * @param container A view into which memory views to add
 * @param listener A listener to handle user interactions
 */
class ReminiscenceHelper( context: Context,
                          container: LinearLayout,
                          listener:SearchViewInterface.Listener ) {
  
  
  private val TAG = "ReminiscenceHelper"
  private val mContext = context
  private val mContainer = container
  private val mListener = listener
  lateinit var onTagClickListener: TagSection.OnTagClickListener
  
  
  /**
   * Add memory views to container.
   */
  fun remember(memories: MutableList<Memory>): ReminiscenceHelper {

    // Clear the container
    mContainer.removeAllViews()

    // Add memories to container
    for (memory in memories) {
      
      // Get memory view
      val memoryView = MemoryView(mContext, memory, mContainer, memories, mListener, onTagClickListener)
      
      // Add memory view to container
      mContainer.addView(memoryView.rootView)
    }

    return this
  }
  
  
  
  
  /**
   * Clear all views from container.
   */
  fun forget(){
    mContainer.removeAllViews()
  }
}