package com.volkanatalan.memory.interfaces

import android.view.View

interface AddMemoryViewInterface {
  
  
  /**
   * Returns the root view, i.e, the inflated layout file
   */
  fun getRootView(): View
  
  
  
  interface Listener {
  
    fun onAddImage()
  
    fun onAddDocument()
    
    fun afterSaveMemory(isEditing: Boolean, memoryId: Int)
  }
}