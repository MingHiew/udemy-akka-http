package part3_highlevelserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object HighLevelIntro extends App {
  implicit val system = ActorSystem("HighLevelIntro")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  // directives
  import akka.http.scaladsl.server.Directives._

  val simpleRoute: Route =
    path("home") { // DIRECTIVE
      complete(StatusCodes.OK) // DIRECTIVE
    }

  val pathGetRoute: Route =
    path("home") {
      get {
        complete(StatusCodes.OK)
      }
    }

  val chainedRoute: Route =
    path("myEndPoint"){
      get {
        complete(StatusCodes.OK)

      } /** VERY IMPORTANT!!! **/ ~
      post {
        complete(StatusCodes.Forbidden)
      }
    } ~
    path("home"){
      complete(
        HttpEntity(
           ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            | <body>
            |   Hello from the high level Akka HTTP!
            | </body>
            |</html>
          """.stripMargin
        )
      ) // Routing tree
    }

  Http().bindAndHandle(pathGetRoute,"localhost",8085)
}
