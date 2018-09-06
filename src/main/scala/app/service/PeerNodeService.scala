package app.service

import akka.actor.Actor
import akka.event.Logging

import models._

class PeerNodeService extends Actor {

  val log = Logging(context.system, this)

  def receive = {

    case SetKeyValueStore(setKey,setValue,address,port) => {
      //API Calls to the known server
      val result = scalaj.http.Http(s"http://$address:$port/cset/$setKey").postData(s"""$setValue""")
        .header("Content-Type", "application/json")
        .header("Charset", "UTF-8")
        .option(scalaj.http.HttpOptions.readTimeout(10000)).asString

      log.info("Got the Post results " + result.body)
      log.info("Made the call to update with key " + setKey + "set Value " + setValue)
    }

    case GetValue(getKey,address,port) => {
      val result = scalaj.http.Http(s"http://$address:$port/get/$getKey")
        .header("Content-Type", "application/json")
        .header("Charset", "UTF-8")
        .option(scalaj.http.HttpOptions.readTimeout(10000)).asString

      log.info("Got the Get results " + result.body)
      log.info("Made the call to get the value for key : " + getKey )

      sender ! result.body
    }

    case _ => log.info("Got incorrect request...")
  }

  def syncWithPeerNode = {

  }
}
