package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("VoiceBank Activity", "HOME", "CHECK the ACTIVITIES"))
  }
  def about = Action {
    Ok(views.html.about("VoiceBank Activity", "ABOUT", "CHECK the ACTIVITIES"))
  }
  def voicebanks = Action {
    Ok(views.html.voicebanks("VoiceBank Activity", "VOICE BANKS", "CHECK the ACTIVITIES"))
  }

}