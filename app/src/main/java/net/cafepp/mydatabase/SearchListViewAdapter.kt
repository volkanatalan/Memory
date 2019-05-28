package net.cafepp.mydatabase

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.File
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_row_search.view.*
import kotlinx.android.synthetic.main.tag_view.view.*
import org.apmem.tools.layouts.FlowLayout


class SearchListViewAdapter(private val context: Context,
                                     var memories : MutableList<Memory>) : BaseAdapter(){

    private val TAG = "SearchListViewAdapter"

    private class ViewHolder {
        lateinit var titleTextView: TextView
        lateinit var imageContainer: LinearLayout
        lateinit var textTextView: TextView
        lateinit var tagContainer: FlowLayout
    }




    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder
        val memory = memories[position]

        if (convertView == null) {

            view = LayoutInflater.from(context).inflate(R.layout.list_row_search, null)

            holder = ViewHolder()
            holder.titleTextView = view.titleTextView
            holder.imageContainer = view.imageContainer
            holder.textTextView = view.textTextView
            holder.tagContainer = view.tagContainer

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val titleTextView = holder.titleTextView
        val imageContainer = holder.imageContainer
        val textTextView = holder.textTextView
        val tagContainer = holder.tagContainer

        titleTextView.text = memory.title
        textTextView.text = memory.text

        setupImageContainer(memory, imageContainer)

        setupTagContainer(tagContainer, memory.tags!!)

        return view
    }




    private fun setupImageContainer(memory: Memory, imageContainer: LinearLayout) {
        val images = memory.images
        imageContainer.removeAllViews()
        for (imagePath in images!!) {
            val imageView = LayoutInflater.from(context).inflate(R.layout.memory_image, null) as ImageView


            // Setup image view
            val imgFile = File(imagePath)
            if (imgFile.exists()) {

                val requestManager = Glide.with(context)
                val requestBuilder = requestManager.load(imgFile)
                requestBuilder.into(imageView)
            }

            // Add list row to  image container linear layout
            imageContainer.addView(imageView)
        }
    }




    private fun setupTagContainer(container: FlowLayout, tags: MutableList<String>){
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