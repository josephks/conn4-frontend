package code.lib

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._

import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.mongodb._
import java.util.Date
import code.lib._
import Helpers._
import  net.liftweb.json.JsonAST._
                                    import com.mongodb._
import net.liftweb.json.JsonParser


case object TheMongoIdentifier extends MongoIdentifier {
  val jndiName = "test"
}

/**
 * Created by IntelliJ IDEA.
 * User: jks
 * Date: 12/7/11
 * Time: 4:49 PM
 * <p>Gets hint data from the mongo database
 */
object HintGetter {



  def getHintJson (key : String) : Box[LiftResponse] = {

    //val docOpt =
    MongoDB.use(DefaultMongoIdentifier) { db =>
      val lines = key.split("\n")
      val cols = lines(0).length()
      val coll = db.getCollection("conn4boards" + cols + "x" + lines.length)

      def getAnsDoc(boardDoc: JValue, reverse: Boolean) = {
        var bestresults = boardDoc \ "bestresults"
        //col is base 1
        def getOppositeCol(col: Int) = {
          cols - (col - 1);
        }
        if (reverse)
          Full(   JsonResponse ( new JObject( bestresults.children.map( { jv =>
            val jf = jv.asInstanceOf[JField] ;
            new JField( getOppositeCol(jf.name.toInt).toString, jf.value)    }
          )    ))   )
        else
          Full(   JsonResponse ( bestresults)   )
      }    //  def getAnsDoc

      val cur = {
        val query = new BasicDBObject("_id", key)
        coll.find(query)
      }
      if (cur.hasNext)
        getAnsDoc(JsonParser.parse(cur.next().toString )   , false)
      else{    //didn't find,look for the mirror image
        val revkey = lines.map( s => s.trim().reverse + "\n").mkString
        val query = new BasicDBObject("_id", revkey)
        val cur2 = coll.find(query)
        if (cur2.hasNext)
                getAnsDoc(JsonParser.parse(cur2.next().toString )   , true)
        else
             None
      }
    }
    //docOpt
    /*match  {
      case None =>  Full(   JsonResponse (  JNull  ))
      case doc =>
        //val boardDoc =
        getAnsDoc(JsonParser.parse(doc.toString))
    }        */

    //Full(   JsonResponse (  JNull  )) //placeholder
     /*
val buffered = balanceChart.createBufferedImage(width,height)
val chartImage = ChartUtilities.encodeAsPNG(buffered)
// InMemoryResponse is a subclass of LiftResponse
// it takes an Array of Bytes, a List[(String,String)] of
// headers, a List[Cookie] of Cookies, and an integer
// return code (here 200 for HTTP 200: OK)
Full(InMemoryResponse(chartImage,
("Content-Type" -> "application/json") :: Nil,
Nil,
200))     */
  }


}
          /*
                        mongos> db.conn4boards2x2.find()
{ "_id" : "state", "best" : 0, "gen" : 4 }
{ "_id" : "BB\nRR\n", "board" : [ [ "RED", "BLACK" ], [ "RED", "BLACK" ] ], "wins" : "tie", "numcheckers" : 4 }
{ "_id" : "BR\nBR\n", "board" : [ [ "BLACK", "BLACK" ], [ "RED", "RED" ] ], "wins" : "tie", "numcheckers" : 4 }
{ "_id" : "BR\nRB\n", "board" : [ [ "RED", "BLACK" ], [ "BLACK", "RED" ] ], "wins" : "tie", "numcheckers" : 4 }
{ "_id" : "RB\nBR\n", "board" : [ [ "BLACK", "RED" ], [ "RED", "BLACK" ] ], "wins" : "tie", "numcheckers" : 4 }
{ "_id" : "RB\nRB\n", "board" : [ [ "RED", "RED" ], [ "BLACK", "BLACK" ] ], "wins" : "tie", "numcheckers" : 4 }
{ "_id" : " B\nRR\n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie" }, "board" : [ [ "RED" ], [ "RED", "BLACK" ] ], "numcheckers" : 3 }
{ "_id" : "  \n  \n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie", "2" : "tie" }, "board" : [ [ ], [ ] ], "numcheckers" : 0 }
{ "_id" : " R\nBR\n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie" }, "board" : [ [ "BLACK" ], [ "RED", "RED" ] ], "numcheckers" : 3 }
{ "_id" : " R\nRB\n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie" }, "board" : [ [ "RED" ], [ "BLACK", "RED" ] ], "numcheckers" : 3 }
{ "_id" : "B \nRR\n", "bestmove" : 2, "bestresult" : "tie", "bestresults" : { "2" : "tie" }, "board" : [ [ "RED", "BLACK" ], [ "RED" ] ], "numcheckers" : 3 }
{ "_id" : "R \nBR\n", "bestmove" : 2, "bestresult" : "tie", "bestresults" : { "2" : "tie" }, "board" : [ [ "BLACK", "RED" ], [ "RED" ] ], "numcheckers" : 3 }
{ "_id" : "R \nRB\n", "bestmove" : 2, "bestresult" : "tie", "bestresults" : { "2" : "tie" }, "board" : [ [ "RED", "RED" ], [ "BLACK" ] ], "numcheckers" : 3 }
{ "_id" : "  \nBR\n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie", "2" : "tie" }, "board" : [ [ "BLACK" ], [ "RED" ] ], "numcheckers" : 2 }
{ "_id" : "  \nRB\n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie", "2" : "tie" }, "board" : [ [ "RED" ], [ "BLACK" ] ], "numcheckers" : 2 }
{ "_id" : " B\n R\n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie" }, "board" : [ [ ], [ "RED", "BLACK" ] ], "numcheckers" : 2 }
{ "_id" : "B \nR \n", "bestmove" : 2, "bestresult" : "tie", "bestresults" : { "2" : "tie" }, "board" : [ [ "RED", "BLACK" ], [ ] ], "numcheckers" : 2 }
{ "_id" : "  \n R\n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie", "2" : "tie" }, "board" : [ [ ], [ "RED" ] ], "numcheckers" : 1 }
{ "_id" : "  \nR \n", "bestmove" : 1, "bestresult" : "tie", "bestresults" : { "1" : "tie", "2" : "tie" }, "board" : [ [ "RED" ], [ ] ], "numcheckers" : 1 }
              */