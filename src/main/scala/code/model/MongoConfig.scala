package code.model

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._

import net.liftweb.mongodb._
import net.liftweb.util.Props
import com.mongodb.{Mongo, ServerAddress}


/**
 * Created by IntelliJ IDEA.
 * User: jks
 * Date: 12/7/11
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */

object MongoConfig {

case object TheMongoIdentifier extends MongoIdentifier {
  val jndiName = "conn4"
}

             def init: Unit = {
    val srvr = new ServerAddress(
       Props.get("mongo.host", "127.0.0.1"),
       Props.getInt("mongo.port", 30010)
    )
    MongoDB.defineDb(DefaultMongoIdentifier, new Mongo(srvr), "conn4")  //TODO: how to find the app name?
    //MongoDB.defineDb(AdminDb, new Mongo(srvr), "admin")
  }

}