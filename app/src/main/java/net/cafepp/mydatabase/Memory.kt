package net.cafepp.mydatabase

class Memory(){
  var id: Int = -1
  var title: String = ""
  var text: String = ""
  var tags: MutableList<String>? = null
  var links: MutableList<String>? = null
  var images: MutableList<String>? = null
  var documents: MutableList<String>? = null

  constructor(id: Int,
              title: String,
              text: String,
              tags: MutableList<String>?,
              links: MutableList<String>?,
              images: MutableList<String>?,
              documents: MutableList<String>?) : this() {

    this.id = id
    this.title = title
    this.text = text
    this.tags = tags
    this.links = links
    this.images = images
    this.documents = documents
  }
}