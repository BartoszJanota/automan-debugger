package edu.umass.cs.plasma.automandebugger.automan.actors

import akka.actor.{Actor, ActorRefFactory}
import edu.umass.cs.automan.core.AutomanAdapter
import edu.umass.cs.plasma.automandebugger.models.{TaskSnapshotResponse, Tasks}

//import org.slf4j.Logger
import spray.routing.RejectionHandler.Default
import spray.routing._

/**
 * Created by bj on 25.06.15.
 */
class DebugServerActor(adapter: AutomanAdapter) extends Actor with HttpService {
  override implicit def actorRefFactory: ActorRefFactory = context

  println("AutoManDebugger Server has started....")

  implicit val eh: ExceptionHandler = ExceptionHandler.default
  implicit val settings: RoutingSettings = RoutingSettings.default(actorRefFactory)

  override def receive: Receive = runRoute(routes)

  val routes: Route = path("state") {
    pathEndOrSingleSlash {
      get {
        complete {
          getAdapterState().toString
        }
      }
    }
  }

  def getAdapterState(): Tasks = {
    val listOfTaskSnapshots: List[TaskSnapshotResponse] = adapter.state_snapshot().map{ taskSnapshot =>
      TaskSnapshotResponse(taskSnapshot.title, taskSnapshot.text)
    }
    Tasks(listOfTaskSnapshots)
  }
}
