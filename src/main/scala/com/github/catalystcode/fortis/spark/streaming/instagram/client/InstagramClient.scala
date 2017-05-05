package com.github.catalystcode.fortis.spark.streaming.instagram.client

import java.io.IOException

import com.github.catalystcode.fortis.spark.streaming.instagram.InstagramAuth
import com.github.catalystcode.fortis.spark.streaming.instagram.dto.{InstagramItem, InstagramResponse}
import net.liftweb.json

@SerialVersionUID(100L)
abstract class InstagramClient(auth: InstagramAuth) extends Serializable {
  def loadNewInstagrams(): Iterable[InstagramItem] = {
    try {
      loadNewInstagramsPaginated()
    } catch {
      case ex: IOException => println(ex); List()
    }
  }

  private def loadNewInstagramsPaginated(url: Option[String] = None): Iterable[InstagramItem] = {
    implicit val formats = json.DefaultFormats

    val response = json.parse(fetchInstagramResponse(url))
      .extract[InstagramResponse]

    println(s"Got json response with ${response.data.length} entries")
    var payload = response.data

    if (response.pagination.isDefined && response.pagination.get.next_url.isDefined) {
      println(s"Fetching next results page from ${response.pagination.get.next_url}")
      payload ++= loadNewInstagramsPaginated(response.pagination.get.next_url)
    }

    payload
  }

  protected def fetchInstagramResponse(url: Option[String] = None): String
}
