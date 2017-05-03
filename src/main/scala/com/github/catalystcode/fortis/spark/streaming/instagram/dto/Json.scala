package com.github.catalystcode.fortis.spark.streaming.instagram.dto

case class InstagramResponse(
  data: List[InstagramItem],
  pagination: Option[InstagramPagination] = None,
  meta: Option[InstagramMeta] = None
)

case class InstagramPagination(
  next_url: Option[String] = None,
  next_min_id: Option[String] = None,
  next_max_id: Option[String] = None
)

case class InstagramMeta(
  code: Int,
  error_type: Option[String] = None,
  error_message: Option[String] = None
)

case class InstagramItem(
  `type`: String,
  users_in_photo: List[InstagramUser] = List(),
  filter: Option[String] = None,
  tags: List[String] = List(),
  comments: InstagramComments,
  caption: InstagramCaption,
  user_has_liked: Option[Boolean] = None,
  likes: InstagramLikes,
  link: String,
  user: InstagramUser,
  created_time: String,
  images: InstagramImages,
  id: String,
  location: Option[InstagramLocation] = None
)

case class InstagramComments(
  count: Int
)

case class InstagramCaption(
  created_time: String,
  text: String,
  from: InstagramUser,
  id: String
)

case class InstagramUser(
  username: String,
  id: String,
  profile_picture: Option[String] = None,
  full_name: Option[String] = None
)

case class InstagramLikes(
  count: Int
)

case class InstagramImages(
  low_resolution: InstagramImage,
  standard_resolution: InstagramImage,
  thumbnail: InstagramImage
)

case class InstagramImage(
  url: String,
  width: Int,
  height: Int
)

case class InstagramLocation(
  latitude: Double,
  longitude: Double,
  id: Option[String] = None,
  name: Option[String] = None
)
