package edu.umass.cs.plasma.automandebugger.automan.ws

import edu.umass.cs.plasma.automandebugger.automan.AutoManPlugin
import edu.umass.cs.plasma.automandebugger.models.TaskSnapshotJsonProtocol.TasksJson
import edu.umass.cs.plasma.automandebugger.models.Tasks
import io.backchat.hookup._
import org.json4s.jackson.JsonMethods._

import scala.Error

/**
 * Created by bj on 20.07.15.
 */
trait WebSocketUtils {
  val client: HookupServerClient with Object {def receive: PartialFunction[InboundMessage, Unit]} = new HookupServerClient {
    def receive = {
      case Connected ⇒
        AID.isActive = true
        println("client connected")
        send(TasksJson.write(Tasks(AutoManPlugin.mostRecentAutomanState)).toString)
        println("just sent most recent AutoMan state")
      case Disconnected(_) ⇒
        AID.isActive = false
        println("client disconnected")
      case m @ Error(exOpt) ⇒
        AID.isActive = false
        System.err.println("Received an error: " + m)
        exOpt foreach { _.printStackTrace(System.err) }
      case m: TextMessage ⇒
        println(m)
        send(m)
      case m: JsonMessage ⇒
        println("JsonMessage(" + pretty(render(m.content)) + ")")
        send(m)
    }
  }

  object AID{
     var isActive = false
  }
}
