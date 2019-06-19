package com.volkanatalan.memory.helpers

import com.volkanatalan.memory.R
import java.io.File

/**
 * FileIconHelper helps to determine file icons from file extensions.
 */
class FileIconHelper {

  companion object {
  
    /**
     * Get file icon resource from file.
     */
    fun getResource(file: File): Int {
    
      return when (file.extension) {
        "3ds" -> R.drawable.file_3ds
        "3gp" -> R.drawable.file_3gp
        "aac" -> R.drawable.file_aac
        "ai" -> R.drawable.file_ai
        "avi" -> R.drawable.file_avi
        "bmp" -> R.drawable.file_bmp
        "cad" -> R.drawable.file_cad
        "cdr" -> R.drawable.file_cdr
        "css" -> R.drawable.file_css
        "dat" -> R.drawable.file_dat
        "dll" -> R.drawable.file_dll
        "dmg" -> R.drawable.file_dmg
        "doc" -> R.drawable.file_doc
        "docx" -> R.drawable.file_docx
        "epub" -> R.drawable.file_epub
        "eps" -> R.drawable.file_eps
        "fla" -> R.drawable.file_fla
        "flv" -> R.drawable.file_flv
        "gif" -> R.drawable.file_gif
        "html" -> R.drawable.file_html
        "indd" -> R.drawable.file_indd
        "iso" -> R.drawable.file_iso
        "jpg" -> R.drawable.file_jpg
        "jpeg" -> R.drawable.file_jpeg
        "js" -> R.drawable.file_js
        "midi" -> R.drawable.file_midi
        "mov" -> R.drawable.file_mov
        "mp3" -> R.drawable.file_mp3
        "mp4" -> R.drawable.file_mp4
        "mpg" -> R.drawable.file_mpg
        "obj" -> R.drawable.file_obj
        "pdf" -> R.drawable.file_pdf
        "php" -> R.drawable.file_php
        "png" -> R.drawable.file_png
        "ppt" -> R.drawable.file_ppt
        "ps" -> R.drawable.file_ps
        "psd" -> R.drawable.file_psd
        "rar" -> R.drawable.file_rar
        "raw" -> R.drawable.file_raw
        "sql" -> R.drawable.file_sql
        "svg" -> R.drawable.file_svg
        "tif" -> R.drawable.file_tif
        "txt" -> R.drawable.file_txt
        "wav" -> R.drawable.file_wav
        "wmv" -> R.drawable.file_wmv
        "xls" -> R.drawable.file_xls
        "xml" -> R.drawable.file_xml
        "zip" -> R.drawable.file_zip
        else -> R.drawable.file_default
      }
    }
  }
}