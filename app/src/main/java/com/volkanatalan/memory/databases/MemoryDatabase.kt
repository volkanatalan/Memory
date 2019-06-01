package com.volkanatalan.memory.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.volkanatalan.memory.classes.Memory
import java.text.SimpleDateFormat
import java.util.*

class MemoryDatabase(context: Context, factory: CursorFactory?) :
  SQLiteOpenHelper(context,
      DATABASE_NAME, factory,
      DATABASE_VERSION
  ) {


  companion object {
    private val DATABASE_VERSION = 1
    private val DATABASE_NAME = "MemoryDatabase.db"
    private val TABLE_MEMORIES = "Memories"
    private val COLUMN_ID = "_id"
    private val COLUMN_DATE = "Date"
    private val COLUMN_TAGS = "Tags"
    private val COLUMN_LINKS = "Links"
    private val COLUMN_IMAGES = "Images"
    private val COLUMN_DOCUMENT = "Documents"
    private val COLUMN_TITLE = "Titles"
    private val COLUMN_TEXT = "Text"
  }




  override fun onCreate(db: SQLiteDatabase) {
    val createTable = (
      "CREATE TABLE " +
      TABLE_MEMORIES + "(" +
      COLUMN_ID + " INTEGER PRIMARY KEY," +
      COLUMN_DATE + " TEXT," +
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





  fun hasMemory(id: Int): Boolean{
    val db = this.readableDatabase
    val c = db.rawQuery("SELECT $COLUMN_ID FROM $TABLE_MEMORIES WHERE $COLUMN_ID = \"$id\";", null)
    val has = c.moveToNext()

    c.close()
    db.close()

    return has
  }




  fun memorize(memory: Memory) {
    val db = this.writableDatabase

    if (memory.id == -1 || !hasMemory(memory.id)) {
      val values = ContentValues()
      values.put(COLUMN_DATE, SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault()).format(memory.date))
      values.put(COLUMN_TAGS, memory.tags.toString())
      values.put(COLUMN_LINKS, memory.linksToString())
      values.put(COLUMN_IMAGES, memory.images.toString())
      values.put(COLUMN_DOCUMENT, memory.documents.toString())
      values.put(COLUMN_TITLE, memory.title)
      values.put(COLUMN_TEXT, memory.text)
      db.insert(TABLE_MEMORIES, null, values)
    }

    else{
      update(memory)
    }


    db.close()
  }





  fun forget(memory: Memory){
    val db = this.writableDatabase
    db.delete(TABLE_MEMORIES, "$COLUMN_ID = ${memory.id}", null)
    db.close()
  }





  fun rememberMemories(text:String): MutableList<Memory> {
    var subText = text
    val subTextList = mutableListOf<String>()
    
    while (subText.isNotEmpty()){
      val end = subText.indexOf(" ")
      subText = if (end > -1) {
        subTextList.add(subText.substring(0, end))
        subText.substring(end + 1, subText.length)
      } else {
        subTextList.add(subText)
        ""
      }
    }
  
    var tagsConditionText = ""
    var titleConditionText = ""
    for (index in 0 until subTextList.size) {
      if (index != subTextList.size - 1) {
        tagsConditionText += "$COLUMN_TAGS LIKE '%${subTextList[index]}%' AND "
        titleConditionText += "$COLUMN_TITLE LIKE '%${subTextList[index]}%' AND "
      }
      else {
        tagsConditionText += "$COLUMN_TAGS LIKE '%${subTextList[index]}%'"
        titleConditionText += "$COLUMN_TITLE LIKE '%${subTextList[index]}%'"
      }
    }
    
    
    val memories = mutableListOf<Memory>()
    val db = this.readableDatabase
    val query = "SELECT * FROM $TABLE_MEMORIES " +
      "WHERE ($tagsConditionText) " +
      "OR ($titleConditionText);"
    
    //Log.d("database", query)
    
    val c = db.rawQuery(query, null)
    while (c.moveToNext()) {
      val memory = Memory()
      memory.id = c.getInt(c.getColumnIndex(COLUMN_ID))
      memory.title = c.getString(c.getColumnIndex(COLUMN_TITLE))
      memory.date = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault())
        .parse(c.getString(c.getColumnIndex(COLUMN_DATE)))
      memory.text = c.getString(c.getColumnIndex(COLUMN_TEXT))
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





  fun rememberSomething(id: Int): Memory{
    val db = this.readableDatabase
    val c = db.rawQuery("SELECT * FROM $TABLE_MEMORIES WHERE $COLUMN_ID = \"$id\";", null)
    c.moveToFirst()
    val title = c.getString(c.getColumnIndex(COLUMN_TITLE))
    val date = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault())
      .parse(c.getString(c.getColumnIndex(COLUMN_DATE)))
    val text = c.getString(c.getColumnIndex(COLUMN_TEXT))
    val tags = stringToList(c.getString(c.getColumnIndex(COLUMN_TAGS)))
    val links = Memory().linksFromString(c.getString(c.getColumnIndex(COLUMN_LINKS)))
    val images = stringToList(c.getString(c.getColumnIndex(COLUMN_IMAGES)))
    val documents = stringToList(c.getString(c.getColumnIndex(COLUMN_DOCUMENT)))

    val memory = Memory(id, date, title, text, tags, links, images, documents)

    c.close()
    db.close()

    return memory
  }





  fun update(memory: Memory){
    val db = this.writableDatabase
    val values = ContentValues()
    values.put(COLUMN_ID, memory.id)
    values.put(COLUMN_TITLE, memory.title)
    values.put(COLUMN_TEXT, memory.text)
    values.put(COLUMN_TAGS, memory.tags.toString())
    values.put(COLUMN_LINKS, memory.linksToString())
    values.put(COLUMN_IMAGES, memory.images.toString())
    values.put(COLUMN_DOCUMENT, memory.documents.toString())
    db.update(TABLE_MEMORIES, values, "$COLUMN_ID = ?", arrayOf("${memory.id}"))
  }




  private fun stringToList(text:String):MutableList<String> {
    val list = mutableListOf<String>()
    var mutableText = text
    mutableText = mutableText.substring(1, mutableText.length - 1)


    while (mutableText.isNotEmpty() && mutableText != "ul") {

      var end = mutableText.indexOf(",", 0, true)
      if (end == -1) end = mutableText.length
      val subtext = mutableText.substring(0, end)

      mutableText = if (end + 2 < mutableText.length) mutableText.substring(end + 2, mutableText.length)
      else ""

      list.add(subtext)
    }

    return list
  }
}