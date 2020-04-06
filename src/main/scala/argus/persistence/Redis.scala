package argus.persistence

import com.redis._

import scala.util.Try

class Redis(url: String, port: Int) extends Db {

  private val key = "latest"
  private lazy val client = new RedisClient(url, port)

  override def put(entity: String): Try[Boolean] = {
    Try {
      client.set(key, entity)
    }
  }

  override def get(): Try[Option[String]] = {
    Try {
      client.get(key)
    }
  }
}

object Redis {
  def apply(url: String, port: Int): Redis = {
    new Redis(url, port)
  }
}
