package com.volkanatalan.memory.helpers

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.volkanatalan.memory.R
import com.volkanatalan.memory.classes.Link
import com.volkanatalan.memory.classes.Memory
import kotlinx.android.synthetic.main.list_item_document.view.*
import kotlinx.android.synthetic.main.list_item_link.view.*
import kotlinx.android.synthetic.main.list_item_search.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import org.apmem.tools.layouts.FlowLayout
import java.io.File


class ReminiscenceHelper(
  private val context: Context,
  private val container: LinearLayout) {



  fun remember(memories: MutableList<Memory>): ReminiscenceHelper {

    // Clear the container
    container.removeAllViews()

    // Add memories to container
    for (memory in memories) {
      // Create root
      val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
      val margin = context.resources.getDimensionPixelSize(R.dimen.memory_top_bottom_margin)
      params.setMargins(0, margin, 0, margin)
      var root = LinearLayout(context)
      root.layoutParams = params

      // Inflate root
      root = LayoutInflater.from(context).inflate(R.layout.list_item_search, root) as LinearLayout

      val titleTextView = root.titleTextView
      val imageContainer = root.imageContainer
      val textTextView = root.textTextView
      val tagContainer = root.tagContainer
      val linkBase = root.linkBase
      val documentBase = root.documentBase


      // Setup title end text
      titleTextView.text = memory.title
      if (memory.text != "") {
        textTextView.visibility = View.VISIBLE
        textTextView.text = memory.text
      }


      setupImageContainer(memory.images, imageContainer)
      setupLinkContainer(memory.links, linkBase)
      setupDocumentContainer(memory.documents, documentBase)
      setupTagContainer(memory.tags!!, tagContainer)


      container.addView(root)
    }

    return this
  }




  fun forget(){
    container.removeAllViews()
  }





  private fun setupImageContainer(images: MutableList<String>?, container: LinearLayout) {
    if (images != null && images.size > 0) {

      container.removeAllViews()
      container.visibility = View.VISIBLE

      for (imagePath in images) {

        val imgFile = File(imagePath)
        if (imgFile.exists()) {

          // Inflate root
          val imageView = LayoutInflater.from(context).inflate(R.layout.memory_image, null) as ImageView


          // Setup image view
          Glide.with(context).load(imgFile).into(imageView)


          // Add list row to  image container linear layout
          container.addView(imageView)
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
        // Create root
        val params = LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(5, 5, 5, 5)
        var root = LinearLayout(context)
        root.layoutParams = params

        // Inflate link list item
        root = LayoutInflater.from(context).inflate(R.layout.list_item_document, root) as LinearLayout
        val documentIconImageView = root.documentIconImageView
        val documentNameTextView = root.documentNameTextView

        // Setup document name text view
        val file = File(document)
        val fileName = file.name
        documentNameTextView.text = fileName

        // Setup document icon image view
        val fileIconResource = FileIconHelper().getResource(file)
        documentIconImageView.setImageResource(fileIconResource)

        base.documentContainer.addView(root)
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




  lateinit var onTagClickListener: OnTagClickListener


  interface OnTagClickListener{
    fun onClick(tag: String)
  }
}