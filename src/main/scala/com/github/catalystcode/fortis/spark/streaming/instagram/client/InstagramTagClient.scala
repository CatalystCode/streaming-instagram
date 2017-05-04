package com.github.catalystcode.fortis.spark.streaming.instagram.client

import com.github.catalystcode.fortis.spark.streaming.instagram.InstagramAuth

import scala.io.Source

@SerialVersionUID(100L)
class InstagramTagClient(tag: String, auth: InstagramAuth) extends InstagramClient(auth) {
  override protected def fetchInstagramResponse(url: Option[String] = None): String = {
    val fetch = url.getOrElse(s"https://${auth.apiHost}/v1/tags/$tag/media/recent?access_token=${auth.accessToken}")
    println(s"Fetching response from $fetch")
    Source.fromURL(fetch).mkString
  }
}
