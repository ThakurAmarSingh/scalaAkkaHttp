package app.service

import scala.language.postfixOps
import conf.AppConf._

case class clusterNode(address: String,port : String,id : Int,groupID : Int)

class networkService  {

  def getGroupIdForKey(key : String) : Int =  (key.map( k => k.hashCode).sum % replication ) + 1

  def getClusterNodes : List[clusterNode] = listenAddress.map(x => x.split(":")).map(a => clusterNode(a(0),a(1),a(2).toInt,(a(2).toInt % replication) + 1))

  def getNodesWithGroupId(groupId : Int) : List[clusterNode] = getClusterNodes.filter( node => node.groupID == groupId)

  def updateNewNode = ???

  def clusterStatus = ???

  def exitCluster = ???

  def updateSyncNodes = ???
}
