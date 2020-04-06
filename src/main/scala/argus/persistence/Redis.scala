package argus.persistence

import argus.Logging
import com.redis._

import scala.util.{Failure, Success, Try}

class Redis(url: String, port: Int) extends Db with Logging {

  private val key = "latest"
  private lazy val client = {
    try {
      val r = new RedisClient(url, port)
      logger.info(s"Successfully connected to Redis at $url:$port")
      r
    } catch {
      case ex: Exception => {
        logger.error(s"Connection failed to Redis at $url:$port with msg: ")
        ex.printStackTrace()
        // No point in keeping the server running
        throw ex
      }
    }
  }

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
