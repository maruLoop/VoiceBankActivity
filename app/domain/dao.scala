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
          SELECT
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
  
  def getActivityDetails(implicit connection: Connection): List[Activity] = {
            val res = SQL(
        """
          SELECT filename, count, timestamp FROM activity
          WHERE
            (filename, timestamp) 
          IN(
            SELECT filename, MAX(timestamp) FROM activity
            GROUP BY filename
          )
          ORDER BY count DESC
        """
      ).as( str("filename") ~ int("count") ~ date("timestamp") map(flatten) *)
      res.map(Activity.tupled)
  }

  /**
   * 音源が既に登録済みか確認する。
   * あった時true,なければfalse。
   */
  def isExistVoiceBank(name: String)(implicit connection: Connection): Boolean = {
            SQL(
        """
          SELECT EXISTS(
          SELECT * FROM voicebank WHERE name = {name}
          )
        """
      ).on(
        'name -> name
      ).as(scalar[Int].single) != 0
  }
  
  /**
   * 音源をvoicebankに登録する。
   */
  def insertNewVoiceBank(name: String)(implicit connection: Connection) = {
            SQL(
        """
          INSERT INTO voicebank (name) VALUES ({name})
        """
      ).on(
        'name -> name
      ).executeInsert()
  }
  
  /**
   * 原音ファイル名をactivityに登録する。
   * このとき、countに1がセットされる。
   * 
   * @return Int 1
   */
  def insertNewFilename(name: String, filename: String)(implicit connection: Connection): Int = {
      val id: Int = SQL(
        """
          SELECT id FROM voicebank WHERE
          name = {name}
        """
      ).on(
        'name -> name
      ).as(scalar[Int].single)
      insertNewFilenameWithId(id, filename)
  }
  
  /**
   * 原音ファイル名をactivityに登録する。
   * このとき、countに1がセットされる。
   * 
   * @return Int 1
   */
  def insertNewFilenameWithId(id: Int, filename: String)(implicit connection: Connection): Int = {
            SQL(
        """
          INSERT INTO activity (id, filename) VALUES ({id}, {filename})
        """
      ).on(
        'id -> id,
        'filename -> filename
      ).executeInsert()
      1
  }
  
  /**
   * 原音ファイル名が既にあるか確認する。
   * あった時true,なければfalse。
   */
  def isExistFilename(name: String, filename: String)(implicit connection: Connection): Boolean = {
            SQL(
        """
          SELECT EXISTS(
          SELECT * FROM activity WHERE
          filename = {filename} AND id = ( SELECT id FROM voicebank WHERE name = {name} )
          )
        """
      ).on(
        'name -> name,
        'filename -> filename
      ).as(scalar[Int].single) != 0
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
  
  /**
   * 原音フェイル名の再生数を{count}にupdateする。
   */
  def updateCount(name: String, filename: String, count: Int)(implicit connection: Connection) = {
            SQL(
        """
          UPDATE activity SET count = {count}
          WHERE filename = {filename} AND id = ( SELECT id FROM voicebank WHERE name = {name} )
        """
      ).on(
        'name -> name,
        'filename -> filename,
        'count -> count
      ).executeUpdate()
  }
}