package edu.umass.cs.plasma.automandebugger.automan.actors

import akka.actor.{Actor, ActorRefFactory}
import edu.umass.cs.automan.core.AutomanAdapter
//import org.slf4j.Logger
import spray.json._
import spray.routing._
import spray.routing.RejectionHandler.Default
import spray.util.LoggingContext

/**
 * Created by bj on 25.06.15.
 */
class DebugServerActor(adapter: AutomanAdapter) extends Actor with HttpService with TaskSnapshotJsonProtocol {
  override implicit def actorRefFactory: ActorRefFactory = context

  println("AutoManDebugger Server has started....")

  implicit val eh: ExceptionHandler = ExceptionHandler.default
  implicit val settings: RoutingSettings = RoutingSettings.default(actorRefFactory)

  override def receive: Receive = runRoute(routes)

 // Logger logger = new LoggingContext {}

  val routes: Route = path("state") {
    pathEndOrSingleSlash {
      get {
        complete {
          getAdapterState().toJson.toString()
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
