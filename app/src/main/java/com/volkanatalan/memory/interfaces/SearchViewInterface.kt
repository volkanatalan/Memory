package com.volkanatalan.memory.interfaces

import android.content.Intent
import android.view.View
import com.volkanatalan.memory.fragments.DeleteConfirmationFragment
import com.volkanatalan.memory.helpers.ReminiscenceHelper
import com.volkanatalan.memory.models.Memory

interface SearchViewInterface {
  
  /**
   * Returns the root view, i.e, the inflated layout file
   */
  fun getRootView(): View
  
  fun updateMemory(id: Int)
  
  fun getSearchText(): String
  
  fun setSearchText(text: String)
  
  fun showInterlayer(show: Boolean)
  
  
  
  interface Listener{
    
    fun onClickSearchButton()
  
    fun onClickDeleteButton(fragment: DeleteConfirmationFragment)
    
    fun onClickEditButton(intent: Intent)
    
    fun onRemember(reminiscenceHelper: ReminiscenceHelper, memories: MutableList<Memory>)
    
    fun afterSearch()
  }
}