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
import domain.JsonFormat._
import domain.count.PlayCounter
import java.nio.charset.StandardCharsets
import domain.count.GetActivity
import java.util.Date
import java.text.DateFormat
import java.text.SimpleDateFormat

object Application extends Application

class Application extends Controller{

    val df: DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    
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
  def voicebanks = Action {
    Ok(views.html.voicebanks("VoiceBank Activity", "VOICE BANKS", "CHECK the ACTIVITIES"))
  }
    
  def voicebank(id: Int) = Action {
    Ok(views.html.voicebank("VoiceBank Activity", "VOICE BANK", id))
  }
  
  def getAllVoicebanks = Action {
    val getActivity: GetActivity = new GetActivity
    val voicebanks: List[Voicebank] = getActivity.getAllVoicebanks
    Ok(Json.toJson(
        voicebanks.map{vb => JsonVoicebankFormat(vb.id, vb.name, df.format(vb.timestamp))}
    ))
  }
  
  def getNewcomers = Action {
    val getActivity: GetActivity = new GetActivity
    val voicebanks: List[Voicebank] = getActivity.getNewcomers
    Ok(Json.toJson(
        voicebanks.map{vb => JsonVoicebankFormat(vb.id, vb.name, df.format(vb.timestamp))}
    ))
  }
  
  def recentActivity = Action {
    val getActivity: GetActivity = new GetActivity
    val recentActivities: List[RecentActivity] = getActivity.getRecentActivity
    Ok(Json.toJson(
        recentActivities.map{ac => JsonRecentActivityFormat(ac.id, ac.name, df.format(ac.timestamp))}
    ))
  }
  
  def voicebankJson(id: Int) = Action {
    val getActivity: GetActivity = new GetActivity
    val activities: List[Activity] = getActivity.getDetailActivity(id)
    Ok(Json.toJson(
        JsonVoicebankActivityFormat(
            id,
            getActivity.getName(id),
            activities.map{ac => JsonActivityFormat(ac.filename, ac.count, df.format(ac.timestamp))}
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
            val playcounter: PlayCounter = new PlayCounter()
            val count: Int = playcounter.incrementPlayCount(nameUtf8, filenameUtf8)
            Ok("%s %s %d".format(nameUtf8, filenameUtf8, count))
          }catch{
            // TODO エラーハンドリングちゃんとやる
            case e: Throwable => InternalServerError("Internal Server Error")
          }
        })
  }

}