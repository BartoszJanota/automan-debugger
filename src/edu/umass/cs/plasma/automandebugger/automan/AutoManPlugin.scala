package edu.umass.cs.plasma.automandebugger.automan


import akka.actor.{Props, ActorSystem}
import akka.io.IO
import edu.umass.cs.automan.core.info.StateInfo
import edu.umass.cs.automan.core.logging.TaskSnapshot
import edu.umass.cs.automan.core.{AutomanAdapter, Plugin}
import edu.umass.cs.plasma.automandebugger.automan.actors.DebugServerActor
import spray.can.Http
import scala.concurrent.duration._
import akka.pattern.ask

/**
 * Created by bj on 24.06.15.
 */
object AutoManPlugin{
  def plugin = classOf[AutoManPlugin]
}

class AutoManPlugin extends Plugin{
  implicit var system: ActorSystem = ActorSystem("AutoManDebugger")
  implicit val timeout = akka.util.Timeout(10 seconds)

  override def startup(adapter: AutomanAdapter): Unit = {
    println("Hello from BJ Plugin!")
    //val snapshots: List[TaskSnapshot[_]] = adapter.state_snapshot()

    //println("received " + snapshots.size + " tasks")
    //snapshots.foreach{ snapshot =>
    //  println("task title: " + snapshot.title)
    //}

    val debugServerActor = system.actorOf(Props(classOf[DebugServerActor], adapter), "DebugServerActor")

    IO(Http) ? Http.Bind(debugServerActor, interface = "localhost", port = 8888)

  }

  override def shutdown(): Unit = {
    println("Bye bye from BJ Plugin!")
  }


}
