package com.volkanatalan.memory.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.File
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_link.view.*
import kotlinx.android.synthetic.main.list_item_search.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import org.apmem.tools.layouts.FlowLayout
import android.text.style.UnderlineSpan
import android.text.SpannableString
import com.volkanatalan.memory.R
import com.volkanatalan.memory.classes.FileToImageResource
import com.volkanatalan.memory.classes.Memory
import kotlinx.android.synthetic.main.list_item_document.view.*


class SearchListViewAdapter(private val context: Context,
                                     var memories : MutableList<Memory>) : BaseAdapter(){

  private val TAG = "SearchListViewAdapter"

  private class ViewHolder {
    lateinit var titleTextView: TextView
    lateinit var imageContainer: LinearLayout
    lateinit var textTextView: TextView
    lateinit var tagContainer: FlowLayout
    lateinit var linkContainer: LinearLayout
    lateinit var documentContainer: GridLayout
  }




  @SuppressLint("SetTextI18n")
  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view: View
    val holder: ViewHolder
    val memory = memories[position]

    if (convertView == null) {

      view = LayoutInflater.from(context).inflate(R.layout.list_item_search, null)

      holder = ViewHolder()
      holder.titleTextView = view.titleTextView
      holder.imageContainer = view.imageContainer
      holder.textTextView = view.textTextView
      holder.tagContainer = view.tagContainer
      holder.linkContainer = view.linkContainer
      holder.documentContainer = view.documentContainer

      view.tag = holder
    }

    else {
      view = convertView
      holder = convertView.tag as ViewHolder
    }

    val titleTextView = holder.titleTextView
    val imageContainer = holder.imageContainer
    val textTextView = holder.textTextView
    val tagContainer = holder.tagContainer
    val linkContainer = holder.linkContainer
    val documentContainer = holder.documentContainer

    titleTextView.text = memory.title
    if (memory.text != ""){
      textTextView.visibility = View.VISIBLE
      textTextView.text = memory.text
    }

    setupImageContainer(memory, imageContainer)
    setupLinkContainer(memory, linkContainer)
    setupDocumentContainer(memory, documentContainer)
    setupTagContainer(memory.tags!!, tagContainer)

    return view
  }




  private fun setupImageContainer(memory: Memory, container: LinearLayout) {
    val images = memory.images
    if (images!!.size > 0) {

      container.removeAllViews()
      container.visibility = View.VISIBLE

      for (imagePath in images) {
        val imageView = LayoutInflater.from(context).inflate(R.layout.memory_image, null) as ImageView


        // Setup image view
        val imgFile = File(imagePath)
        if (imgFile.exists()) {

          val requestManager = Glide.with(context)
          val requestBuilder = requestManager.load(imgFile)
          requestBuilder.into(imageView)
        }

        // Add list row to  image container linear layout
        container.addView(imageView)
      }
    }
  }




  private fun setupLinkContainer(memory: Memory, container: LinearLayout){
    val links = memory.links
    if (links != null && links.size > 0) {

      container.removeAllViews()
      container.visibility = View.VISIBLE

      for (link in links){
        // Inflate link list item
        val linkRowLinearLayout = LayoutInflater.from(context).inflate(R.layout.list_item_link, null)
        val linkTextView = linkRowLinearLayout.linkTextView

        // Put underline to text
        var linkTitle = link.title
        val linkAddress = link.address
        if (linkTitle == "") linkTitle = linkAddress
        val content = SpannableString(linkTitle)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        linkTextView.text = content
      }
    }
  }




  private fun setupDocumentContainer(memory: Memory, container: GridLayout){
    val documents = memory.documents
    if (documents != null && documents.size > 0) {

      container.removeAllViews()
      container.visibility = View.VISIBLE

      for (document in documents){
        // Inflate link list item
        val documentRow = LayoutInflater.from(context).inflate(R.layout.list_item_document, null)
        val documentIconImageView = documentRow.documentIconImageView
        val documentNameTextView = documentRow.documentNameTextView

        // Setup document name text view
        val file = File(document)
        val fileName = file.name
        documentNameTextView.text = fileName

        // Setup document icon image view
        val fileIconResource = FileToImageResource().getResource(file)
        documentIconImageView.setImageResource(fileIconResource)

      }
    }
  }




  private fun setupTagContainer(tags: MutableList<String>, container: FlowLayout){
    container.removeAllViews()


    val layoutParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )
    layoutParams.setMargins(0, 0, 5, 0)


    // Firstly add "Tags:" label
    val tagsLabelTextView = TextView(context)
    tagsLabelTextView.setPadding(0, 5, 5, 0)
    val tagsLabel = context.resources.getString(R.string.tags_semicolon)
    tagsLabelTextView.text = tagsLabel

    val labelLayout = LinearLayout(context)
    labelLayout.addView(tagsLabelTextView, layoutParams)
    container.addView(labelLayout)


    // Then add the tag(s)
    for (tag in tags) {
      val tagLayout = LinearLayout(context)



      val view = LayoutInflater.from(context).inflate(R.layout.tag_view, null)
      val textView = view.textView

      textView.text = tag

      view.setOnClickListener{
        val text = ((it as RelativeLayout).getChildAt(0) as TextView).text.toString()
        onTagClickListener.onClick(text)
      }

      tagLayout.addView(view, layoutParams)
      container.addView(tagLayout)
    }
  }




  override fun getItem(position: Int): Memory {
    return memories[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return memories.size
  }

  override fun isEnabled(position: Int): Boolean {
    return false
  }

  lateinit var onTagClickListener: OnTagClickListener


  interface OnTagClickListener{
    fun onClick(tag: String)
  }
}