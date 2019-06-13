package com.volkanatalan.memory.interfaces

import android.view.View

interface AddMemoryInterface {
  
  
  /**
   * Returns the root view, i.e, the inflated layout file
   */
  fun getRootView(): View
  
  
  
  interface MemoryAddListener {
  
    fun onAddImage()
  
    fun onAddDocument()
    
    fun afterSaveMemory(isEditing: Boolean, memoryId: Int)
  }
  
  
  
  fun setOnSaveMemoryListener(listener: MemoryAddListener)
}