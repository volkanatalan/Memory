package com.volkanatalan.memory.sections.add_memory_activity

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.volkanatalan.memory.R
import com.volkanatalan.memory.activities.AddMemoryActivity
import com.volkanatalan.memory.models.Link
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.AddMemoryView
import kotlinx.android.synthetic.main.activity_add_memory.view.*
import kotlinx.android.synthetic.main.list_item_link_container.view.*

/**
 * Sets up the link section of [AddMemoryActivity].
 */
class LinkSection( root: View,
                   memory: Memory,
                   isEditing: Boolean ) {
  
  
  private val mContext = root.context
  private val mRoot = root
  private val mMemory = memory
  private val mIsEditing = isEditing
  
  
  
  
  /**
   * Start setting up the link section of [AddMemoryActivity].
   */
  fun setup(){
    // If it is in editing mode, add links of the memory to the container.
    if (mIsEditing){
      for (link in mMemory.links)
        addLinkToContainer(link)
    }
  
  
    // Paste without rich text formatting
    mRoot.linkTitleEditText.addTextChangedListener { text ->
      AddMemoryView.removeSpans(text!!)
    }
  
  
    // Paste without rich text formatting
    mRoot.linkAddressEditText.addTextChangedListener { text ->
      AddMemoryView.removeSpans(text!!)
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
  
  
  
  
  /**
   * Adds to container a link and a button to remove it from
   * container. If link title is empty, link address will be shown.
   */
  private fun addLinkToContainer(link: Link?){
    if (link != null){
      val listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_link_container, null) as LinearLayout
      val linkTextView = listItem.linkTextView
      val deleteImageView = listItem.deleteImageView
      
      // If title is empty show the address
      if (link.title!!.isEmpty()) link.title = link.address
      
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
  
  
  /**
   * Takes the texts from link title and link address edit texts to [Link] model.
   */
  fun getLink(): Link?{
    val title = mRoot.linkTitleEditText.text.toString()
    val address = mRoot.linkAddressEditText.text.toString()
    
    return if (address == "")
      null
    else
      Link(address, title)
  }
  
  
}