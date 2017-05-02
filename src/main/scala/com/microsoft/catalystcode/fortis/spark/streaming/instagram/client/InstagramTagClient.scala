package com.microsoft.catalystcode.fortis.spark.streaming.instagram.client

import com.microsoft.catalystcode.fortis.spark.streaming.instagram.InstagramAuth

import scala.io.Source

@SerialVersionUID(100L)
class InstagramTagClient(tag: String, auth: InstagramAuth) extends InstagramClient(auth) {
  override protected def fetchInstagramResponse(url: Option[String] = None): String = {
    val fetch = url.getOrElse(s"https://${auth.apiHost}/v1/tags/$tag/media/recent?access_token=${auth.accessToken}")
    Source.fromURL(fetch).mkString
  }
}
