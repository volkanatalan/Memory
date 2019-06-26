package com.volkanatalan.memory.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.volkanatalan.memory.R
import com.volkanatalan.memory.helpers.FilePathHelper
import com.volkanatalan.memory.interfaces.AddMemoryViewInterface
import com.volkanatalan.memory.views.AddMemoryView
import kotlinx.android.synthetic.main.activity_add_memory.*


/**
 * Add new memories with this activity.
 */
class AddMemoryActivity : AppCompatActivity(), AddMemoryViewInterface.Listener {


  private val TAG = "AddMemoryActivity"
  private var PICK_IMAGE_MULTIPLE = 0
  private var ADD_DOCUMENTS = 0
  private lateinit var mInterstitialAd: InterstitialAd
  private lateinit var mAddMemoryView: AddMemoryView




  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  
    // Request codes
    PICK_IMAGE_MULTIPLE = resources.getInteger(R.integer.pick_image_multiple)
    ADD_DOCUMENTS = resources.getInteger(R.integer.add_documents)
  
    // If activity opened to edit a memory, get memory id
    val editMemoryId = intent.getIntExtra("editMemory", -1)
    
    // Inflate views
    mAddMemoryView = AddMemoryView(layoutInflater, this, editMemoryId)
    setContentView(mAddMemoryView.getRootView())
    
    setSupportActionBar(toolbar)
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
  
  
  
  
  
  private fun onSelectedDone(){
    // Save memory to database
    mAddMemoryView.saveMemory()
  }





  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)

    if (resultCode == Activity.RESULT_OK && intent != null) {

      if (requestCode == ADD_DOCUMENTS) {
        // Show selected documents
        val documents = takePathsFromChooser(intent)
        mAddMemoryView.documentSection.setupDocumentsContainer(documents)
      }

      else if (requestCode == PICK_IMAGE_MULTIPLE) {
        // Show selected images
        mAddMemoryView.memory.images.addAll(takePathsFromChooser(intent))
        mAddMemoryView.imageSection.addImagesToContainer()
      }
    }
  }
  
  
  /**
   * Take paths of chosen files with chooser from intent.
   */
  private fun takePathsFromChooser(intent: Intent): MutableList<String>{
    val list = mutableListOf<String>()
    if (intent.clipData != null) {
      val count = intent.clipData!!.itemCount
      for (i in 0 until count) {
        
        val uri: Uri = intent.clipData!!.getItemAt(i).uri
        val selectedFilePath = FilePathHelper().getPath(this, uri)
        list.add(selectedFilePath!!)
      }
      
    } else if (intent.data != null) {
      val uri: Uri = intent.data!!
      val selectedFilePath = FilePathHelper().getPath(this, uri)
      
      list.add(selectedFilePath!!)
    }
    
    return list
  }
  
  
  
  
  
  override fun onAddImage() {
  
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED) {
    
      // Permission is not granted
      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.
      }
      
      else {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
          PICK_IMAGE_MULTIPLE)
      }
    }
    
    else {
      // Permission has already been granted
      val intent = Intent(ACTION_OPEN_DOCUMENT).apply {
        putExtra(EXTRA_ALLOW_MULTIPLE, true)
        addCategory(CATEGORY_OPENABLE)
        type = "image/*"
      }
      
      startActivityForResult(createChooser(intent, getString(R.string.select_an_app)), PICK_IMAGE_MULTIPLE)
    }
  }
  
  
  
  
  
  override fun onAddDocument() {
  
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED) {
    
      // Permission is not granted
      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.
      }
    
      else {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
          ADD_DOCUMENTS)
      }
    }
  
    else {
      // Permission has already been granted
      val intent = Intent(ACTION_GET_CONTENT).apply {
        putExtra(EXTRA_ALLOW_MULTIPLE, true)
        type = "*/*"
      }
  
      startActivityForResult(createChooser(intent, getString(R.string.select_an_app)), ADD_DOCUMENTS)
    }
  }
  
  
  
  
  
  override fun afterSaveMemory(isEditing: Boolean, memoryId: Int) {
    // If not created a new memory, but edited an existing memory,
    // send memory id to MainActivity and go back to MainActivity.
    if (isEditing){
      val updateIntent = Intent()
      updateIntent.putExtra("updateMemory", memoryId)
      setResult(Activity.RESULT_OK, updateIntent)
      finish()
    }
  }
}
