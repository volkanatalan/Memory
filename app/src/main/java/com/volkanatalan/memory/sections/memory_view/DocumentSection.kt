package com.volkanatalan.memory.sections.memory_view

import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import com.volkanatalan.memory.R
import com.volkanatalan.memory.helpers.ShareHelper
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.DocumentView
import com.volkanatalan.memory.views.MemoryView
import kotlinx.android.synthetic.main.view_memory.view.*
import kotlin.math.ceil
import kotlin.math.roundToInt


/**
 * Sets up the document section of [MemoryView].
 */
class DocumentSection (rootView: LinearLayout, val memory: Memory) {
  
  
  private val mContext = rootView.context
  private val mDocuments = memory.documents
  private val mDocumentBase = rootView.documentBase
  private val mDocumentContainer = rootView.documentContainer
  
  
  
  /**
   * Start setting up the document section of [MemoryView].
   */
  fun setup(){
    if (mDocuments.size > 0) {
      
      mDocumentContainer.removeAllViews()
      mDocumentBase.visibility = View.VISIBLE
      
      
      // Calculate column and row numbers
      val screenWidth = mContext.resources.displayMetrics.widthPixels
      val pagePadding = mContext.resources.getDimensionPixelSize(R.dimen.page_padding) * 2
      val memoryPadding = mContext.resources.getDimensionPixelSize(R.dimen.section_padding) * 2
      val documentContainerPadding = mContext.resources.getDimensionPixelSize(R.dimen.subsection_padding) * 2
      val freeSpaceForContainer = screenWidth - pagePadding - memoryPadding - documentContainerPadding
      
      val documentWidth = mContext.resources.getDimensionPixelSize(R.dimen.document_width)
      val documentMargin = mContext.resources.getDimensionPixelSize(R.dimen.grid_margin)
      val totalDocumentWidth = documentWidth + (documentMargin * 2)
      
      val columnCount = freeSpaceForContainer / totalDocumentWidth
      val rowCount = ceil(mDocuments.size.toFloat() / columnCount.toFloat()).roundToInt()
      
      mDocumentContainer.columnCount = columnCount
      mDocumentContainer.rowCount = rowCount
      
      // Center container horizontal
      val containerStartMargin = (freeSpaceForContainer % totalDocumentWidth) / 2
      (mDocumentContainer.layoutParams as LinearLayout.LayoutParams).marginStart = containerStartMargin
      
      
      for ((index, document) in mDocuments.withIndex()){
        
        // Calculate column and row specs of document
        val columnSpec = index % columnCount
        val rowSpec = index / columnCount
        
        // Setup document view layout params
        val param = GridLayout.LayoutParams()
        param.height = GridLayout.LayoutParams.WRAP_CONTENT
        param.width = GridLayout.LayoutParams.WRAP_CONTENT
        param.setGravity(Gravity.CENTER)
        param.columnSpec = GridLayout.spec(columnSpec)
        param.rowSpec = GridLayout.spec(rowSpec)
        param.setMargins(documentMargin, documentMargin, documentMargin, documentMargin)
        
        // Create document view
        val documentView = DocumentView(mContext, document).rootView
        documentView.layoutParams = param
        
        // Set on click listener to document view to open it with another application
        documentView.setOnClickListener {
          val parentOfDocument = it.parent as GridLayout
          val documentIndex = parentOfDocument.indexOfChild(it)
          val path = mDocuments[documentIndex]
    
          ShareHelper(mContext).shareFile(path)
        }
        
        
        mDocumentContainer.addView(documentView.rootView)
      }
    }
  }
}