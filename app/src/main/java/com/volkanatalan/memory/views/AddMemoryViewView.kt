package com.volkanatalan.memory.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.volkanatalan.memory.R
import com.volkanatalan.memory.databases.MemoryDatabase
import com.volkanatalan.memory.interfaces.AddMemoryViewInterface
import com.volkanatalan.memory.models.Memory
import com.volkanatalan.memory.sections.*
import kotlinx.android.synthetic.main.activity_add_memory.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.*


class AddMemoryViewView(inflater :LayoutInflater,
                        listener: AddMemoryViewInterface.Listener?,
                        editMemoryId: Int ):
  AddMemoryViewInterface {
  
  
  private val TAG = "AddMemoryViewView"
  private val mRootView = inflater.inflate(R.layout.activity_add_memory, null)
  private val mContext = mRootView.context
  var memory = Memory()
  private var mEditMemory: Memory? = null
  private var isEditing = false
  private var isSaveSuccessful = false
  private val mImagesToRemove = mutableListOf<String>()
  private val mDocumentsToRemove = mutableListOf<String>()
  private var mListener = listener
  var textSection: TextSection
  var imageSection: ImageSection
  var documentSection: DocumentSection
  var linkSection: LinkSection
  var tagSection: TagSection
  
  
  
  init{
    
    // If editing a memory copy memory to mEditMemory
    if (editMemoryId > -1) {
      isEditing = true
      mEditMemory = MemoryDatabase(mContext, null).remember(editMemoryId)
      memory.id = mEditMemory!!.id
      memory.title = mEditMemory!!.title
      memory.text = mEditMemory!!.text
      memory.tags = mEditMemory!!.tags
      memory.images.addAll(mEditMemory!!.images)
      memory.documents.addAll(mEditMemory!!.documents)
      memory.links = mEditMemory!!.links
    }
  
  
    // Setup sections
    textSection = TextSection(mRootView, isEditing, mEditMemory)
    textSection.setup()
  
    imageSection = ImageSection(mRootView, mListener, memory, isEditing, mEditMemory, mImagesToRemove)
    imageSection.setup()
  
    documentSection = DocumentSection(mRootView, mListener, memory, isEditing, mEditMemory, mDocumentsToRemove)
    documentSection.setup()
  
    linkSection = LinkSection(mRootView, memory, isEditing)
    linkSection.setup()
  
    tagSection = TagSection(mRootView, memory, isEditing)
    tagSection.setup()
  }
  
  
  
  
  
  override fun getRootView(): View = mRootView
  
  
  
  
  
  fun saveMemory() {
    
    // Get title and text
    memory.title = mRootView.titleEditText.text.toString()
    memory.text = mRootView.textEditText.text.toString()
  
    // Add forgotten tag
    val forgottenTag = tagSection.getTag()
    if (forgottenTag != "" && !memory.tags.contains(forgottenTag)) memory.tags.add(forgottenTag)
  
    // Add forgotten link
    val forgottenLink = linkSection.getLink()
    if (forgottenLink != null) memory.links.add(forgottenLink)
    
    // Save the memory if necessary places are filled
    when {
      memory.title == "" -> Toast.makeText(mContext, "Title cannot be empty!", Toast.LENGTH_SHORT).show()
  
      memory.tags.size == 0 -> Toast.makeText(mContext, "Please enter a tag!", Toast.LENGTH_SHORT).show()
  
      else -> {
    
        // Remove deleted files from storage, if it is in editing mode
        if (isEditing) {
          // Delete images
          for (image in mImagesToRemove) {
            val imgFile = File(image)
            if (imgFile.exists())
              imgFile.delete()
          }
      
          // Delete documents
          for (doc in mDocumentsToRemove) {
            val docFile = File(doc)
            if (docFile.exists())
              docFile.delete()
          }
        }
    
    
        // Copy chosen files to internal storage
        val folderImages = "Images"
        val folderDocuments = "Documents"
    
        memory.images = copyFileToAppFolder(memory.images, folderImages)
        memory.documents = copyFileToAppFolder(memory.documents, folderDocuments)
        if (!isEditing) {
          memory.date = Calendar.getInstance().time
        }
    
        // Save memory to database
        val database = MemoryDatabase(mContext, null)
        database.memorize(memory)
  
        isSaveSuccessful = true
        Log.d(TAG, "Memorized successfully! ${memory.title}")
      }
    }
    
    
    
    if (isSaveSuccessful) {
  
      // Clear activity
      mRootView.tagEditText.setText("")
      mRootView.textEditText.setText("")
      mRootView.titleEditText.setText("")
      mRootView.linkTitleEditText.setText("")
      mRootView.linkAddressEditText.setText("")
      mRootView.tagContainer.removeAllViews()
      mRootView.linkContainer.removeAllViews()
      mRootView.imageContainer.removeAllViews()
      mRootView.documentContainer.removeAllViews()
  
      // Scroll to top
      mRootView.scroll_view.scrollTo(0, 0)
  
      // Focus on title edit text
      mRootView.titleEditText.requestFocus()
  
  
      isSaveSuccessful = false
      
      
      Toast.makeText(mContext, "Memorized successfully.", Toast.LENGTH_SHORT).show()
  
  
      mListener?.afterSaveMemory(isEditing, memory.id)
  
      // Clear memory
      memory = Memory()
    }
  }
  
  
  
  
  @Throws(IOException::class)
  private fun copyFileToAppFolder(sourceList: MutableList<String>, folder: String): MutableList<String> {
    
    val destinationList = mutableListOf<String>()
    
    // Create an internal directory if doesn't exist
    val directory = File(mContext.filesDir, folder)
    if (!directory.exists()) directory.mkdirs()
    
    
    for (path in sourceList) {
      if (mEditMemory == null || (!mEditMemory!!.images.contains(path) && !mEditMemory!!.documents.contains(path))) {
        val sourceFile = File(path)
        val sourceFileOnlyName = sourceFile.nameWithoutExtension
        val sourceFileExtension = sourceFile.extension
        var destinationFile = File(directory, sourceFile.name)
        
        // If file exist change its name.
        var count = 1
        while (destinationFile.exists()) {
          count++
          val fileNameAddition = "($count)"
          val fileName = "$sourceFileOnlyName$fileNameAddition.$sourceFileExtension" // .../Images/fileName(2).jpg
          destinationFile = File(directory, fileName)
        }
        
        // Copy file to internal storage.
        if (sourceFile.exists()) {
          val inChannel: FileChannel = FileInputStream(sourceFile).channel
          val outChannel: FileChannel = FileOutputStream(destinationFile).channel
          try {
            inChannel.transferTo(0, inChannel.size(), outChannel)
            destinationList.add(destinationFile.path)
          } finally {
            inChannel.close()
            outChannel.close()
          }
        }
      }
      else {
        destinationList.add(path)
      }
    }
    
    
    return destinationList
  }
}