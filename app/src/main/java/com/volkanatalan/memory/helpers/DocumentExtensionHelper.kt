package com.volkanatalan.memory.helpers

class DocumentExtensionHelper {
  
  fun getFileType(extension: String): String{
    return when(extension){
      "jpg" -> "image/jpg"
      "jpeg" -> "image/jpeg"
      "png" -> "image/png"
      "gif" -> "image/gif"
      "pdf" -> "application/pdf"
      "apk" -> "application/vnd.android.package-archive"
      "txt" -> "text/plain"
      "cfg" -> "text/plain"
      "rc" -> "text/plain"
      "csv" -> "text/plain"
      "xml" -> "text/xml"
      "html" -> "text/html"
      "htm" -> "text/html"
      "mpeg" -> "audio/mpeg"
      "mp3" -> "audio/mp3"
      "aac" -> "audio/aac"
      "wav" -> "audio/wav"
      "ogg" -> "audio/ogg"
      "midi" -> "audio/midi"
      "wma" -> "audio/wma"
      "mp4" -> "video/mp4"
      "wmv" -> "video/wmv"
      else -> "*/*"
    }
  }
}