package com.volkanatalan.memory.sections.memory_view

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.volkanatalan.memory.R
import com.volkanatalan.memory.helpers.ShareHelper
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.views.MemoryView
import kotlinx.android.synthetic.main.memory_image.view.*
import java.io.File


/**
 * Sets up the image section of [MemoryView].
 */
class ImageSection (val rootView: LinearLayout, val memory: Memory) {
  
  
  
  private val mContext = rootView.context
  private val mImages = memory.images
  
  
  
  
  
  
  /**
   * Start setting up the image section of [MemoryView].
   */
  fun setup() {
    if (mImages.size > 0) {
  
      rootView.removeAllViews()
      rootView.visibility = View.VISIBLE
      
      for (imagePath in mImages) {
        
        val imgFile = File(imagePath)
        if (imgFile.exists()) {
          
          // Create root
          val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
          params.bottomMargin = mContext.resources.getDimensionPixelSize(R.dimen.memory_image_bottom_margin)
          var root = LinearLayout(mContext)
          root.layoutParams = params
          
          // Inflate root
          root = LayoutInflater.from(mContext).inflate(R.layout.memory_image, root) as LinearLayout
          val imageView = root.memory_image_view
          
          
          // Setup image view
          Glide.with(mContext).load(imgFile).into(imageView)
          
          
          // Set on click listener to image to open it another app
          root.setOnClickListener {
            val parent = it.parent as LinearLayout
            val index = parent.indexOfChild(it)
            val path = mImages[index]
  
            ShareHelper(mContext).shareFile(path)
          }
          
          
          // Add image to image container
          rootView.addView(root)
        }
      }
    }
  }
}