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


}
