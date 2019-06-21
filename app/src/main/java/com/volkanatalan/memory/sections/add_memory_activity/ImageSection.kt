package com.volkanatalan.memory.sections.add_memory_activity

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.volkanatalan.memory.R
import com.volkanatalan.memory.activities.AddMemoryActivity
import com.volkanatalan.memory.interfaces.AddMemoryViewInterface
import com.volkanatalan.memory.models.Memory
import kotlinx.android.synthetic.main.activity_add_memory.view.*
import kotlinx.android.synthetic.main.list_item_image_container.view.*
import java.io.File

/**
 * Sets up the image section of [AddMemoryActivity].
 */
class ImageSection(root: View,
                   listener: AddMemoryViewInterface.Listener?,
                   memory: Memory,
                   isEditing: Boolean,
                   editMemory: Memory?,
                   imagesToRemove: MutableList<String> ) {
  
  
  private val mContext = root.context
  private val mRoot = root
  private val mMemory = memory
  private val mListener = listener
  private val mIsEditing = isEditing
  private val mEditMemory: Memory? = editMemory
  private val mImagesToRemove = imagesToRemove
  
  
  
  
  /**
   * Start setting up the image section of [AddMemoryActivity].
   */
  fun setup(){
    if (mIsEditing && mEditMemory != null){
      addImagesToContainer()
    }
  
  
    mRoot.addImageButton.setOnClickListener {
      mListener?.onAddImage()
    }
  }
  
  
  
  
  /**
   * Adds to container an image, its name and a button to remove them from container.
   */
  fun addImagesToContainer() {
    
    // Empty image container for any case
    mRoot.imageContainer.removeAllViews()
    
    // Put images to image container
    for (imagePath in mMemory.images) {
      
      val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_image_container, null)
      val imageView = view.imageView
      val pathTextView = view.pathTextView
      val deleteImageView = view.deleteImageView
      
      // Setup image view
      val imgFile = File(imagePath)
      if (imgFile.exists()) Glide.with(mContext).load(imagePath).into(imageView)
      
      // Setup path text view
      pathTextView.text = imgFile.name
      
      // Setup delete image view
      deleteImageView.setOnClickListener {
        val parent = it.parent as LinearLayout
        
        // Get position of the image
        val viewPosition = (parent.parent as LinearLayout).indexOfChild(parent)
        val imageToRemove = mMemory.images[viewPosition]
        
        // If it is in editing mode and the image is in the storage, add image to removing list
        if (mIsEditing && mEditMemory!!.images.contains(imageToRemove)) {
          mImagesToRemove.add(imageToRemove)
          mEditMemory.images.remove(imageToRemove)
        }
        
        // Delete image from list
        mMemory.images.removeAt(viewPosition)
        
        // Delete image from container
        mRoot.imageContainer.removeView(parent)
      }
      
      // Add list row to  image container linear layout
      mRoot.imageContainer.addView(view)
    }
  }
}