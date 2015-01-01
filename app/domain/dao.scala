package domain

import play.api.db._
import anorm._
import anorm.SqlParser._
import java.sql.Connection
import anorm.NamedParameter.symbol

object dao {

  /**
   * 音源が既に登録済みか確認する。
   * あった時true,なければfalse。
   */
  def isExistVoiceBank(name: String)(implicit connection: Connection): Boolean = {
            SQL(
        """
          SELECT COUNT(*) FROM voicebanks WHERE
          name = {name}
        """
      ).on(
        'name -> name
      ).as(scalar[Int].single) > 0
  }
  
  /**
   * 音源をvoicebanksに登録する。
   */
  def insertNewVoiceBank(name: String)(implicit connection: Connection) = {
            SQL(
        """
          INSERT INTO voicebanks (name) VALUES ({name})
        """
      ).on(
        'name -> name
      ).executeInsert()
  }
  
  /**
   * 原音ファイル名をlogsに登録する。
   * このとき、countに1がセットされる。
   * 
   * @return Int 1
   */
  def insertNewFilename(name: String, filename: String)(implicit connection: Connection): Int = {
      val id: Int = SQL(
        """
          SELECT id FROM voicebanks WHERE
          name = {name}
        """
      ).on(
        'name -> name
      ).as(scalar[Int].single)
      insertNewFilenameWithId(id, filename)
  }
  
  /**
   * 原音ファイル名をlogsに登録する。
   * このとき、countに1がセットされる。
   * 
   * @return Int 1
   */
  def insertNewFilenameWithId(id: Int, filename: String)(implicit connection: Connection): Int = {
            SQL(
        """
          INSERT INTO logs (id, filename) VALUES ({id}, {filename})
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
          SELECT COUNT(*) FROM logs WHERE
          filename = {filename} AND id = ( SELECT id FROM voicebanks WHERE name = {name} )
        """
      ).on(
        'name -> name,
        'filename -> filename
      ).as(scalar[Int].single) > 0
  }
  
  /**
   * 原音ファイル名の再生数を取得する。
   */
  def getCount(name: String, filename: String)(implicit connection: Connection): Int = {
            SQL(
        """
          SELECT count FROM logs WHERE
          filename = {filename} AND id = ( SELECT id FROM voicebanks WHERE name = {name} )
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
          UPDATE logs SET count = {count}
          WHERE filename = {filename} AND id = ( SELECT id FROM voicebanks WHERE name = {name} )
        """
      ).on(
        'name -> name,
        'filename -> filename,
        'count -> count
      ).executeUpdate()
  }
}