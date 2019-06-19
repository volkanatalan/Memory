package com.volkanatalan.memory.databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import com.volkanatalan.memory.models.Memory
import java.text.SimpleDateFormat
import java.util.*

/**
 * MemoryDatabase is a database which is used to save
 * memories to device storage. It has one table with 8 columns.
 */
class MemoryDatabase(context: Context, factory: CursorFactory?):
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
  
  
  
  
  /**
   * Check if there is a memory with the same id.
   * @param id Id of the memory which is wanted to be checked
   */
  fun hasMemory(id: Int): Boolean{
    val db = this.readableDatabase
    val c = db.rawQuery("SELECT $COLUMN_ID FROM $TABLE_MEMORIES WHERE $COLUMN_ID = \"$id\";", null)
    val has = c.moveToNext()

    c.close()
    db.close()

    return has
  }
  
  
  
  
  /**
   * Save a memory to storage of the device.
   */
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
  
  
  
  
  /**
   * Remove a memory from device storage.
   */
  fun forget(memory: Memory){
    val db = this.writableDatabase
    db.delete(TABLE_MEMORIES, "$COLUMN_ID = ${memory.id}", null)
    db.close()
  }
  
  
  
  
  /**
   * Find memories according to search text. Titles and texts of memories are searched.
   * @param searchText A text that database makes search accordingly.
   */
  fun rememberMemories(searchText: String): MutableList<Memory> {
    var subText = searchText
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
    val queryTitle = "SELECT * FROM $TABLE_MEMORIES WHERE ($titleConditionText);"
    val queryTag = "SELECT * FROM $TABLE_MEMORIES WHERE ($tagsConditionText);"
    
    //Log.d("database", query)
    
    // First add memories, found with title
    val cTitle = db.rawQuery(queryTitle, null)
    addMemoriesToList(cTitle, memories)
    
    // Then add memories, found with tag
    val cTag = db.rawQuery(queryTag, null)
    addMemoriesToList(cTag, memories)
    
    db.close()

   return memories
  }
  
  
  
  
  /**
   * Add memories from [Cursor] to [MutableList].
   */
  private fun addMemoriesToList(c: Cursor, list: MutableList<Memory>){
    while (c.moveToNext()) {
      val memory = Memory()
      memory.id = c.getInt(c.getColumnIndex(COLUMN_ID))
      
      if (!isListContainMemory(memory.id, list)) {
        memory.title = c.getString(c.getColumnIndex(COLUMN_TITLE))
        memory.date = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault())
          .parse(c.getString(c.getColumnIndex(COLUMN_DATE)))
        memory.text = c.getString(c.getColumnIndex(COLUMN_TEXT))
        memory.tags = stringToList(c.getString(c.getColumnIndex(COLUMN_TAGS)))
        memory.links = Memory()
          .linksFromString(c.getString(c.getColumnIndex(COLUMN_LINKS)))
        memory.images = stringToList(c.getString(c.getColumnIndex(COLUMN_IMAGES)))
        memory.documents = stringToList(c.getString(c.getColumnIndex(COLUMN_DOCUMENT)))
  
        list.add(memory)
      }
    }
  
    c.close()
  }
  
  
  
  
  /**
   * Check if there is a memory with the same id in given [MutableList].
   * @param memoryId Id of the memory to check its existance
   * @param list The list to check if it contains the given memory
   */
  private fun isListContainMemory(memoryId: Int, list: MutableList<Memory>): Boolean{
    for (memory in list){
      if (memory.id == memoryId)
        return true
    }
    return false
  }
  
  
  
  
  /**
   * Find the memory with given id.
   * @param id Id of the memory which is wanted to be found
   */
  fun remember(id: Int): Memory{
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
  
  
  
  
  /**
   * Pick a random memory from database.
   */
  fun rememberRandomMemory(): Memory?{
    var memory: Memory? = null
    val db = this.readableDatabase
    val c = db.rawQuery("SELECT * FROM $TABLE_MEMORIES ORDER BY RANDOM() LIMIT 1;", null)
    if (c.count > 0){
      c.moveToFirst()
      val id = c.getInt(c.getColumnIndex(COLUMN_ID))
      val title = c.getString(c.getColumnIndex(COLUMN_TITLE))
      val date = SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault())
        .parse(c.getString(c.getColumnIndex(COLUMN_DATE)))
      val text = c.getString(c.getColumnIndex(COLUMN_TEXT))
      val tags = stringToList(c.getString(c.getColumnIndex(COLUMN_TAGS)))
      val links = Memory().linksFromString(c.getString(c.getColumnIndex(COLUMN_LINKS)))
      val images = stringToList(c.getString(c.getColumnIndex(COLUMN_IMAGES)))
      val documents = stringToList(c.getString(c.getColumnIndex(COLUMN_DOCUMENT)))
  
      memory = Memory(id, date, title, text, tags, links, images, documents)
    }
    
    c.close()
    db.close()
    
    return memory
  }
  
  
  
  
  /**
   * Update a memory.
   * @param memory Memory to update
   */
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
  
  
  
  
  /**
   * Convert string with "[..., ...]" format to [MutableList].
   * @param text A text with "[..., ...]" format
   */
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
  
  
  /**
   * Get sorted searchables (titles and tags) from database with a [MutableList] of [Pair]
   * @return The first parameter of [Pair] is type (Title or Tag), and the second one is name.
   */
  fun getSearchables():MutableList<Pair<String, String>>{
    val searchables = mutableListOf<Pair<String, String>>()
    val db = this.readableDatabase
    val c = db.rawQuery("SELECT * FROM $TABLE_MEMORIES;", null)
    while (c.moveToNext()){
      
      // Add title to searchables list
      val title = c.getString(c.getColumnIndex(COLUMN_TITLE))
      searchables.add(Pair("Title", title))
  
      // Add tags to searchables list
      val columnTags = stringToList(c.getString(c.getColumnIndex(COLUMN_TAGS)))
      for (tag in columnTags){
        if (!searchables.contains(Pair("Tag", tag))){
          searchables.add(Pair("Tag", tag))
        }
      }
    }
  
    c.close()
    db.close()
    
    return searchables.sortedWith(compareBy({it.second}, {it.first})) as MutableList<Pair<String, String>>
  }
  
  
  
  
  /**
   * Get titles of saved memories from database.
   */
  fun getTitles(): MutableList<String>{
    val titles = mutableListOf<String>()
    val db = this.readableDatabase
    val c = db.rawQuery("SELECT $COLUMN_TITLE FROM $TABLE_MEMORIES;", null)
    while (c.moveToNext()){
      titles.add(c.getString(c.getColumnIndex(COLUMN_TITLE)))
    }
    
    c.close()
    db.close()
  
    titles.sort()
    
    return titles
  }
}