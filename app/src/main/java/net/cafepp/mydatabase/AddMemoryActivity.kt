package net.cafepp.mydatabase

import android.app.Activity
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_memory.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_row_image_container.view.*
import java.io.*
import java.nio.channels.FileChannel


class AddMemoryActivity : AppCompatActivity() {


  private val TAG = "AddMemoryActivity"
  private val mTags = mutableListOf<String>()
  private val mImages = mutableListOf<String>()
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
        val appFolderImages = mutableListOf<String>()
        for (path in mImages) {
          val sourceFile = File(path)
          var destinationFile = File(filesDir, sourceFile.name)

          val sourceFileName = sourceFile.nameWithoutExtension
          val sourceFileExtension = sourceFile.extension
          var count = 1

          // If file exist change its name.
          while (destinationFile.exists()) {
            count++
            val fileNameAddition = "($count)"
            val fileName = "$sourceFileName$fileNameAddition.$sourceFileExtension" // ...\fileName(2).jpg
            destinationFile = File(filesDir, fileName)
          }

          // Copy file to application folder.
          val isCopySuccess = copyFile(sourceFile, destinationFile)

          if (isCopySuccess) {
            appFolderImages.add(destinationFile.path)
          }
        }


        // Save memory to database
        val memory = Memory(-1, title, text, mTags, appFolderImages)
        val database = MemoryDatabase(this, null)
        database.addMemory(memory)

        // Clear activity
        titleEditText.setText("")
        textEditText.setText("")
        tagEditText.setText("")
        tagContainer.removeAllViews()
        imagesContainer.removeAllViews()
        mTags.clear()
        mImages.clear()
      }
    }
  }





  private fun setupAddImageButton(button : LinearLayout){
    button.setOnClickListener {

      val intent = Intent(ACTION_OPEN_DOCUMENT)
      intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
      intent.addCategory(Intent.CATEGORY_OPENABLE)
      intent.type = "image/*"
      startActivityForResult(Intent.createChooser(intent, "Select an app to add images"), PICK_IMAGE_MULTIPLE)
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


  private fun getPathListFromURI(uri: Uri): String {
    val path: String = uri.path!! // uri = any content Uri

    val databaseUri: Uri
    val selection: String?
    val selectionArgs: Array<String>?

    if (path.contains("/document/image:")) {
      // Files selected from "Documents"
      databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
      selection = "_id=?"
      selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
    }

    else {
      // Files selected from all other sources, especially on Samsung devices
      databaseUri = uri
      selection = null
      selectionArgs = null
    }

    try {
      val projection = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.ORIENTATION,
        MediaStore.Images.Media.DATE_TAKEN
      )

      // Some example data to query
      val cursor = contentResolver.query(
        databaseUri,
        projection, selection, selectionArgs, null
      )

      var filePath = ""
      if (cursor!!.moveToFirst()) {
        val columnIndex = cursor.getColumnIndex(projection[0])
        filePath = cursor.getString(columnIndex)
        Log.d(TAG, "filePath: $filePath")
      }

      cursor.close()

      return filePath

    } catch (e: Exception) {
      Log.e(TAG, e.message, e)
    }
    return ""
  }




  private fun setupImageContainer(intent: Intent) {
    // Take images from chooser
    if (intent.clipData != null) {
      val count = intent.clipData!!.itemCount
      for (i in 0 until count) {
        val imageUri: Uri = intent.clipData!!.getItemAt(i).uri
        mImages.add(getPathListFromURI(imageUri))
      }

    } else if (intent.data != null) {
      val imageUri: Uri = intent.data!!
      mImages.add(getPathListFromURI(imageUri))
    }


    // Empty image container for any case
    imagesContainer.removeAllViews()

    // Put images to image container
    for (imagePath in mImages) {

      val view = LayoutInflater.from(this).inflate(R.layout.list_row_image_container, null)

      val imageView = view.imageView
      val pathTextView = view.imagePathTextView
      val deleteImageView = view.deleteImageView

      // Setup image view
      val imgFile = File(imagePath)
      if (imgFile.exists()) {

        Glide.with(this).load(imgFile).into(imageView)
      }

      // Setup path text view
      pathTextView.text = imgFile.name

      // Setup delete image view
      deleteImageView.setOnClickListener{
        val parent = it.parent as LinearLayout

        // Delete image from container
        imagesContainer.removeView(parent)

        // Delete image from list
        val textView = parent.getChildAt(1) as TextView
        val path = textView.text
        mImages.remove(path)
      }

      // Add list row to  image container linear layout
      imagesContainer.addView(view)
    }
  }




  private fun setupDocumentsContainer(intent: Intent){
    // Take files from chooser
    if (intent.clipData != null) {
      val count = intent.clipData!!.itemCount
      for (i in 0 until count) {
        val fileUri: Uri = intent.clipData!!.getItemAt(i).uri
        getPathListFromURI(fileUri)
      }

    } else if (intent.data != null) {
      val fileUri: Uri = intent.data!!
      getPathListFromURI(fileUri)
    }

  }




  @Throws(IOException::class)
  private fun copyFile(src: File, dst: File): Boolean {

    if (src.absolutePath.toString() == dst.absolutePath.toString()) {
      return false
    }
    else {
      val inChannel: FileChannel = FileInputStream(src).channel
      val outChannel: FileChannel = FileOutputStream(dst).channel
      try {
        inChannel.transferTo(0, inChannel.size(), outChannel)
      }
      finally {
        inChannel.close()
        outChannel.close()
      }
    }
    return true
  }
}
