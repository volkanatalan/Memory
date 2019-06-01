package com.volkanatalan.memory.classes


class Memory(){

  var id: Int = -1
  var title: String = ""
  var text: String = ""
  var tags: MutableList<String> = mutableListOf()
  var links: MutableList<Link> = mutableListOf()
  var images: MutableList<String> = mutableListOf()
  var documents: MutableList<String> = mutableListOf()



  constructor(id: Int,
              title: String,
              text: String,
              tags: MutableList<String>,
              links: MutableList<Link>,
              images: MutableList<String>,
              documents: MutableList<String>) : this() {

    this.id = id
    this.title = title
    this.text = text
    this.tags = tags
    this.links = links
    this.images = images
    this.documents = documents
  }




  fun linksToString(): String{
    var text = ""

    for (link in links){
      text += "[${link.title},${link.address}]"
    }

    return text
  }




  fun linksFromString(text: String): MutableList<Link>{
    val linkList = mutableListOf<Link>()
    var fText = text

    while (fText.isNotEmpty()){
      val linkStartPoint = fText.indexOf("[") + 1
      val linkEndPoint = fText.indexOf("]")
      val linkRaw = fText.substring(linkStartPoint, linkEndPoint)

      fText = fText.substring(linkEndPoint + 1, fText.length)

      val linkTitleEndPoint = linkRaw.indexOf(",")
      val linkTitle = linkRaw.substring(0, linkTitleEndPoint)

      val linkAddressStartPoint = linkTitleEndPoint + 1
      val linkAddressEndPoint = linkRaw.length
      val linkAddress = linkRaw.substring(linkAddressStartPoint, linkAddressEndPoint)

      val link = Link(linkAddress, linkTitle)
      linkList.add(link)
    }

    return linkList
  }
}