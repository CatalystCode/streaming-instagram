package com.microsoft.partnercatalyst.fortis.spark.sources.instagram.client

import com.microsoft.partnercatalyst.fortis.spark.sources.instagram.InstagramAuth
import com.microsoft.partnercatalyst.fortis.spark.sources.instagram.dto.{InstagramItem, InstagramResponse}
import net.liftweb.json

@SerialVersionUID(100L)
abstract class InstagramClient(auth: InstagramAuth) extends Serializable {
  def loadNewInstagrams(): Iterable[InstagramItem] = {
    implicit val formats = json.DefaultFormats

    json.parse(fetchInstagramResponse())
      .extract[InstagramResponse]
      .data
  }

  protected def fetchInstagramResponse(): String
}
