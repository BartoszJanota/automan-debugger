package edu.umass.cs.plasma.automandebugger.idea.utils

import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http.StatusCodes

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by bj on 25.06.15.
 */
trait httpHelpers {
  def get(url: String) = scala.io.Source.fromURL(url).mkString


  def getSpray(url: String)(implicit system: ActorSystem): Future[String] = {
    import system.dispatcher

    val pipeline = sendReceive
    val r = pipeline (Get("http://some.url/"))
    r.map(_.entity.asString)
  }
}
