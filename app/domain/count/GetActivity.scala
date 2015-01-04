package domain.count

import domain.Dao._
import play.api.db._
import play.api.Play.current
import domain._

object GetActivity {
  def getName(id: Int): String =
    DB.withConnection (implicit connection  => {
      getVoicebankName(id)
    })
    
  def getNewcomers: List[Voicebank] =
    DB.withConnection (implicit connection  => {
      getVoicebanksWithLimit(10)
    })
  
  def getRecentActivity: List[RecentActivity] = 
    DB.withConnection (implicit connection  => {
      getRecentActivityWithLimit(10)
    })
  
  def getDetailActivity(id: Int): List[Activity] = 
    DB.withConnection (implicit connection  => {
      getActivityDetails(id)
    })
  
  def getAllVoicebanks: List[Voicebank] =
    DB.withConnection (implicit connection  => {
      getVoicebanks
    })
}