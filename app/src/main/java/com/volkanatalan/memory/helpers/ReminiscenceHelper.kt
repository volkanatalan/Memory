package com.volkanatalan.memory.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.volkanatalan.memory.BuildConfig
import com.volkanatalan.memory.R
import com.volkanatalan.memory.activities.AddMemoryActivity
import com.volkanatalan.memory.activities.MainActivity
import com.volkanatalan.memory.databases.MemoryDatabase
import com.volkanatalan.memory.fragments.DeleteConfirmationFragment
import com.volkanatalan.memory.interfaces.SearchViewInterface
import com.volkanatalan.memory.models.Link
import com.volkanatalan.memory.models.Memory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_document.view.*
import kotlinx.android.synthetic.main.list_item_link.view.*
import kotlinx.android.synthetic.main.list_item_memory.view.*
import kotlinx.android.synthetic.main.memory_image.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import org.apmem.tools.layouts.FlowLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * ReminiscenceHelper helps to show memory views to user.
 * @param context
 * @param container A view into which memory views to add
 * @param listener A listener to handle user interactions
 */
class ReminiscenceHelper( context: Context,
                          container: LinearLayout,
                          listener:SearchViewInterface.Listener ) {
  
  private val TAG = "ReminiscenceHelper"
  private val mContext = context
  private val mContainer = container
  private val mListener:SearchViewInterface.Listener = listener
  
  
  /**
   * Add memory views to container.
   */
  fun remember(memories: MutableList<Memory>): ReminiscenceHelper {

    // Clear the container
    mContainer.removeAllViews()

    // Add memories to container
    for (memory in memories) {
      // Create root
      val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
      val margin = mContext.resources.getDimensionPixelSize(R.dimen.memory_top_bottom_margin)
      params.setMargins(0, margin, 0, margin)
      var root = LinearLayout(mContext)
      root.layoutParams = params

      // Inflate root
      root = LayoutInflater.from(mContext).inflate(R.layout.list_item_memory, root) as LinearLayout
  
      val titleTextView = root.titleTextView
      val dateTextView = root.dateTextView
      val imageContainer = root.imageContainer
      val textTextView = root.textTextView
      val tagContainer = root.tagContainer
      val linkBase = root.linkBase
      val documentBase = root.documentBase
      val deleteButton = root.deleteButtonImageView
      val editButton = root.editButtonImageView


      // Setup title end text
      titleTextView.text = memory.title
      if (memory.text != "") {
        textTextView.visibility = View.VISIBLE
        textTextView.text = memory.text
      }


      // Setup view sections
      setupDateSection(memory.date, dateTextView)
      setupImageContainer(memory.images, imageContainer)
      setupLinkContainer(memory.links, linkBase)
      setupDocumentContainer(memory.documents, documentBase)
      setupTagContainer(memory.tags, tagContainer)
      setupButtonContainer(memories, deleteButton, editButton)


      mContainer.addView(root)
    }

    return this
  }
  
  
  
  
  /**
   * Clear all views from container.
   */
  fun forget(){
    mContainer.removeAllViews()
  }
  
  
  
  
  
  private fun setupDateSection(date: Date, textView: TextView){
    val dateText = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault()).format(date)
    textView.text = dateText
  }





  private fun setupImageContainer(images: MutableList<String>?, container: LinearLayout) {
    if (images != null && images.size > 0) {

      container.removeAllViews()
      container.visibility = View.VISIBLE

      for (imagePath in images) {

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
            val path = images[index]
  
            shareFile(path)
          }


          // Add image to image container
          container.addView(root)
        }
      }
    }
  }





  private fun setupLinkContainer(links: MutableList<Link>?, base: LinearLayout){
    if (links != null && links.size > 0) {

      base.linkContainer.removeAllViews()
      base.visibility = View.VISIBLE

      for (link in links){
        // Create root
        val density = mContext.resources.displayMetrics.density
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.bottomMargin = (5 * density).toInt()
        var root = LinearLayout(mContext)
        root.layoutParams = params
        
        // Inflate link list item
        root = LayoutInflater.from(mContext).inflate(R.layout.list_item_link, root) as LinearLayout
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
          var url = links[index].address
          if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://$url"
  
          mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        base.linkContainer.addView(root)
      }
    }
  }





  private fun setupDocumentContainer(documents: MutableList<String>?, base: LinearLayout){
    if (documents != null && documents.size > 0) {

      val documentContainer = base.documentContainer as GridLayout
      documentContainer.removeAllViews()
      base.visibility = View.VISIBLE


      // Calculate column and row numbers
      val screenWidth = mContext.resources.displayMetrics.widthPixels
      val pagePadding = mContext.resources.getDimensionPixelSize(R.dimen.page_padding) * 2
      val memoryPadding = mContext.resources.getDimensionPixelSize(R.dimen.section_padding) * 2
      val documentContainerPadding = mContext.resources.getDimensionPixelSize(R.dimen.subsection_padding) * 2
      val freeSpaceForContainer = screenWidth - pagePadding - memoryPadding - documentContainerPadding

      val documentWidth = mContext.resources.getDimensionPixelSize(R.dimen.document_width)
      val documentMargin = mContext.resources.getDimensionPixelSize(R.dimen.grid_margin)
      val totalDocumentWidth = documentWidth + (documentMargin * 2)

      val columnCount = freeSpaceForContainer / totalDocumentWidth
      val rowCount = ceil(documents.size.toFloat() / columnCount.toFloat()).roundToInt()

      documentContainer.columnCount = columnCount
      documentContainer.rowCount = rowCount

      // Center container horizontal
      val containerStartMargin = (freeSpaceForContainer % totalDocumentWidth) / 2
      (documentContainer.layoutParams as LinearLayout.LayoutParams).marginStart = containerStartMargin


      for ((index, document) in documents.withIndex()){

        // Calculate column and row specs of document
        val columnSpec = index % columnCount
        val rowSpec = index / columnCount

        // Create root
        val param = GridLayout.LayoutParams()
        param.height = GridLayout.LayoutParams.WRAP_CONTENT
        param.width = GridLayout.LayoutParams.WRAP_CONTENT
        param.setGravity(Gravity.CENTER)
        param.columnSpec = GridLayout.spec(columnSpec)
        param.rowSpec = GridLayout.spec(rowSpec)
        param.setMargins(documentMargin, documentMargin, documentMargin, documentMargin)
        var root = LinearLayout(mContext)
        root.layoutParams = param

        // Inflate link list item
        root = LayoutInflater.from(mContext).inflate(R.layout.list_item_document, root) as LinearLayout
        val documentIconImageView = root.documentIconImageView
        val documentNameTextView = root.documentNameTextView

        // Setup document name text view
        val file = File(document)
        val fileName = file.name
        documentNameTextView.text = fileName

        // Setup document icon image view
        val fileIconResource = FileIconHelper.getResource(file)
        documentIconImageView.setImageResource(fileIconResource)
        
        // Set on click listener to open file with another application
        root.setOnClickListener {
          val parentOfDocument = it.parent as GridLayout
          val documentIndex = parentOfDocument.indexOfChild(it)
          val documentPath = documents[documentIndex]
          
          shareFile(documentPath)
        }

        documentContainer.addView(root)
      }
    }
  }





  private fun setupTagContainer(tags: MutableList<String>, container: FlowLayout){
    container.removeAllViews()


    val layoutParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )
    layoutParams.setMargins(0, 0, 5, 5)


    // Firstly add "Tags:" label
    val tagsLabelTextView = TextView(mContext)
    tagsLabelTextView.setPadding(0, 5, 5, 0)
    val tagsLabel = mContext.resources.getString(R.string.tags_semicolon)
    tagsLabelTextView.text = tagsLabel

    val labelLayout = LinearLayout(mContext)
    labelLayout.addView(tagsLabelTextView, layoutParams)
    container.addView(labelLayout)


    // Then add the tag(s)
    for (tag in tags) {
      val tagLayout = LinearLayout(mContext)
      
      val view = LayoutInflater.from(mContext).inflate(R.layout.tag_view, null)
      val textView = view.textView

      textView.text = tag

      view.setOnClickListener{
        val text = ((it as RelativeLayout).getChildAt(0) as TextView).text.toString()
        onTagClickListener.onClick(text)
      }

      tagLayout.addView(view, layoutParams)
      container.addView(tagLayout)
    }
  }





  private fun setupButtonContainer(memories: MutableList<Memory>, deleteButton: ImageView, editButton: ImageView){
    
    deleteButton.setOnClickListener {
      mContext.hideKeyboard(deleteButton)
  
      (mContext as MainActivity).interlayer.visibility = View.VISIBLE
  
      val memoryView = deleteButton.parent.parent.parent as LinearLayout
      val position = mContainer.indexOfChild(memoryView)
      val memory = memories[position]
      
      val deleteConfirmationFragment = DeleteConfirmationFragment.newInstance(memory)
      deleteConfirmationFragment.onDeleteClickListener = object : DeleteConfirmationFragment.OnDeleteClickListener{
        override fun onClick(memory: Memory) {
          
          // Delete files from storage
          for (image in memory.images) deleteFilesFromStorage(image)
          for (doc in memory.documents) deleteFilesFromStorage(doc)
          
          // Delete memory from database
          val database = MemoryDatabase(mContext, null)
          database.forget(memory)
  
          // Remove memory from list
          memories.removeAt(position)
  
          // Finally remove memory from memory container linear layout
          mContainer.removeView(memoryView)
        }
      }
      
      
      mListener.onClickDeleteButton(deleteConfirmationFragment)
      
    }


    editButton.setOnClickListener {
      mContext.hideKeyboard(editButton)
      
      val memoryView = deleteButton.parent.parent.parent as LinearLayout
      val position = mContainer.indexOfChild(memoryView)
      val memory = memories[position]

      val intent = Intent(mContext, AddMemoryActivity::class.java)
      intent.putExtra("editMemory", memory.id)
      mListener.onClickEditButton(intent)
    }
  }
  
  
  
  
  
  private fun deleteFilesFromStorage(path: String){
    val file = File(path)
    if (file.exists()) file.delete()
  }
  
  
  
  
  
  private fun shareFile(path: String){
    val file = File(path)
    //Log.d(TAG, "document: ${documents[documentIndex]}")
  
    // Share file to another app
    val intent = Intent(Intent.ACTION_VIEW)
  
    // Set flag to give temporary permission to external app to use FileProvider
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    val uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID, file)
  
    val dataType = DocumentExtensionHelper.getFileType(file.extension)
  
    intent.setDataAndType(uri, dataType)
  
    // validate that the device can open your File!
    val packageManager = mContext.packageManager
    if (intent.resolveActivity(packageManager) != null) {
      mContext.startActivity(intent)
    }
  }
  
  
  
  
  
  private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
  }
  
  
  
  
  lateinit var onTagClickListener: OnTagClickListener
  
  /**
   * A listener to handle user's click events on tags.
   */
  interface OnTagClickListener{
    fun onClick(tag: String)
  }
}