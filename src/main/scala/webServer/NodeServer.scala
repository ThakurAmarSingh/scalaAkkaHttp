package webServer

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn

import models._
import conf.AppConf._
object NodeServer extends App {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route =
      get {
          pathPrefix("get" / Segment) { key =>
            KeyValueStore.getValueForKey(key) match {
              case Some(value) => complete(s"$value")
              case None => complete(s"No key with found with $key.")
            }
          }
      } ~ get {
        pathPrefix("get/status") {
            complete("alive")
        }
      } ~ post {
        pathPrefix("set" / Segment){ key =>
        {
              entity(as[String]){ value => complete(KeyValueStore.setValueForKey(key,value))
            }
          }
        }
      } ~ post {
        pathPrefix("cset" / Segment){ key =>
            {
              entity(as[String]){ value => complete(KeyValueStore.cSetValueForKey(key,value))
            }
          }
        }
      }

  val bindingFuture = akka.http.scaladsl.Http().bindAndHandle(route,localNodeAddress,localNodePort)

    println(s"Server online at http://$localNodeAddress/$localNodePort/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
}