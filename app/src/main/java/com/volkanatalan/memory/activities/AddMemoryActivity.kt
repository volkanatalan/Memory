package com.volkanatalan.memory.activities

import android.app.Activity
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_memory.*
import com.bumptech.glide.Glide
import com.volkanatalan.memory.*
import com.volkanatalan.memory.classes.FilePath
import com.volkanatalan.memory.helpers.FileIconHelper
import com.volkanatalan.memory.classes.Link
import com.volkanatalan.memory.classes.Memory
import com.volkanatalan.memory.databases.MemoryDatabase
import kotlinx.android.synthetic.main.activity_add_memory.documentContainer
import kotlinx.android.synthetic.main.activity_add_memory.imageContainer
import kotlinx.android.synthetic.main.activity_add_memory.linkContainer
import kotlinx.android.synthetic.main.activity_add_memory.tagContainer
import kotlinx.android.synthetic.main.list_item_image_container.view.*
import kotlinx.android.synthetic.main.list_item_image_container.view.deleteImageView
import kotlinx.android.synthetic.main.list_item_link.view.*
import kotlinx.android.synthetic.main.list_item_tag_container.view.*
import java.io.*
import java.nio.channels.FileChannel


class AddMemoryActivity : AppCompatActivity() {


  private val TAG = "AddMemoryActivity"
  private var mMemory = Memory()
  lateinit var mEditMemory: Memory
  private var PICK_IMAGE_MULTIPLE = 100
  private var ADD_DOCUMENTS = 101
  private var isEditing = false
  private val mImagesToRemove = mutableListOf<String>()
  private val mDocumentsToRemove = mutableListOf<String>()




  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_memory)
    setSupportActionBar(toolbar)

    val editMemoryId = intent.getIntExtra("editMemory", -1)
    if (editMemoryId > -1) {
      isEditing = true
      mEditMemory = MemoryDatabase(this, null).rememberSomething(editMemoryId)
      mMemory.id = mEditMemory.id
      mMemory.title = mEditMemory.title
      mMemory.text = mEditMemory.text
      mMemory.tags = mEditMemory.tags
      mMemory.images.addAll(mEditMemory.images)
      mMemory.documents.addAll(mEditMemory.documents)
      mMemory.links = mEditMemory.links

    }

    setupTextSection()
    setupImageSection()
    setupDocumentSection()
    setupLinkSection()
    setupTagSection()

  }





  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)

    if (resultCode == Activity.RESULT_OK && intent != null) {

      if (requestCode == ADD_DOCUMENTS) {
        setupDocumentsContainer(intent)
      }

      else if (requestCode == PICK_IMAGE_MULTIPLE) {
        mMemory.images.addAll(takeImageFromChooser(intent))
        addImageToContainer()
      }
    }
  }





  private fun onSelectedDone(){
    mMemory.title = titleEditText.text.toString()
    mMemory.text = textEditText.text.toString()

    // Add forgotten tag
    val forgottenTag = getTag()
    if (forgottenTag != "") mMemory.tags.add(forgottenTag)

    // Add forgotten link
    val forgottenLink = getLink()
    if (forgottenLink != null) mMemory.links.add(forgottenLink)



    when {
      title == "" -> Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show()

      mMemory.tags.size == 0 -> Toast.makeText(this, "Please enter a tag!", Toast.LENGTH_SHORT).show()

      else -> {

        // Remove deleted files from storage, if it is in editing mode
        if (isEditing){
          // Delete images
          for (image in mImagesToRemove){
            val imgFile = File(image)
            if (imgFile.exists())
              imgFile.delete()
          }

          // Delete documents
          for (doc in mDocumentsToRemove){
            val docFile = File(doc)
            if (docFile.exists())
              docFile.delete()
          }
        }
  
  
        // Copy chosen files to internal storage
        val folderImages = "Images"
        val folderDocuments = "Documents"

        mMemory.images = copyFileToAppFolder(mMemory.images, folderImages)
        mMemory.documents = copyFileToAppFolder(mMemory.documents, folderDocuments)

        // Save memory to database
        val database = MemoryDatabase(this, null)
        database.memorize(mMemory)

        // Clear activity
        mMemory = Memory()
        tagEditText.setText("")
        textEditText.setText("")
        titleEditText.setText("")
        linkTitleEditText.setText("")
        linkAddressEditText.setText("")
        tagContainer.removeAllViews()
        linkContainer.removeAllViews()
        imageContainer.removeAllViews()
        documentContainer.removeAllViews()

        Toast.makeText(this, "Memorized successfully.", Toast.LENGTH_SHORT).show()

        // Scroll to top
        scrollView.scrollTo(0, 0)

        // Focus on title edit text
        titleEditText.requestFocus()
      }
    }
  }





  private fun setupTextSection(){
    if (isEditing) {
      titleEditText.setText(mEditMemory.title)
      textEditText.setText(mEditMemory.text)
    }
  }





  private fun setupTagSection(){
    if (isEditing){
      for (tag in mMemory.tags)
        addTagToContainer(tag)
    }


    addTagImageView.setOnClickListener{

      when (val tag = getTag()) {
        "" -> Toast.makeText(this, "Tag cannot be empty!", Toast.LENGTH_SHORT).show()

        in mMemory.tags -> Toast.makeText(this, "Same tag!", Toast.LENGTH_SHORT).show()

        else -> {
          mMemory.tags.add(tag)

          addTagToContainer(tag)

          tagEditText.setText("")
        }
      }
    }
  }




  private fun addTagToContainer(tag: String){
    val view = LayoutInflater.from(this).inflate(R.layout.list_item_tag_container, null)
    view.id = tagContainer.childCount

    val tagTextView = view.tagTextView
    tagTextView.text = tag

    val deleteButton = view.deleteImageView
    deleteButton.setOnClickListener{
      val listRow = it.parent as LinearLayout
      val tagName = (listRow.getChildAt(0) as TextView).text

      mMemory.tags.remove(tagName)

      tagContainer.removeView(listRow)
    }

    tagContainer.addView(view)
  }





  private fun getTag(): String{
    return tagEditText.text.toString()
  }





  private fun setupImageSection(){
    if (isEditing){
      addImageToContainer()
    }


    addImageButton.setOnClickListener {
      val intent = Intent(ACTION_OPEN_DOCUMENT).apply {
        putExtra(EXTRA_ALLOW_MULTIPLE, true)
        addCategory(CATEGORY_OPENABLE)
        type = "image/*"
      }
      startActivityForResult(createChooser(intent, "Select an app to add images"), PICK_IMAGE_MULTIPLE)
    }
  }





  private fun addImageToContainer() {

    // Empty image container for any case
    imageContainer.removeAllViews()

    // Put images to image container
    for (imagePath in mMemory.images) {

      val view = LayoutInflater.from(this).inflate(R.layout.list_item_image_container, null)

      val imageView = view.imageView
      val pathTextView = view.pathTextView
      val deleteImageView = view.deleteImageView

      // Setup image view
      val imgFile = File(imagePath)
      if (imgFile.exists()) {

        Glide.with(this).load(imagePath).into(imageView)
      }

      // Setup path text view
      pathTextView.text = imgFile.name

      // Setup delete image view
      deleteImageView.setOnClickListener {
        val parent = it.parent as LinearLayout

        // Get position of the image
        val viewPosition = (parent.parent as LinearLayout).indexOfChild(parent)
        val imageToRemove = mMemory.images[viewPosition]

        // If it is in editing mode and the image is in the storage, add image to removing list
        if (isEditing && mEditMemory.images.contains(imageToRemove)) {
          mImagesToRemove.add(imageToRemove)
          mEditMemory.images.remove(imageToRemove)
        }

        // Delete image from list
        mMemory.images.removeAt(viewPosition)

        // Delete image from container
        imageContainer.removeView(parent)
      }

      // Add list row to  image container linear layout
      imageContainer.addView(view)
    }
  }





  private fun setupDocumentSection() {
    if (isEditing) {
      for (document in mEditMemory.documents) {
        addDocumentToContainer(document)
      }
    }

    addDocumentButton.setOnClickListener {
      val intent = Intent(ACTION_GET_CONTENT)
      intent.putExtra(EXTRA_ALLOW_MULTIPLE, true)
      intent.type = "*/*"

      startActivityForResult(createChooser(intent, "Select an app to add documents"), ADD_DOCUMENTS)
    }
  }





  private fun setupDocumentsContainer(intent: Intent){
    if (isEditing){
      for (document in mEditMemory.documents){
        addDocumentToContainer(document)
      }
    }


    // Take documents from chooser to mDocuments list
    val list = takeImageFromChooser(intent)
    mMemory.documents.addAll(list)


    // Empty image container before adding views
    documentContainer.removeAllViews()

    // Put documents to document container
    for (documentPath in mMemory.documents) {
      addDocumentToContainer(documentPath)
    }
  }





  private fun addDocumentToContainer(documentPath: String){
    val view = LayoutInflater.from(this).inflate(R.layout.list_item_image_container, null)

    val imageView = view.imageView
    val pathTextView = view.pathTextView
    val deleteImageView = view.deleteImageView

    // Setup path text view
    val file = File(documentPath)
    pathTextView.text = file.name

    // Setup document icon image view
    val icon = FileIconHelper().getResource(file)
    imageView.setImageResource(icon)


    // Setup delete image view
    deleteImageView.setOnClickListener{
      val parent = it.parent as LinearLayout

      // Get position of the document
      val viewPosition = (parent.parent as LinearLayout).indexOfChild(parent)
      val docToRemove = mMemory.documents[viewPosition]

      if (isEditing && mEditMemory.documents.contains(docToRemove)){
        mDocumentsToRemove.add(docToRemove)
        mEditMemory.documents.remove(docToRemove)
      }

      // Delete document from list
      mMemory.documents.removeAt(viewPosition)

      // Delete document from container
      documentContainer.removeView(parent)
    }

    // Add list row to  image container linear layout
    documentContainer.addView(view)
  }





  private fun setupLinkSection(){
    if (isEditing){
      for (link in mMemory.links)
      addLinkToContainer(link)
    }


    addLinkImageView.setOnClickListener {
      val link = getLink()

      if (link == null){
        Toast.makeText(this, "Address cannot be empty!", Toast.LENGTH_SHORT).show()
      }
      else{
        // Add link to mLinks list
        mMemory.links.add(link)

        // Add link to link container linear layout
        addLinkToContainer(link)

        // Clear edit texts
        linkTitleEditText.setText("")
        linkAddressEditText.setText("")
      }
    }
  }




  private fun getLink(): Link?{
    val title = linkTitleEditText.text.toString()
    val address = linkAddressEditText.text.toString()

    return if (address == "")
      null
    else
      Link(address, title)
  }




  private fun addLinkToContainer(link: Link?){
    if (link != null){
      val listItem = LayoutInflater.from(this).inflate(R.layout.list_item_link_container, null) as LinearLayout
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
        val position = linkContainer.indexOfChild(parent)
        mMemory.links.removeAt(position)

        // Remove link from container
        linkContainer.removeView(parent)
      }

      linkContainer.addView(listItem)
    }
  }





  private fun takeImageFromChooser(intent: Intent): MutableList<String>{
    val list = mutableListOf<String>()
    if (intent.clipData != null) {
      val count = intent.clipData!!.itemCount
      for (i in 0 until count) {

        val uri: Uri = intent.clipData!!.getItemAt(i).uri
        val selectedFilePath = FilePath().getPath(this, uri)

        list.add(selectedFilePath!!)
      }

    } else if (intent.data != null) {
      val uri: Uri = intent.data!!
      val selectedFilePath = FilePath().getPath(this, uri)

      list.add(selectedFilePath!!)
    }

    return list
  }




  @Throws(IOException::class)
  private fun copyFileToAppFolder(sourceList: MutableList<String>, folder: String): MutableList<String> {

    val destinationList = mutableListOf<String>()

    // Create an internal directory if doesn't exist
    val directory = File(filesDir, folder)
    if (!directory.exists()) directory.mkdirs()


    for (path in sourceList) {
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

    return destinationList
  }





  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.add_memory_activity_menu, menu)

    return super.onCreateOptionsMenu(menu)
  }





  override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
    R.id.action_addmemoryactivity_done ->{
      onSelectedDone()
      true
    }

    else -> super.onOptionsItemSelected(item)
  }
}
