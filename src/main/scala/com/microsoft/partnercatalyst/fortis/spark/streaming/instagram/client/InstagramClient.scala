package com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.client

import java.io.IOException

import com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.InstagramAuth
import com.microsoft.partnercatalyst.fortis.spark.streaming.instagram.dto.{InstagramItem, InstagramResponse}
import net.liftweb.json

@SerialVersionUID(100L)
abstract class InstagramClient(auth: InstagramAuth) extends Serializable {
  def loadNewInstagrams(): Iterable[InstagramItem] = {
    try {
      loadNewInstagramsPaginated()
    } catch {
      case ex: IOException => List()
    }
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
