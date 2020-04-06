package argus

import argus.conf.Plain
import argus.persistence.Redis
import argus.server.{EntityHandler, Server}

object Main {
  def main(args: Array[String]): Unit = {
    val redis = Redis(Plain.url, Plain.port)
    val handler = new EntityHandler(redis)
    Server.start(handler)

    // Get argus.conf
    // set up argus.server dependencies
    // start argus.server
  }
}
