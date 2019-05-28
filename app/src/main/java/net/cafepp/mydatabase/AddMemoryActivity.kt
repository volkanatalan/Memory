package net.cafepp.mydatabase

import android.app.Activity
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.net.toFile
import kotlinx.android.synthetic.main.activity_add_memory.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_row_image_container.view.*
import java.io.*
import java.nio.channels.FileChannel


class AddMemoryActivity : AppCompatActivity() {


  private val TAG = "AddMemoryActivity"
  private val mTags = mutableListOf<String>()
  private val mImages = mutableListOf<String>()
  private val mDocuments = mutableListOf<String>()
  private var PICK_IMAGE_MULTIPLE = 100
  private var ADD_DOCUMENTS = 101




  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_memory)
    setSupportActionBar(toolbar)


    setupAddImageButton(addImageButton)
    setupAddDocumentButton(addDocumentButton)
    addTagImageView.setOnClickListener (onClickAddTagImageView)

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




  private val onClickAddTagImageView = View.OnClickListener{

    when (val tag = tagEditText.text.toString()) {
      "" -> Toast.makeText(this, "Tag cannot be empty!", Toast.LENGTH_SHORT).show()


      in mTags -> Toast.makeText(this, "Same tag!", Toast.LENGTH_SHORT).show()


      else -> {
        mTags.add(tag)

        val view = LayoutInflater.from(this).inflate(R.layout.list_row_tag, null)
        view.id = tagContainer.childCount

        val tagTextView = view.findViewById<TextView>(R.id.tagTextView)
        tagTextView.text = tag

        val deleteButton = view.findViewById<ImageView>(R.id.deleteImageView)
        deleteButton.setOnClickListener{
          val listRow = it.parent as LinearLayout
          val tagName = (listRow.getChildAt(0) as TextView).text

          mTags.remove(tagName)

          tagContainer.removeView(listRow)
        }

        tagContainer.addView(view)

        tagEditText.setText("")
      }
    }
  }




  private fun onSelectedDone(){
    val title = titleEditText.text.toString()
    val text = textEditText.text.toString()

    when {
      title == "" -> Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show()


      mTags.size == 0 -> Toast.makeText(this, "Please enter a tag!", Toast.LENGTH_SHORT).show()


      else -> {
        val appFolderImages: MutableList<String>
        val appFolderDocuments: MutableList<String>

        val folderImages = "Images\\"
        val folderDocuments = "Documents\\"

        // Copy chosen files to app folder
        appFolderImages = copyFileToAppFolder(mImages, folderImages)
        appFolderDocuments = copyFileToAppFolder(mDocuments, folderDocuments)

        // Save memory to database
        val memory = Memory(-1, title, text, mTags, appFolderImages, appFolderDocuments)
        val database = MemoryDatabase(this, null)
        database.addMemory(memory)

        // Clear activity
        titleEditText.setText("")
        textEditText.setText("")
        tagEditText.setText("")
        tagContainer.removeAllViews()
        imageContainer.removeAllViews()
        documentContainer.removeAllViews()
        mTags.clear()
        mImages.clear()
        mDocuments.clear()
      }
    }
  }





  private fun setupAddImageButton(button : LinearLayout){
    button.setOnClickListener {

      val intent = Intent(ACTION_OPEN_DOCUMENT).apply {
        putExtra(EXTRA_ALLOW_MULTIPLE, true)
        addCategory(CATEGORY_OPENABLE)
        type = "image/*"
      }
      startActivityForResult(createChooser(intent, "Select an app to add images"), PICK_IMAGE_MULTIPLE)
    }
  }





  private fun setupAddDocumentButton(button: LinearLayout) {
    button.setOnClickListener {
      val intent = Intent(ACTION_GET_CONTENT)
      intent.putExtra(EXTRA_ALLOW_MULTIPLE, true)
      intent.type = "*/*"

      startActivityForResult(createChooser(intent, "Select an app to add documents"), ADD_DOCUMENTS)
    }
  }





  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)

    if (resultCode == Activity.RESULT_OK && intent != null) {

      if (requestCode == ADD_DOCUMENTS) {
        setupDocumentsContainer(intent)
      }

      else if (requestCode == PICK_IMAGE_MULTIPLE) {
        setupImageContainer(intent)
      }
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




  private fun setupImageContainer(intent: Intent) {
    // Take images from chooser to mImages list
    val list = takeImageFromChooser(intent)
    mImages.addAll(list)

    // Empty image container for any case
    imageContainer.removeAllViews()

    // Put images to image container
    for (imagePath in mImages) {

      val view = LayoutInflater.from(this).inflate(R.layout.list_row_image_container, null)

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
      deleteImageView.setOnClickListener{
        val parent = it.parent as LinearLayout

        // Get position of the image
        val viewPosition = (parent.parent as LinearLayout).indexOfChild(parent)

        // Delete image from container
        imageContainer.removeView(parent)

        // Delete image from list
        mImages.removeAt(viewPosition)
      }

      // Add list row to  image container linear layout
      imageContainer.addView(view)
    }
  }




  private fun setupDocumentsContainer(intent: Intent){
    // Take documents from chooser to mDocuments list
    val list = takeImageFromChooser(intent)
    mDocuments.addAll(list)


    // Empty image container for any case
    documentContainer.removeAllViews()

    // Put documents to document container
    for (documentPath in mDocuments) {

      val view = LayoutInflater.from(this).inflate(R.layout.list_row_image_container, null)

      val imageView = view.imageView
      val pathTextView = view.pathTextView
      val deleteImageView = view.deleteImageView

      // Setup image view
      imageView.setImageResource(R.drawable.document_file)

      // Setup path text view
      val file = File(documentPath)
      pathTextView.text = file.name

      // Setup delete image view
      deleteImageView.setOnClickListener{
        val parent = it.parent as LinearLayout

        // Get position of the image
        val viewPosition = (parent.parent as LinearLayout).indexOfChild(parent)

        // Delete document from container
        documentContainer.removeView(parent)

        // Delete document from list
        mDocuments.removeAt(viewPosition)
      }

      // Add list row to  image container linear layout
      documentContainer.addView(view)
    }
  }




  @Throws(IOException::class)
  private fun copyFileToAppFolder(sourceList: MutableList<String>, folder: String): MutableList<String> {

    val destinationList = mutableListOf<String>()

    for (path in sourceList) {
      val sourceFile = File(path)
      val sourceFileWithFolder = folder + sourceFile.name
      val sourceFileOnlyName = sourceFile.nameWithoutExtension
      val sourceFileExtension = sourceFile.extension

      var destinationFile = File(filesDir, sourceFileWithFolder)

      var count = 1

      // If file exist change its name.
      while (destinationFile.exists()) {
        count++
        val fileNameAddition = "($count)"
        val fileName = "$folder$sourceFileOnlyName$fileNameAddition.$sourceFileExtension" // ...\Images\fileName(2).jpg
        destinationFile = File(filesDir, fileName)
      }

      // Copy file to application folder.
      val inChannel: FileChannel = FileInputStream(sourceFile).channel
      val outChannel: FileChannel = FileOutputStream(destinationFile).channel
      try {
        inChannel.transferTo(0, inChannel.size(), outChannel)
        destinationList.add(destinationFile.path)
        Log.d(TAG, "destinationFile: $destinationFile")
      }
      finally {
        inChannel.close()
        outChannel.close()
      }
    }

    return destinationList
  }
}
