package com.volkanatalan.memory.views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.volkanatalan.memory.R
import kotlinx.android.synthetic.main.view_tag.view.*

class TagView( context: Context,
               text: String ) {
  
  
  var rootView = LinearLayout(context)
  
  
  init {
  
    rootView = LayoutInflater.from(context).inflate(R.layout.view_tag, rootView) as LinearLayout
    val textView = rootView.textView
  
    textView.text = text
  }
}