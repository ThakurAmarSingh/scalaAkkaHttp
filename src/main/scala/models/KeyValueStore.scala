package models

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.language.postfixOps
import app.service.{PeerNodeService, clusterNode, networkService}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import conf.AppConf._

trait apiCall

case class SetKeyValueStore(key : String,value : String,address :String,port: String) extends apiCall
case class GetValue(key : String,address :String,port: String) extends apiCall

object KeyValueStore extends networkService {

  implicit val timeout = Timeout(10 seconds)

  //Actor call to set values
  val peerNodeSystem = ActorSystem("data-store-system")
  val props = Props[PeerNodeService]
  val peerNodeActor = peerNodeSystem.actorOf(props,"simplePeerNode")

  //Local key value store
  var myKeyValue : Map[String,String] = Map()

  val myNetwork = new networkService

  def updateKeyAndValue(key:String,value:String) : String = {
    println(s"got a request to update key value --- $key and $value")
    myKeyValue += (key -> value)
    "Ok"
  }

  def getValueForKey(key : String) : Option[String] = {

    val keyGroup = myNetwork.getGroupIdForKey(key)

    if (keyGroup == localGroupId) Some(myKeyValue(key))
    else {
        val node : clusterNode = getNodesWithGroupId(keyGroup).head
      val futureRes = peerNodeActor ? GetValue(key,node.address,node.port)

      Await.result(futureRes,timeout.duration).asInstanceOf[String] match {
        case x : String => {
          println(" Info ::: get response " + x)
          Some(x)
        }
        case _ => None
      }
    }
  }

  def setValueForKey(key:String,value:String) : String =  {
     val keyGroup = myNetwork.getGroupIdForKey(key)
      for(clusterNode <- myNetwork.getClusterNodes){
        if (clusterNode.groupID == keyGroup)
          if(clusterNode.id == localNodeId) {
            updateKeyAndValue(key,value)
          }
          else   {
            println(s"making a call to update the key value pair  ::: $clusterNode with $keyGroup")
            peerNodeActor ! SetKeyValueStore(key,value,clusterNode.address,clusterNode.port)
          }
      }
    "Key Updated"
  }

  def cSetValueForKey(key:String,value:String) : String =  updateKeyAndValue(key,value)

}




