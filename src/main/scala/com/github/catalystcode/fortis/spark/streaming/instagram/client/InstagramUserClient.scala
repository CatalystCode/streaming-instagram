package com.github.catalystcode.fortis.spark.streaming.instagram.client

import com.github.catalystcode.fortis.spark.streaming.instagram.InstagramAuth

import scala.io.Source

@SerialVersionUID(100L)
class InstagramUserClient(userId: String, auth: InstagramAuth) extends InstagramClient(auth) {
  override protected def fetchInstagramResponse(url: Option[String] = None): String = {
    val fetch = url.getOrElse(s"https://${auth.apiHost}/v1/users/$userId/media/recent?access_token=${auth.accessToken}")
    logInfo(s"Fetching response from $fetch")
    Source.fromURL(fetch).mkString
  }
}
