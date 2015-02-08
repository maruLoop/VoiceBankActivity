package domain.count

import domain.Dao._
import play.api.db._
import play.api.Play.current
import domain._
import domain.entity._

object GetActivity {
  def getId(name: String): Int =
    DB.withConnection (implicit connection  => {
      Dao.getVoicebankId(name)
    })
  def getName(id: Int): String =
    DB.withConnection (implicit connection  => {
      getVoicebankName(id)
    })
    
  def getNewcomers: List[Voicebank] =
    DB.withConnection (implicit connection  => {
      getVoicebanksWithLimit("", 1, 10, Sort.UPDATE_TIME, Order.DESC)
    })
    
  def getVoicebanksCount: Int =
    DB.withConnection (implicit connection  => {
      Dao.getVoicebanksCount
    })
  def getFilenameCount(id: Int): Int =
    DB.withConnection (implicit connection  => {
      Dao.getFilenameCount(id)
    })
  def getFilenameCountByName(name: String): Int =
    DB.withConnection (implicit connection  => {
      Dao.getFilenameCountByName(name)
    })
  
  def getRecentActivity: List[RecentActivity] = 
    DB.withConnection (implicit connection  => {
      getRecentActivityWithLimit(10)
    })
  
  def getDetailActivity(id: Int, page: Int, pageSize: Int, sort: ActivitySort, order:Order): List[Activity] = 
    DB.withConnection (implicit connection  => {
      val offset: Int = pageSize * page
      val limit: Int = pageSize
      Dao.getActivityDetails(id, limit, offset, sort, order)
    })
    
  def getDetailActivityByName(name: String, page: Int, pageSize: Int, sort: ActivitySort, order: Order): List[Activity] = 
    DB.withConnection (implicit connection  => {
      val offset: Int = pageSize * page
      val limit: Int = pageSize
      Dao.getActivityDetailsByName(name, limit, offset, sort, order)
    })
  
  def getVoicebanks(name: String, page: Int, pageSize: Int, sort: Sort, order: Order): List[Voicebank] =
    DB.withConnection (implicit connection  => {
      val offset: Int = pageSize * page
      val limit: Int = pageSize
      Dao.getVoicebanksWithLimit(name, limit, offset, sort, order)
    })
    
  def getCount(name: String, filename: String): Int =
    DB.withConnection (implicit connection  => {
      Dao.getCount(name, filename)
    })
}