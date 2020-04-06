package argus.persistence

import scala.util.Try

trait Db {
  def put(entity: String): Try[Boolean]

  def get(): Try[Option[String]]
}
