package domain

import play.api.db._
import anorm._
import anorm.SqlParser._
import anorm.Column._
import java.sql.Connection
import anorm.NamedParameter.symbol

object Dao {
  
  def getVoicebanks(implicit connection: Connection):List[Voicebank] = {
            val res = SQL(
        """
          SELECT * FROM voicebank ORDER BY id DESC
        """
      ).as( int("id") ~ str("name") ~ date("timestamp") map(flatten) *)
      res.map(Voicebank.tupled)
  }
  
  def getVoicebankName(id: Int)(implicit connection: Connection): String = {
        SQL(
        """
          SELECT name FROM voicebank WHERE id = {id}
        """
      ).on(
        'id -> id
      ).as( scalar[String].single )
  }
  
  def getVoicebanksWithLimit(limit: Int)(implicit connection: Connection):List[Voicebank] = {
            val res = SQL(
        """
          SELECT * FROM voicebank ORDER BY id DESC LIMIT {limit}
        """
      ).on(
        'limit -> limit
      ).as( int("id") ~ str("name") ~ date("timestamp") map(flatten) *)
      res.map(Voicebank.tupled)
  }
  
  def getRecentActivityWithLimit(limit: Int)(implicit connection: Connection): List[RecentActivity] = {
            val res = SQL(
        """
          SELECT DISTINCT
            activity.id, name, activity.timestamp
          FROM
            activity
          INNER JOIN voicebank ON voicebank.id = activity.id 
          WHERE
            (activity.id, activity.timestamp) 
          IN(
            SELECT activity.id, MAX(activity.timestamp) FROM activity
            GROUP BY activity.id
          )
          ORDER BY activity.timestamp DESC 
          LIMIT {limit}
        """
      ).on(
        'limit -> limit
      ).as(int("id") ~ str("name") ~ date("timestamp") map(flatten) *)
      res.map(RecentActivity.tupled)
  }
  
  def getActivityDetails(id: Int)(implicit connection: Connection): List[Activity] = {
            val res = SQL(
        """
          SELECT DISTINCT filename, count, timestamp FROM activity
          WHERE
            (filename, timestamp) 
          IN(
            SELECT filename, MAX(timestamp) FROM activity
            GROUP BY filename
          )
          AND id = {id}
          ORDER BY count DESC
        """
      ).on(
        'id -> id
      ).as( str("filename") ~ int("count") ~ date("timestamp") map(flatten) *)
      res.map(Activity.tupled)
  }
  /**
   * 音源をvoicebankに登録する。
   */
  def insertNewVoiceBank(name: String)(implicit connection: Connection) = {
            SQL(
        """
          INSERT IGNORE INTO voicebank (name) VALUES ({name})
        """
      ).on(
        'name -> name
      ).executeInsert()
  }
  
  def insertAndUpdateNewFilename(name: String, filename: String)(implicit connection: Connection) = {
            SQL(
        """
          INSERT INTO
    activity (
        id,
        filename
    ) VALUES (
        (SELECT id FROM voicebank WHERE name = {name}),
        {filename}
    ) ON DUPLICATE KEY UPDATE
        count = count + 1
        """
      ).on(
        'name -> name,
        'filename -> filename
      ).executeInsert()
  }
  
  /**
   * 原音ファイル名の再生数を取得する。
   */
  def getCount(name: String, filename: String)(implicit connection: Connection): Int = {
            SQL(
        """
          SELECT count FROM activity WHERE
          filename = {filename} AND id = ( SELECT id FROM voicebank WHERE name = {name} )
        """
      ).on(
        'name -> name,
        'filename -> filename
      ).as(scalar[Int].single)
  }
}