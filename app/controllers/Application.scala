package controllers

import play.api._
import play.api.data.Forms._
import play.api.data.Form
import play.api.mvc._
import utils.Validator._
import java.net.URLDecoder
import domain.count.PlayCounter
import java.nio.charset.StandardCharsets

object Application extends Application

class Application extends Controller{

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
          val playcounter: PlayCounter = new PlayCounter()
          val count: Int = playcounter.incrementPlayCount(nameUtf8, filenameUtf8)
          Ok("%s %s %d".format(nameUtf8, filenameUtf8, count))
        })
  }

}