package argus.server

import argus.persistence.Db

import scala.util.Try

class EntityHandler(db: Db) extends Handler {
  override def put(entity: String): Try[Boolean] = {
    db.put(entity)
  }

  override def get(): Try[Option[String]] = {
    db.get()
  }
}
