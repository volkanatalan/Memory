package com.volkanatalan.memory.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.LinearLayout
import com.volkanatalan.memory.R
import com.volkanatalan.memory.helpers.FileIconHelper
import com.volkanatalan.memory.helpers.ShareHelper
import kotlinx.android.synthetic.main.view_document.view.*
import java.io.File

class DocumentView( context: Context,
                    documentPath: String,
                    documents: MutableList<String>,
                    params: GridLayout.LayoutParams ) {
  
  
  var rootView: LinearLayout = LinearLayout(context)
  
  
  
  init {
    // Create root
    rootView.layoutParams = params
  
    // Inflate link list item
    rootView = LayoutInflater.from(context).inflate(R.layout.view_document, rootView) as LinearLayout
    val documentIconImageView = rootView.documentIconImageView
    val documentNameTextView = rootView.documentNameTextView
  
    // Setup document name text view
    val file = File(documentPath)
    val fileName = file.name
    documentNameTextView.text = fileName
  
    // Setup document icon image view
    val fileIconResource = FileIconHelper.getResource(file)
    documentIconImageView.setImageResource(fileIconResource)
  
    // Set on click listener to document view to open it with another application
    rootView.setOnClickListener {
      val parentOfDocument = it.parent as GridLayout
      val documentIndex = parentOfDocument.indexOfChild(it)
      val path = documents[documentIndex]
    
      ShareHelper(context).shareFile(path)
    }
  }
}