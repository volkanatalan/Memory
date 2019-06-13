package com.volkanatalan.memory.sections

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.volkanatalan.memory.R
import com.volkanatalan.memory.helpers.FileIconHelper
import com.volkanatalan.memory.interfaces.AddMemoryInterface
import com.volkanatalan.memory.models.Memory
import kotlinx.android.synthetic.main.activity_add_memory.view.*
import kotlinx.android.synthetic.main.list_item_image_container.view.*
import java.io.File

class DocumentSection(root: View,
                      listener: AddMemoryInterface.MemoryAddListener?,
                      memory: Memory,
                      isEditing: Boolean,
                      editMemory: Memory?,
                      documentsToRemove: MutableList<String>) {
  
  
  private val mContext = root.context
  private val mRoot = root
  private val mMemory = memory
  private val mListener = listener
  private val mIsEditing = isEditing
  private val mEditMemory: Memory? = editMemory
  private val mDocumentsToRemove = documentsToRemove
  
  
  
  
  
  
  fun setup() {
    if (mIsEditing) {
      for (document in mEditMemory!!.documents) {
        addDocumentToContainer(document)
      }
    }
  
    mRoot.addDocumentButton.setOnClickListener {
      mListener?.onAddDocument()
    }
  }
  
  
  
  
  
  fun setupDocumentsContainer(paths: MutableList<String>){
    if (mIsEditing){
      for (document in mEditMemory!!.documents){
        addDocumentToContainer(document)
      }
    }
    
    
    // Take documents from chooser to mDocuments list
    mMemory.documents.addAll(paths)
    
    
    // Empty image container before adding views
    mRoot.documentContainer.removeAllViews()
    
    // Put documents to document container
    for (documentPath in mMemory.documents) {
      addDocumentToContainer(documentPath)
    }
  }
  
  
  
  
  
  private fun addDocumentToContainer(documentPath: String){
    val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_image_container, null)
    
    val imageView = view.imageView
    val pathTextView = view.pathTextView
    val deleteImageView = view.deleteImageView
    
    // Setup path text view
    val file = File(documentPath)
    pathTextView.text = file.name
    
    // Setup document icon image view
    val icon = FileIconHelper().getResource(file)
    imageView.setImageResource(icon)
    
    
    // Setup delete image view
    deleteImageView.setOnClickListener{
      val parent = it.parent as LinearLayout
      
      // Get position of the document
      val viewPosition = (parent.parent as LinearLayout).indexOfChild(parent)
      val docToRemove = mMemory.documents[viewPosition]
      
      if (mIsEditing && mEditMemory!!.documents.contains(docToRemove)){
        mDocumentsToRemove.add(docToRemove)
        mEditMemory.documents.remove(docToRemove)
      }
      
      // Delete document from list
      mMemory.documents.removeAt(viewPosition)
      
      // Delete document from container
      mRoot.documentContainer.removeView(parent)
    }
    
    // Add list row to  image container linear layout
    mRoot.documentContainer.addView(view)
  }
}