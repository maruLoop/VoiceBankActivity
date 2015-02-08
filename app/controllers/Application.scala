package controllers

import play.api._
import play.api.data.Forms._
import play.api.data.Form
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.libs.functional.syntax._
import play.api.mvc._
import utils.Validator._
import java.net.URLDecoder
import domain._
import domain.entity._
import domain.JsonFormat._
import domain.count.PlayCounter
import java.nio.charset.StandardCharsets
import domain.count.GetActivity
import java.util.Date
import java.util.TimeZone
import java.text.DateFormat
import java.text.SimpleDateFormat

object Application extends Application

class Application extends Controller{
  val dfString: String = "yyyy-MM-dd HH:mm:ss"
  val df: DateFormat = new SimpleDateFormat(dfString)
    
  /**
   * show HOME page
   */
  def index = Action {
    Ok(views.html.index("VoiceBank Activity", "HOME", "CHECK the ACTIVITIES"))
  }
  
  /**
   * show ABOUT page
   */
  def about = Action {
    Ok(views.html.about("VoiceBank Activity", "ABOUT", "CHECK the ACTIVITIES"))
  }
  
  /**
   * show VOICE BANKS page
   */
  def voicebanks(page: Int) = Action {
    val voicebanks: Voicebanks = Voicebanks(GetActivity.getVoicebanksCount, GetActivity.getVoicebanks("", 0, 20, Sort.UPDATE_TIME, Order.DESC))
    val json: String = Json.stringify(Json.toJson(
        JsonVoicebanksFormat(page, voicebanks.voicebanksCount, voicebanks.voicebanks.map{vb => JsonVoicebankFormat(vb.id, vb.name, df.format(vb.registTime), df.format(vb.updateTime))})
    ))
    Ok(views.html.voicebanks("VoiceBank Activity", "VOICE BANKS", "CHECK the ACTIVITIES", page, json))
  }
    
  def voicebank(id: Int, page: Int) = Action {
    Ok(views.html.voicebank("VoiceBank Activity", "VOICE BANK", id, page))
  }
  
  def getVoicebanks(name: String, page: Int, pageSize: Int, sortCode: String, orderCode: String) = Action {
    val sort: Sort = Sort.resolve(sortCode)
    val order: Order = Order.resolve(orderCode)
    val voicebanks: Voicebanks = Voicebanks(GetActivity.getVoicebanksCount, GetActivity.getVoicebanks(name,page,pageSize,sort,order))

    Ok(Json.toJson(
        JsonVoicebanksFormat(page, voicebanks.voicebanksCount, voicebanks.voicebanks.map{vb => JsonVoicebankFormat(vb.id, vb.name, df.format(vb.registTime), df.format(vb.updateTime))})
    ))
  }
  
  def getNewcomers = Action {
    val voicebanks: List[Voicebank] = GetActivity.getVoicebanks("", 0, 10, Sort.REGIST_TIME, Order.DESC)
    Ok(Json.toJson(
        voicebanks.map{vb => JsonVoicebankFormat(vb.id, vb.name, df.format(vb.registTime), df.format(vb.updateTime))}
    ))
  }
  
  def recentActivity = Action {
    val recentActivities: List[RecentActivity] = GetActivity.getRecentActivity
    Ok(Json.toJson(
        recentActivities.map{ac => JsonRecentActivityFormat(ac.id, ac.name, df.format(ac.timestamp))}
    ))
  }
  
  def voicebankJson(id: Int, page: Int, pageSize: Int, sortCode: String, orderCode: String, timeZone: String) = Action {
    val sort: ActivitySort = ActivitySort.resolve(sortCode)
    val order: Order = Order.resolve(orderCode)
    
    val tz: TimeZone = TimeZone.getTimeZone(timeZone)
    val sdf: SimpleDateFormat = new SimpleDateFormat(dfString)
    sdf.setTimeZone(tz)
    
    val activities: List[Activity] = GetActivity.getDetailActivity(id, page, pageSize, sort, order)
    Ok(Json.toJson(
        JsonVoicebankActivityFormat(
            page,
            id,
            GetActivity.getName(id),
            GetActivity.getFilenameCount(id),
            activities.map{ac => JsonActivityFormat(ac.filename, ac.count, sdf.format(ac.timestamp))}
        )
    ))
  }
  def voicebankJsonByName(name: String, page: Int, pageSize: Int, sortCode: String, orderCode: String, timeZone: String) = Action {
    val nameUtf8 = URLDecoder.decode(name,"UTF-8")
    val sort: ActivitySort = ActivitySort.resolve(sortCode)
    val order: Order = Order.resolve(orderCode)
    
    val tz: TimeZone = TimeZone.getTimeZone(timeZone)
    val sdf: SimpleDateFormat = new SimpleDateFormat(dfString)
    sdf.setTimeZone(tz)
    
    val activities: List[Activity] = GetActivity.getDetailActivityByName(nameUtf8, page, pageSize, sort, order)
    Ok(Json.toJson(
        JsonVoicebankActivityFormat(
            page,
            GetActivity.getId(nameUtf8),
            nameUtf8,
            GetActivity.getFilenameCountByName(nameUtf8),
            activities.map{ac => JsonActivityFormat(ac.filename, ac.count, sdf.format(ac.timestamp))}
        )
    ))
  }
  
  private val playCountForm = Form(
      tuple(
      "function" -> nonEmptyText.verifying(isValidFunctionGetPcmData(_)),
      "name" -> nonEmptyText,
      "filename" -> nonEmptyText)
      )
      
  def playCount = Action {
    implicit request =>
    playCountForm.bindFromRequest.fold(
        formWithErrors => BadRequest("Invalid Params"),
        form => {
          val (function, name, filename) = form
          val nameUtf8 = URLDecoder.decode(name,"UTF-8")
          val filenameUtf8 = URLDecoder.decode(filename,"UTF-8")
          try{
            PlayCounter.incrementPlayCount(nameUtf8, filenameUtf8)
            Ok("%s %s %d".format(nameUtf8, filenameUtf8, GetActivity.getCount(nameUtf8, filenameUtf8)))
          }catch{
            // TODO エラーハンドリングちゃんとやる
            case e: Throwable => InternalServerError("Internal Server Error")
          }
        })
  }

}