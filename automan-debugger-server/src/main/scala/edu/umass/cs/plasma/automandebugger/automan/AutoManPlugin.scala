package edu.umass.cs.plasma.automandebugger.automan

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import edu.umass.cs.automan.core.logging.TaskSnapshot
import edu.umass.cs.automan.core.{AutomanAdapter, Plugin}
import edu.umass.cs.plasma.automandebugger.automan.WSClient._
import edu.umass.cs.plasma.automandebugger.automan.actors.DebugServerActor
import edu.umass.cs.plasma.automandebugger.automan.ws.WebSocketUtils
import edu.umass.cs.plasma.automandebugger.models.{TaskSnapshotJsonProtocol, Tasks, TaskSnapshotResponse}
import io.backchat.hookup.HookupServer
import spray.can.Http


import scala.concurrent.duration._

/**
 * Created by bj on 24.06.15.
 */
object AutoManPlugin{
  def plugin = classOf[AutoManPlugin]
  var mostRecentAutomanState: List[TaskSnapshotResponse] = List.empty
}

class AutoManPlugin extends Plugin with WebSocketUtils {
  implicit val system: ActorSystem = ActorSystem("AutoManDebugger")
  implicit val timeout = akka.util.Timeout(10 seconds)

  val server = HookupServer(8128)(client)

  import TaskSnapshotJsonProtocol._

  override def startup(adapter: AutomanAdapter): Unit = {
    println("Hello from AID Plugin!")
    val debugServerActor = system.actorOf(Props(classOf[DebugServerActor], adapter), "DebugServerActor")
    IO(Http) ? Http.Bind(debugServerActor, interface = "localhost", port = 8888)

    server onStop {
      println("Server is stopped")
    }
    server onStart {
      println("Server is started")
    }
    server.start

  }

  override def shutdown(): Unit = {
    println("Bye bye from AID Plugin!")
  }

  override def state_updates(tasks: List[TaskSnapshot[_]]): Unit = {
    println("AID callback state_update has been called now!\nGot " + tasks.size + " tasks updates:")
    //tasks.foreach{t => println(TaskSnapshotResponse.applyFromTaskSnapshot(t).toString)}
    AutoManPlugin.mostRecentAutomanState = tasks.map{ taskSnapshot =>
      TaskSnapshotResponse.applyFromTaskSnapshot(taskSnapshot)
    }
    
    if (AID.isActive){
      println("Aid is active now, sending message!")
      client.send(TasksJson.write(Tasks(AutoManPlugin.mostRecentAutomanState)).toString)
    } else {
      println("Aid is not active now!")
    }

  }







}
