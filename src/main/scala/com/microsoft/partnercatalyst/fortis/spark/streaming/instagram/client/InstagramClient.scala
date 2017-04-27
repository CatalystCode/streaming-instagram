package com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.client

import com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.InstagramAuth
import com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.dto.{InstagramItem, InstagramResponse}
import net.liftweb.json

@SerialVersionUID(100L)
abstract class InstagramClient(auth: InstagramAuth) extends Serializable {
  def loadNewInstagrams(): Iterable[InstagramItem] = {
    loadNewInstagramsPaginated()
  }

  private def loadNewInstagramsPaginated(url: Option[String] = None): Iterable[InstagramItem] = {
    implicit val formats = json.DefaultFormats

    val response = json.parse(fetchInstagramResponse(url))
      .extract[InstagramResponse]

    var payload = response.data

    if (response.pagination.isDefined) {
      payload ++= loadNewInstagramsPaginated(response.pagination.get.next_url)
    }

    payload
  }

  protected def fetchInstagramResponse(url: Option[String] = None): String
}
