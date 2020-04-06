package argus.persistence

import scala.util.{Success, Try}

class MockDb(entity: String) extends Db {

  override def put(entity: String): Try[Boolean] = {
    Success(true)
  }

  override def get(): Try[Option[String]] = {
    Success(Some(entity))
  }
}
