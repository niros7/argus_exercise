package argus.server

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import argus.persistence.MockDb
import org.scalatest.Matchers
import org.scalatest.funspec.AnyFunSpec

class ServerSpec extends AnyFunSpec with Matchers with ScalatestRouteTest {

  private val uri = "/api/resource"

  private def handler(entity: String) = {
    new EntityHandler(new MockDb(entity))
  }

  private def routes(entity: String) = {
    val mockHandler = handler(entity)
    new Server(mockHandler).routes(mockHandler)
  }

  describe("server routes") {
    it("gets the saved entity") {
      Get(uri) ~> routes("test") ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "test"
      }
    }
  }
}
