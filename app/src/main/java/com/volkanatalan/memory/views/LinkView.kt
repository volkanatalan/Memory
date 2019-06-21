package com.volkanatalan.memory.views

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.volkanatalan.memory.R
import com.volkanatalan.memory.models.Link
import kotlinx.android.synthetic.main.view_link.view.*

class LinkView( context: Context,
                link: Link) {
  
  
  
  var rootView = LinearLayout(context)
  
  
  
  
  init {
    // Inflate link list item
    rootView = LayoutInflater.from(context).inflate(R.layout.view_link, rootView) as LinearLayout
    val linkTextView = rootView.linkTextView
  
    // Put underline to text
    var linkTitle = link.title
    val linkAddress = link.address
    if (linkTitle == "") linkTitle = linkAddress
    val content = SpannableString(linkTitle)
    content.setSpan(UnderlineSpan(), 0, content.length, 0)
    linkTextView.text = content
  }
}