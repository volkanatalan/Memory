package com.volkanatalan.memory.helpers

import com.volkanatalan.memory.R
import java.io.File

class FileIconHelper {

  fun getResource(file: File): Int {

    return when (file.extension) {
      "3gp" -> R.drawable.file_3gp
      "avi" -> R.drawable.file_avi
      "doc" -> R.drawable.file_doc
      "docx" -> R.drawable.file_docx
      "exe" -> R.drawable.file_exe
      "gif" -> R.drawable.file_gif
      "iso" -> R.drawable.file_iso
      "jpg" -> R.drawable.file_jpg
      "mov" -> R.drawable.file_mov
      "mp3" -> R.drawable.file_mp3
      "mp4" -> R.drawable.file_mp4
      "mpg" -> R.drawable.file_mpg
      "pdf" -> R.drawable.file_pdf
      "png" -> R.drawable.file_png
      "ppt" -> R.drawable.file_ppt
      "psd" -> R.drawable.file_psd
      "rar" -> R.drawable.file_rar
      "txt" -> R.drawable.file_txt
      "wav" -> R.drawable.file_wav
      "xls" -> R.drawable.file_xls
      "xlsx" -> R.drawable.file_xlsx
      "zip" -> R.drawable.file_zip
      else -> R.drawable.file_default
    }
  }
}