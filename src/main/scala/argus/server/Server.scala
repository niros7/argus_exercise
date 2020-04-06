package argus.server

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.{Http, server}
import akka.stream.ActorMaterializer
import argus.Logging
import akka.http.scaladsl.server.directives.DebuggingDirectives

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.{Failure, Success}

object Server extends Logging {

  private def routes(handler: Handler): server.Route = {
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

  def start(handler: Handler) {
    implicit val system: ActorSystem = ActorSystem("exercise")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    val r = routes(handler)
    val clientRouteLogged = DebuggingDirectives.logRequestResult("Client REST", Logging.InfoLevel)(r)

    val bindingFuture = Http().bindAndHandle(clientRouteLogged, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
