package conf

import com.typesafe.config.ConfigFactory

object AppConf {
  val conf = ConfigFactory.load
  val clusterName = conf.getString("cluster.name")

  val localNodeAddress   = conf.getString("node.address")
  val localNodePort = conf.getInt("node.port")
  val nodeAddress = conf.getString("node.address")+":"+conf.getString("node.port")
  val localNodeId = conf.getInt("node.id")

  val totalNodes = conf.getInt("cluster.totalnodes")
  val availability = conf.getDouble("cluster.availability")/100
  val seedNode = conf.getString("cluster.seed.address")
  val listenAddress  = conf.getString("cluster.listen.address").split(",").toList
  val replication = 2
  val localGroupId = (localNodeId % replication) +1

}
