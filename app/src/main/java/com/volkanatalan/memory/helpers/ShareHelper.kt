package com.volkanatalan.memory.helpers

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.volkanatalan.memory.BuildConfig
import java.io.File

class ShareHelper(val context: Context) {
  
  fun shareFile(path: String){
    val file = File(path)
    //Log.d(TAG, "document: ${documents[documentIndex]}")
    
    // Share file to another app
    val intent = Intent(Intent.ACTION_VIEW)
    
    // Set flag to give temporary permission to external app to use FileProvider
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file)
    
    val dataType = DocumentExtensionHelper.getFileType(file.extension)
    
    intent.setDataAndType(uri, dataType)
    
    // validate that the device can open your File!
    val packageManager = context.packageManager
    if (intent.resolveActivity(packageManager) != null) {
      context.startActivity(intent)
    }
  }
}