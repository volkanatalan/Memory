package com.volkanatalan.memory.sections.memory_view

import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.volkanatalan.memory.R
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.MemoryView
import kotlinx.android.synthetic.main.view_link.view.*
import kotlinx.android.synthetic.main.view_memory.view.*


/**
 * Sets up the link section of [MemoryView].
 */
class LinkSection (val rootView: LinearLayout, val memory: Memory) {
  
  
  private val mContext = rootView.context
  private val mLinks = memory.links
  
  
  
  
  
  /**
   * Start setting up the link section of [MemoryView].
   */
  fun setup(){
    if (mLinks.size > 0) {
  
      rootView.linkContainer.removeAllViews()
      rootView.visibility = View.VISIBLE
      
      for (link in mLinks){
        // Create root
        val density = mContext.resources.displayMetrics.density
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.bottomMargin = (5 * density).toInt()
        var root = LinearLayout(mContext)
        root.layoutParams = params
        
        // Inflate link list item
        root = LayoutInflater.from(mContext).inflate(R.layout.view_link, root) as LinearLayout
        val linkTextView = root.linkTextView
        
        // Put underline to text
        var linkTitle = link.title
        val linkAddress = link.address
        if (linkTitle == "") linkTitle = linkAddress
        val content = SpannableString(linkTitle)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        linkTextView.text = content
        
        // Open the link when it is clicked on
        root.setOnClickListener{
          val parent = it.parent as LinearLayout
          val index = parent.indexOfChild(it)
          var url = mLinks[index].address
          if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://$url"
          
          mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
  
        rootView.linkContainer.addView(root)
      }
    }
  }
}