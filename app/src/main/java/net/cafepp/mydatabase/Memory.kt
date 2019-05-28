package net.cafepp.mydatabase

class Memory(){
    var id: Int = -1
    var title: String = ""
    var text: String = ""
    var tags: MutableList<String>? = null
    var images: MutableList<String>? = null

    constructor(id: Int, title: String, text: String, tags: MutableList<String>?, images: MutableList<String>?) : this() {
        this.id = id
        this.title = title
        this.text = text
        this.tags = tags
        this.images = images
    }
}