package edu.umass.cs.plasma.automandebugger.automan

import edu.umass.cs.plasma.automandebugger.automan.ws.WebSocketUtils
import io.backchat.hookup._
import org.json4s._
import org.json4s.jackson.JsonMethods._
/**
 * Created by bj on 20.07.15.
 */
object WSClient extends WebSocketUtils{

  def main(args: Array[String]) {

    val server = HookupServer(8128)(client)

    server onStop {
      println("Server is stopped")

    }
    server onStart {
      println("Server is started")
    }
    server.start

  }
}