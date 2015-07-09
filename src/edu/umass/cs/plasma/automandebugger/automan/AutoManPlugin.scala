package edu.umass.cs.plasma.automandebugger.automan


import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import edu.umass.cs.automan.core.logging.TaskSnapshot
import edu.umass.cs.automan.core.{AutomanAdapter, Plugin}
import edu.umass.cs.plasma.automandebugger.automan.actors.DebugServerActor
import edu.umass.cs.plasma.automandebugger.models.TaskSnapshotResponse
import spray.can.Http

import scala.concurrent.duration._

/**
 * Created by bj on 24.06.15.
 */
object AutoManPlugin{
  def plugin = classOf[AutoManPlugin]
}

class AutoManPlugin extends Plugin{
  implicit val system: ActorSystem = ActorSystem("AutoManDebugger")
  implicit val timeout = akka.util.Timeout(10 seconds)

  override def startup(adapter: AutomanAdapter): Unit = {
    println("Hello from AID Plugin!")

    val debugServerActor = system.actorOf(Props(classOf[DebugServerActor], adapter), "DebugServerActor")

    IO(Http) ? Http.Bind(debugServerActor, interface = "localhost", port = 8888)

  }

  override def shutdown(): Unit = {
    println("Bye bye from AID Plugin!")
  }

  override def state_updates(tasks: List[TaskSnapshot[_]]): Unit = {
    println("AID callback state_update has been called now!\nGot " + tasks.size + " tasks updates:")
    tasks.foreach{t => println(TaskSnapshotResponse.applyFromTaskSnapshot(t).toString)}
    //state_updates tasks need to be emited here to the websocket finally
  }
}
