package domain.count

import domain.Dao._
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import anorm.Column._
import java.sql.Connection
import anorm.NamedParameter.symbol

class PlayCounter {
  def  incrementPlayCount(name: String, filename: String): Int =
    DB.withTransaction (implicit connection  => {
      val isExsistVoicebank = SQL(
        """
          SELECT EXISTS(
          SELECT * FROM voicebank WHERE name = {name}
          )
        """
      ).on(
        'name -> name
      ).as(scalar[Int].single) != 0
      
      val isExistFilename = SQL(
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
      
      if(!isExsistVoicebank){
            SQL(
        """
          INSERT INTO voicebank (name) VALUES ({name})
        """
      ).on(
        'name -> name
      ).executeInsert()
      val id: Int = SQL(
        """
          SELECT id FROM voicebank WHERE
          name = {name}
        """
      ).on(
        'name -> name
      ).as(scalar[Int].single)
            SQL(
        """
          INSERT INTO activity (id, filename) VALUES ({id}, {filename})
        """
      ).on(
        'id -> id,
        'filename -> filename
      ).executeInsert()
      1
      }else if(!isExistFilename){
            SQL(
        """
          INSERT INTO voicebank (name) VALUES ({name})
        """
      ).on(
        'name -> name
      ).executeInsert()
      val id: Int = SQL(
        """
          SELECT id FROM voicebank WHERE
          name = {name}
        """
      ).on(
        'name -> name
      ).as(scalar[Int].single)
            SQL(
        """
          INSERT INTO activity (id, filename) VALUES ({id}, {filename})
        """
      ).on(
        'id -> id,
        'filename -> filename
      ).executeInsert()
      1
      }else{
        val count: Int = SQL(
        """
          SELECT count FROM activity WHERE
          filename = {filename} AND id = ( SELECT id FROM voicebank WHERE name = {name} )
        """
      ).on(
        'name -> name,
        'filename -> filename
      ).as(scalar[Int].single)
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
        count + 1
      }
    })
}