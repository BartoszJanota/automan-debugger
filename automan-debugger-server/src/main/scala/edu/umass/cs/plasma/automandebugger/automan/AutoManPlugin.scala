package edu.umass.cs.plasma.automandebugger.automan

import akka.actor.ActorSystem
import edu.umass.cs.automan.core.logging.TaskSnapshot
import edu.umass.cs.automan.core.{AutomanAdapter, Plugin}
import edu.umass.cs.plasma.automandebugger.automan.ws.WebSocketUtils
import edu.umass.cs.plasma.automandebugger.models.{TaskSnapshotJsonProtocol, TaskSnapshotResponse, Tasks}
import io.backchat.hookup.HookupServer

import scala.concurrent.duration._

/**
 * Created by bj on 24.06.15.
 */
object AutoManPlugin {
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

    server onStop {
      println("AID Server has stopped")
    }
    server onStart {
      println("AID Server has started")
    }

    server.start
  }

  override def shutdown(): Unit = {
    println("Bye bye from AID Plugin!")
  }

  override def state_updates(tasks: List[TaskSnapshot[_]]): Unit = {
    //tasks.foreach{t => println(TaskSnapshotResponse.applyFromTaskSnapshot(t).toString)}
    AutoManPlugin.mostRecentAutomanState = tasks.map { taskSnapshot =>
      TaskSnapshotResponse.applyFromTaskSnapshot(taskSnapshot)
    }

    if (AID.isActive) {
      println("AID Server state_update has been called now!\nReceived " + tasks.size + " tasks updates")
      client.send(TasksJson.write(Tasks(AutoManPlugin.mostRecentAutomanState)).toString)
    } else {
      println("AID Server is not active now!")
    }

  }
}
