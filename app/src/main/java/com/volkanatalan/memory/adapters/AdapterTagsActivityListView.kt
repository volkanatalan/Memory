package com.volkanatalan.memory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.volkanatalan.memory.R
import kotlinx.android.synthetic.main.list_item_tags_activity.view.*

/**
 * An adapter for ListView in [SearchablesActivity].
 * It contains only a TextView to show titles of saved memories.
 */
class AdapterTagsActivityListView(
  private val context: Context,
  private val searchItems:MutableList<String>
): BaseAdapter() {
  
  
  
  class ViewHolder(view: View){
    var title = view.title!!
  }
  
  
  
  
  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val viewHolder: ViewHolder
    val view: View
    
    if (convertView == null){
      view = LayoutInflater.from(context).inflate(R.layout.list_item_tags_activity, parent, false)
      viewHolder = ViewHolder(view)
       view.tag = viewHolder
    }
    else {
    view = convertView
    viewHolder = convertView.tag as ViewHolder
    }
    
    viewHolder.title.text = searchItems[position]
  
    return view
  }
  
  
  
  
  
  override fun getItem(position: Int): String {
  return searchItems[position]
  }
  
  
  
  
  
  override fun getItemId(position: Int): Long {
  return position.toLong()
  }
  
  
  
  
  
  override fun getCount(): Int {
    return searchItems.size
  }
}