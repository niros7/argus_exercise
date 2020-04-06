package argus.server

import argus.persistence.MockDb
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import akka.util.ByteString

class ServerSpec extends AnyFunSpec with Matchers with ScalatestRouteTest {

  private val uri = "/api/resource"

  private def handler(entity: String) = {
    new EntityHandler(new MockDb(entity))
  }

  describe("server routes") {
    it("gets the saved entity") {
      Get(uri) ~> Server.routes(handler("test")) ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "test"
      }
    }
  }
}
