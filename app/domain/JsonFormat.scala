package domain

import java.util.Date
import play.api.libs.json.JsPath
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class JsonVoicebankFormat(id: Int, name: String, registTime: String, updateTime: String)

case class JsonVoicebanksFormat(pageNow: Int, voicebanksCount: Int, voicebanks: List[JsonVoicebankFormat])

case class JsonActivityFormat(filename: String, count: Int, timestamp: String)

case class JsonVoicebankActivityFormat(pageNow: Int, id: Int, name: String, voiceCount: Int, activities: List[JsonActivityFormat])

case class JsonRecentActivityFormat(id: Int, name: String, timestamp: String)

object JsonFormat {
implicit lazy val jsonVoicebanksFormat: Format[JsonVoicebanksFormat] = (
    (__ \ "pageNow").format[Int] and
    (__ \ "voicebanksCount").format[Int] and
    (__ \ "voicebanks").format[List[JsonVoicebankFormat]]
  )(JsonVoicebanksFormat.apply, unlift(JsonVoicebanksFormat.unapply))
  
implicit lazy val jsonVoicebankFormat: Format[JsonVoicebankFormat] = (
    (__ \ "id").format[Int] and
    (__ \ "name").format[String] and
    (__ \ "registTime").format[String] and
    (__ \ "updateTime").format[String]
  )(JsonVoicebankFormat.apply, unlift(JsonVoicebankFormat.unapply))
  
implicit lazy val jsonActivityFormat: Format[JsonActivityFormat] = (
    (__ \ "filename").format[String] and
    (__ \ "count").format[Int] and
    (__ \ "timestamp").format[String]
  )(JsonActivityFormat.apply, unlift(JsonActivityFormat.unapply))
  
implicit lazy val jsonVoicebankActivityFormat: Format[JsonVoicebankActivityFormat] = (
    (__ \ "pageNow").format[Int] and
    (__ \ "id").format[Int] and
    (__ \ "name").format[String] and
    (__ \ "voiceCount").format[Int] and
    (__ \ "activities").format[List[JsonActivityFormat]]
  )(JsonVoicebankActivityFormat.apply, unlift(JsonVoicebankActivityFormat.unapply))
  
implicit lazy val jsonRecentActivityFormat: Format[JsonRecentActivityFormat] = (
    (__ \ "id").format[Int] and
    (__ \ "name").format[String] and
    (__ \ "timestamp").format[String]
  )(JsonRecentActivityFormat.apply, unlift(JsonRecentActivityFormat.unapply))
}