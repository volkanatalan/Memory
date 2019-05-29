package com.volkanatalan.memory.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import com.volkanatalan.memory.classes.Memory

class MemoryDatabase(context: Context, factory: CursorFactory?) :
  SQLiteOpenHelper(context,
      DATABASE_NAME, factory,
      DATABASE_VERSION
  ) {


  companion object {
    private val DATABASE_VERSION = 1
    private val DATABASE_NAME = "MemoryDatabase.db"
    val TABLE_MEMORIES = "Memories"
    val COLUMN_ID = "_id"
    val COLUMN_TAGS = "Tags"
    val COLUMN_LINKS = "Links"
    val COLUMN_IMAGES = "Images"
    val COLUMN_DOCUMENT = "Documents"
    val COLUMN_TITLE = "Titles"
    val COLUMN_TEXT = "Text"
  }




  override fun onCreate(db: SQLiteDatabase) {
    val createTable = ("CREATE TABLE " +
            TABLE_MEMORIES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_TAGS + " TEXT," +
            COLUMN_LINKS + " TEXT," +
            COLUMN_IMAGES + " TEXT," +
            COLUMN_DOCUMENT + " TEXT," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_TEXT + " TEXT" + ")")

    db.execSQL(createTable)
  }




  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL("DROP TABLE IF EXISTS $TABLE_MEMORIES")
    onCreate(db)
  }




  fun addMemory(memory: Memory) {
    val values = ContentValues()
    values.put(COLUMN_TAGS, memory.tags.toString())
    values.put(COLUMN_LINKS, memory.linksToString())
    values.put(COLUMN_IMAGES, memory.images.toString())
    values.put(COLUMN_DOCUMENT, memory.documents.toString())
    values.put(COLUMN_TITLE, memory.title)
    values.put(COLUMN_TEXT, memory.text)
    val db = this.writableDatabase
    db.insert(TABLE_MEMORIES, null, values)
    db.close()
  }




  fun getMemories(text:String): MutableList<Memory> {
    val memories = mutableListOf<Memory>()
    val db = this.readableDatabase
    val c = db.rawQuery(
      "SELECT * FROM $TABLE_MEMORIES " +
        "WHERE $COLUMN_TAGS LIKE '%$text%' " +
        "OR $COLUMN_TITLE LIKE '%$text%';", null)

    while (c.moveToNext()) {
      val memory = Memory()
      memory.id = c.getInt(c.getColumnIndex(COLUMN_ID))
      memory.text = c.getString(c.getColumnIndex(COLUMN_TEXT))
      memory.title = c.getString(c.getColumnIndex(COLUMN_TITLE))
      memory.tags = stringToList(c.getString(c.getColumnIndex(COLUMN_TAGS)))
      memory.links = Memory()
          .linksFromString(c.getString(c.getColumnIndex(COLUMN_LINKS)))
      memory.images = stringToList(c.getString(c.getColumnIndex(COLUMN_IMAGES)))
      memory.documents = stringToList(c.getString(c.getColumnIndex(COLUMN_DOCUMENT)))

      memories.add(memory)
    }

    c.close()
    db.close()

   return memories
  }



  private fun stringToList(text:String):MutableList<String> {
    val list = mutableListOf<String>()
    var mutableText = text
    mutableText = mutableText.substring(1, mutableText.length - 1)


    while (mutableText.isNotEmpty() && mutableText != "ul") {

      var end = mutableText.indexOf(",", 0, true)
      if (end == -1) end = mutableText.length
      val subtext = mutableText.substring(0, end)
      if (end + 2 < mutableText.length)
          mutableText = mutableText.substring(end + 2, mutableText.length)
      else
          mutableText = ""

      list.add(subtext)
    }

    return list
  }
}