package argus

import akka.http.scaladsl.server.{HttpApp, Route}
import argus.conf.Consul
import argus.persistence.Redis
import argus.server.{EntityHandler, Server}

object Main extends Logging {
  def main(args: Array[String]): Unit = {
    logger.info("The program has started")
    val redis = Redis(Consul.uri, Consul.port)
    val handler = new EntityHandler(redis)
    val backend = new Server(handler)
    backend.startServer("0.0.0.0",8080)
  }
}

