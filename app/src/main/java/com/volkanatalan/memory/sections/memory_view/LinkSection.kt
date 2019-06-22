package com.volkanatalan.memory.sections.memory_view

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.LinkView
import com.volkanatalan.memory.views.MemoryView
import kotlinx.android.synthetic.main.view_memory.view.*


/**
 * Sets up the link section of [MemoryView].
 */
class LinkSection (rootView: LinearLayout, memory: Memory) {
  
  
  private val mContext = rootView.context
  private val mLinks = memory.links
  private val mLinkBase = rootView.linkBase
  private val mLinkContainer = rootView.linkContainer
  
  
  
  
  
  /**
   * Start setting up the link section of [MemoryView].
   */
  fun setup(){
    if (mLinks.size > 0) {
  
      mLinkContainer.removeAllViews()
      mLinkBase.visibility = View.VISIBLE
      
      for (link in mLinks){
        // Create root
        val density = mContext.resources.displayMetrics.density
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.bottomMargin = (5 * density).toInt()
        
        val linkView = LinkView(mContext, link).rootView
        linkView.layoutParams = params
        
        
        // Open the link when it is clicked on
        linkView.setOnClickListener{
          val parent = it.parent as LinearLayout
          val index = parent.indexOfChild(it)
          var url = mLinks[index].address
          if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://$url"
          
          mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
  
        mLinkContainer.addView(linkView)
      }
    }
  }
}