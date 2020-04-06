package argus.conf


import argus.Logging
import com.typesafe.config.ConfigFactory

object Consul extends PersistenceConf with Logging {

  val redisUriKeyPath = "redis/uri"
  val redisPortKeyPath = "redis/port"

  private val client = {
    val consulUri = ConfigFactory.defaultApplication().getString("consulUri")
    try {
      com.orbitz.consul.Consul.builder().withUrl(consulUri).build().keyValueClient()
    } catch {
      case ex: Exception => {
        logger.error(s"Connection failed to Consul at $consulUri with msg: ")
        ex.printStackTrace()
        // No point in keeping the server running
        throw ex
      }
    }
  }

  override def uri: String = {
    val uri = client.getValueAsString(redisUriKeyPath)
      uri.isPresent match {
        case true => uri.get()
        case false => {
          logger.error(s"Failed to get redis uri from Consul at key: $redisUriKeyPath")
          // No point in keeping the server running
          throw new NoSuchElementException
        }
    }
  }

  override def port: Int = {
    val uri = client.getValueAsString(redisPortKeyPath)
    uri.isPresent match {
      case true => uri.get().toInt
      case false => {
        logger.error(s"Failed to get redis uri from Consul at key: $redisUriKeyPath")
        // No point in keeping the server running
        throw new NoSuchElementException
      }
    }
  }
}
