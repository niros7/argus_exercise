package argus.server

import scala.util.Try

trait Handler {
  def put(entity: String): Try[Boolean]

  def get(): Try[Option[String]]
}