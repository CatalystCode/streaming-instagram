package com.microsoft.partnercatalyst.fortis.spark.sources.instagram.dto

case class JsonInstagramResponse(
  data: List[JsonInstagramItem],
  pagination: Option[JsonInstagramPagination] = None,
  meta: Option[JsonInstagramMeta] = None
)

case class JsonInstagramPagination(
  next_url: Option[String] = None,
  next_min_id: Option[String] = None,
  next_max_id: Option[String] = None
)

case class JsonInstagramMeta(
  code: Int,
  error_type: Option[String] = None,
  error_message: Option[String] = None
)

case class JsonInstagramItem(
  `type`: String,
  users_in_photo: Option[List[JsonInstagramUser]] = None,
  filter: Option[String] = None,
  tags: Option[List[String]] = None,
  comments: JsonInstagramComments,
  caption: JsonInstagramCaption,
  user_has_liked: Boolean,
  likes: JsonInstagramLikes,
  link: String,
  user: JsonInstagramUser,
  created_time: String,
  images: JsonInstagramImages,
  id: String,
  location: Option[JsonInstagramLocation] = None
)

case class JsonInstagramComments(
  count: Int
)

case class JsonInstagramCaption(
  created_time: String,
  text: String,
  from: JsonInstagramUser,
  id: String
)

case class JsonInstagramUser(
  username: String,
  id: String,
  profile_picture: Option[String] = None,
  full_name: Option[String] = None
)

case class JsonInstagramLikes(
  count: Int
)

case class JsonInstagramImages(
  low_resolution: JsonInstagramImage,
  standard_resolution: JsonInstagramImage,
  thumbnail: JsonInstagramImage
)

case class JsonInstagramImage(
  url: String,
  width: Int,
  height: Int
)

case class JsonInstagramLocation(
  latitude: Double,
  longitude: Double,
  id: Option[String] = None,
  name: Option[String] = None
)
