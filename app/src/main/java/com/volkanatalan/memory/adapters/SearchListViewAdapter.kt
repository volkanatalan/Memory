package com.volkanatalan.memory.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
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
import com.volkanatalan.memory.helpers.FileIconHelper
import com.volkanatalan.memory.classes.Link
import com.volkanatalan.memory.classes.Memory
import kotlinx.android.synthetic.main.list_item_document.view.*
import kotlinx.android.synthetic.main.memory_image.view.*


class SearchListViewAdapter(private val context: Context,
                                     var memories : MutableList<Memory>) : BaseAdapter(){

  private val TAG = "SearchListViewAdapter"

  private class ViewHolder {
    lateinit var titleTextView: TextView
    lateinit var imageContainer: LinearLayout
    lateinit var textTextView: TextView
    lateinit var tagContainer: FlowLayout
    lateinit var linkBase: LinearLayout
    lateinit var documentBase: LinearLayout
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
      holder.linkBase = view.linkBase
      holder.documentBase = view.documentBase

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
    val linkBase = holder.linkBase
    val documentBase = holder.documentBase

    titleTextView.text = memory.title
    if (memory.text != ""){
      textTextView.visibility = View.VISIBLE
      textTextView.text = memory.text
    }

    setupImageContainer(memory.images, imageContainer)
    setupLinkContainer(memory.links, linkBase)
    setupDocumentContainer(memory.documents, documentBase)
    setupTagContainer(memory.tags!!, tagContainer)

    return view
  }




  private fun setupImageContainer(images: MutableList<String>?, container: LinearLayout) {
    if (images != null && images.size > 0) {

      container.removeAllViews()
      container.visibility = View.VISIBLE

      for (imagePath in images) {

        val imgFile = File(imagePath)
        if (imgFile.exists()) {

          // Get image dimensions
          val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
          BitmapFactory.decodeFile(imgFile.path, options)
          val imageWidth: Int = options.outWidth
          val imageHeight: Int = options.outHeight
          val imageRatio = imageWidth.toFloat() / imageHeight.toFloat()

          val screenWidth = context.resources.displayMetrics.widthPixels
          val pagePadding = context.resources.getDimensionPixelSize(R.dimen.page_padding) * 2
          val sectionPadding = context.resources.getDimensionPixelSize(R.dimen.section_padding) * 2
          val imageMaxWidth = screenWidth - pagePadding - sectionPadding
          val imageMaxHeight = (imageMaxWidth / imageRatio).toInt()


          // Create root
          var root = LinearLayout(context)
          val params = LinearLayout.LayoutParams(imageMaxWidth, imageMaxHeight)
          root.layoutParams = params


          // Inflate root
          root = LayoutInflater.from(context).inflate(R.layout.memory_image, root) as LinearLayout
          val imageView = root.memory_image_view


          // Setup image view
          Glide.with(context).load(imgFile).into(imageView)


          // Add list row to  image container linear layout
          container.addView(root)
        }
      }
    }
  }




  private fun setupLinkContainer(links: MutableList<Link>?, base: LinearLayout){
    if (links != null && links.size > 0) {

      base.linkContainer.removeAllViews()
      base.visibility = View.VISIBLE

      for (link in links){
        // Inflate link list item
        val linkLayout = LayoutInflater.from(context).inflate(R.layout.list_item_link, null)
        val linkTextView = linkLayout.linkTextView

        // Put underline to text
        var linkTitle = link.title
        val linkAddress = link.address
        if (linkTitle == "") linkTitle = linkAddress
        val content = SpannableString(linkTitle)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        linkTextView.text = content

        base.linkContainer.addView(linkLayout)
      }
    }
  }




  private fun setupDocumentContainer(documents: MutableList<String>?, base: LinearLayout){
    if (documents != null && documents.size > 0) {

      base.documentContainer.removeAllViews()
      base.visibility = View.VISIBLE

      for (document in documents){
        // Inflate link list item
        val documentLayout = LayoutInflater.from(context).inflate(R.layout.list_item_document, LinearLayout(context))
        val documentIconImageView = documentLayout.documentIconImageView
        val documentNameTextView = documentLayout.documentNameTextView

        // Setup document name text view
        val file = File(document)
        val fileName = file.name
        documentNameTextView.text = fileName

        // Setup document icon image view
        val fileIconResource = FileIconHelper().getResource(file)
        documentIconImageView.setImageResource(fileIconResource)

        base.documentContainer.addView(documentLayout)
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