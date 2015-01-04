package domain.count

import domain.Dao._
import play.api.db._
import play.api.Play.current

class PlayCounter {
  def  incrementPlayCount(name: String, filename: String): Int =
    DB.withTransaction (implicit connection  => {
      if(!isExistVoiceBank(name)){
        insertNewVoiceBank(name)
        insertNewFilename(name, filename)
      }else if(!isExistFilename(name, filename)){
        insertNewFilename(name, filename)
      }else{
        val count: Int = getCount(name, filename)
        updateCount(name, filename, count+1)
        count + 1
      }
    })
}