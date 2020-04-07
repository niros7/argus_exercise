package argus.server

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{HttpApp, Route}
import argus.Logging

import scala.util.{Failure, Success}

class Server(handler: Handler) extends HttpApp with Logging {

  def routes(handler: Handler): Route = {
    pathPrefix("api") {
      path("resource") {
        concat(
          get {
            handler.get() match {
              case Failure(ex) => {
                logger.error(ex.getMessage)
                ex.printStackTrace()
                complete(StatusCodes.InternalServerError)
              }
              case Success(entity) => {
                entity match {
                  case Some(value) => complete(StatusCodes.OK -> value)
                  case None => complete(StatusCodes.NotFound -> "The resource doesn't exist")
                }
              }
            }
          },
          post {
            entity(as[String]) { entity =>
              handler.put(entity) match {
                case Failure(ex) => {
                  logger.error(ex.getMessage)
                  ex.printStackTrace()
                  complete(StatusCodes.InternalServerError)
                }
                case Success(value) =>
                  if (value) {
                    logger.info("Successfully created to resource")
                    complete(StatusCodes.Created)
                  } else {
                    complete(StatusCodes.InternalServerError)
                  }
              }
            }
          }
        )
      }
    }
  }

  override protected def routes: Route = {
    routes(handler)
  }

  override def postHttpBindingFailure(cause: Throwable): Unit = {
    cause.printStackTrace()
  }
}
