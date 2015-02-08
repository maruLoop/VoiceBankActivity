package domain

import play.api.db._
import anorm._
import anorm.SqlParser._
import anorm.Column._
import java.sql.Connection
import anorm.NamedParameter.symbol
import domain.entity._

object Dao {
  
//  def getVoicebanks(implicit connection: Connection):List[Voicebank] = {
//            val res = SQL(
//        """
//          SELECT * FROM voicebank ORDER BY id {order}
//        """
//      ).as(
//          int("id") ~ str("name") ~ date("timestamp") map(flatten) *)
//      res.map(Voicebank.tupled)
//  }
  
  private def convertSortToTableCulmun(sort: Sort): String = {
    sort match{
      case Sort.REGIST_TIME => "voicebank.timestamp"
      case Sort.UPDATE_TIME => "activity.timestamp"
    }
  }
  
  def getVoicebankId(name: String)(implicit connection: Connection): Int = {
        SQL(
        """
          SELECT id FROM voicebank WHERE name = {name}
        """
      ).on(
        'name -> name
      ).as( scalar[Int].single )
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
  
  def getVoicebanksCount(implicit connection: Connection): Int = {
        SQL(
        """
          SELECT COUNT(id) FROM voicebank
        """
      ).as( scalar[Int].single )
  }
  
  def getFilenameCount(id: Int)(implicit connection: Connection): Int = {
        SQL(
        """
          SELECT COUNT(filename) FROM activity
          WHERE id = {id}
        """
      ).on(
          'id -> id
      ).as( scalar[Int].single )
  }
  
  def getFilenameCountByName(name: String)(implicit connection: Connection): Int = {
        SQL(
        """
          SELECT COUNT(filename) FROM activity
          WHERE id = (SELECT id FROM voicebank WHERE name = {name})
        """
      ).on(
          'name -> name
      ).as( scalar[Int].single )
  }
  
  def getVoicebanksWithLimit(name: String, limit: Int, offset: Int, sort: Sort, order: Order)(implicit connection: Connection):List[Voicebank] = {
    val sortForColmun: String = convertSortToTableCulmun(sort)
            val res = SQL(
        """
          SELECT DISTINCT voicebank.id, voicebank.name, voicebank.timestamp, activity.timestamp
          FROM voicebank
          INNER JOIN activity ON voicebank.id = activity.id 
          WHERE
            (activity.id, activity.timestamp) 
          IN(SELECT activity.id, MAX(activity.timestamp) FROM activity
            GROUP BY activity.id
          )
          ORDER BY %s %s LIMIT {limit} OFFSET {offset}
        """.format(sortForColmun ,order.toString())
      ).on(
        'limit -> limit,
        'offset -> offset
      ).as( int("id") ~ str("name") ~ date("voicebank.timestamp") ~ date("activity.timestamp") map(flatten) *)
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
          IN(SELECT activity.id, MAX(activity.timestamp) FROM activity
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
  
  private def convertActivitySortToTableCulmun(sort: ActivitySort): String = {
    sort match{
      case ActivitySort.PLAY_COUNT => "count"
      case ActivitySort.UPDATE_TIME => "timestamp"
    }
  }
  
  def getActivityDetails(id: Int, limit: Int, offset: Int, sort: ActivitySort, order: Order)(implicit connection: Connection): List[Activity] = {
    val sortForColmun: String = convertActivitySortToTableCulmun(sort)
            val res = SQL(
        """
          SELECT DISTINCT filename, count, timestamp FROM activity
          WHERE
            (filename, timestamp) 
          IN(
            SELECT filename, MAX(timestamp) FROM activity
            WHERE id = {id}
            GROUP BY filename
          )
          AND id = {id}
          ORDER BY %s %s LIMIT {limit} OFFSET {offset}
        """.format(sortForColmun, order.toString())
      ).on(
        'id -> id,
        'limit -> limit,
        'offset -> offset
      ).as( str("filename") ~ int("count") ~ date("timestamp") map(flatten) *)
      res.map(Activity.tupled)
  }
  
  def getActivityDetailsByName(name: String, limit: Int, offset: Int, sort: ActivitySort, order: Order)(implicit connection: Connection): List[Activity] = {
    val sortForColmun: String = convertActivitySortToTableCulmun(sort)
            val res = SQL(
        """
          SELECT DISTINCT filename, count, timestamp FROM activity
          WHERE
            (filename, timestamp) 
          IN(
            SELECT filename, MAX(timestamp) FROM activity
            WHERE id = (SELECT id FROM voicebank WHERE name = {name})
            GROUP BY filename
          )
          AND id = (SELECT id FROM voicebank WHERE name = {name})
          ORDER BY %s %s LIMIT {limit} OFFSET {offset}
        """.format(sortForColmun, order.toString())
      ).on(
        'name -> name,
        'limit -> limit,
        'offset -> offset
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