package com.volkanatalan.memory.sections

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.volkanatalan.memory.R
import com.volkanatalan.memory.models.Link
import com.volkanatalan.memory.models.Memory
import kotlinx.android.synthetic.main.activity_add_memory.view.*
import kotlinx.android.synthetic.main.list_item_link_container.view.*

class LinkSection( root: View,
                   memory: Memory,
                   isEditing: Boolean ) {
  
  
  private val mContext = root.context
  private val mRoot = root
  private val mMemory = memory
  private val mIsEditing = isEditing
  
  
  
  
  
  fun setup(){
    if (mIsEditing){
      for (link in mMemory.links)
        addLinkToContainer(link)
    }
  
  
    mRoot.addLinkImageView.setOnClickListener {
      val link = getLink()
      
      if (link == null){
        Toast.makeText(mContext, "Address cannot be empty!", Toast.LENGTH_SHORT).show()
      }
      else{
        // Add link to mLinks list
        mMemory.links.add(link)
        
        // Add link to link container linear layout
        addLinkToContainer(link)
        
        // Clear edit texts
        mRoot.linkTitleEditText.setText("")
        mRoot.linkAddressEditText.setText("")
      }
    }
  }
  
  
  
  
  private fun addLinkToContainer(link: Link?){
    if (link != null){
      val listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_link_container, null) as LinearLayout
      val linkTextView = listItem.linkTextView
      val deleteImageView = listItem.deleteImageView
      if (link.title == "") link.title = link.address
      
      // Setup address text view
      val content = SpannableString(link.title)
      content.setSpan(UnderlineSpan(), 0, content.length, 0)
      linkTextView.text = content
      
      // Delete link on click delete image view
      deleteImageView.setOnClickListener {
        val parent = it.parent as LinearLayout
        
        // Remove link from mLinks list
        val position = mRoot.linkContainer.indexOfChild(parent)
        mMemory.links.removeAt(position)
        
        // Remove link from container
        mRoot.linkContainer.removeView(parent)
      }
  
      mRoot.linkContainer.addView(listItem)
    }
  }
  
  
  
  fun getLink(): Link?{
    val title = mRoot.linkTitleEditText.text.toString()
    val address = mRoot.linkAddressEditText.text.toString()
    
    return if (address == "")
      null
    else
      Link(address, title)
  }
  
  
}