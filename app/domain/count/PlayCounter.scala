package domain.count

import domain.Dao._
import play.api.db._
import play.api.Play.current

object PlayCounter {
  def  incrementPlayCount(name: String, filename: String) =
    DB.withTransaction (implicit connection  => {
      insertNewVoiceBank(name)
      insertAndUpdateNewFilename(name, filename)
    })
}