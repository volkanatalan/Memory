package com.volkanatalan.memory.interfaces

import android.content.Intent
import android.view.View
import com.volkanatalan.memory.fragments.DeleteConfirmationFragment
import com.volkanatalan.memory.helpers.ReminiscenceHelper
import com.volkanatalan.memory.models.Memory

interface SearchViewInterface {
  
  /**
   * Returns the root view, i.e, the inflated layout file.
   */
  fun getRootView(): View
  
  
  /**
   * Update a memory whose id is the same with the given parameter.
   * @param id Id of the view to update
   */
  fun updateMemory(id: Int)
  
  
  /**
   * Get the text, user typed into search bar to search for memories.
   */
  fun getSearchText(): String
  
  
  /**
   * Replace the text of search bar with the text, given with the parameter.
   * @param text Text to write into the search bar
   */
  fun setSearchText(text: String)
  
  
  /**
   * Show/hide interlayer which highlights [DeleteConfirmationFragment].
   * @param show true: show, false: hide
   */
  fun showInterlayer(show: Boolean)
  
  
  
  interface Listener{
    
    fun onClickSearchButton()
  
    fun onClickDeleteButton(fragment: DeleteConfirmationFragment)
    
    fun onClickEditButton(intent: Intent)
    
    fun onRemember(reminiscenceHelper: ReminiscenceHelper, memories: MutableList<Memory>)
  }
}