package domain

import java.util.Date
import play.api.libs.json.JsPath
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class JsonVoicebankFormat(id: Int, name: String, timestamp: String)

case class JsonActivityFormat(filename: String, count: Int, timestamp: String)

case class JsonVoicebankActivityFormat(id: Int, name: String, activities: List[JsonActivityFormat])

case class JsonRecentActivityFormat(id: Int, name: String, timestamp: String)

object JsonFormat {  
implicit val jsonVoicebankFormat: OFormat[JsonVoicebankFormat] = (
    (__ \ "id").format[Int] and
    (__ \ "name").format[String] and
    (__ \ "timestamp").format[String]
  )(JsonVoicebankFormat.apply, unlift(JsonVoicebankFormat.unapply))
  
implicit val jsonActivityFormat: OFormat[JsonActivityFormat] = (
    (__ \ "filename").format[String] and
    (__ \ "count").format[Int] and
    (__ \ "timestamp").format[String]
  )(JsonActivityFormat.apply, unlift(JsonActivityFormat.unapply))
  
implicit val jsonVoicebankActivityFormat: OFormat[JsonVoicebankActivityFormat] = (
    (__ \ "id").format[Int] and
    (__ \ "name").format[String] and
    (__ \ "activities").format[List[JsonActivityFormat]]
  )(JsonVoicebankActivityFormat.apply, unlift(JsonVoicebankActivityFormat.unapply))
  
implicit val jsonRecentActivityFormat: OFormat[JsonRecentActivityFormat] = (
    (__ \ "id").format[Int] and
    (__ \ "name").format[String] and
    (__ \ "timestamp").format[String]
  )(JsonRecentActivityFormat.apply, unlift(JsonRecentActivityFormat.unapply))
}