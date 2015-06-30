package edu.umass.cs.plasma.automandebugger.idea.utils

import akka.actor.ActorSystem
import edu.umass.cs.plasma.automandebugger.idea.exceptions.AutoManConnectionException
import spray.client.pipelining._
import spray.http.StatusCodes

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by bj on 25.06.15.
 */
trait httpHelpers {
  def get(url: String): Either[Exception,String] =
    try {
      Right(scala.io.Source.fromURL(url).mkString)
    } catch {
      case e: Exception => Left(new AutoManConnectionException("Cannot get an update of AutoMan program state, check if your AutoMan program is responding now."))
    }

  def getSpray(url: String)(implicit system: ActorSystem): Future[String] = {
    import system.dispatcher

    val pipeline = sendReceive
    val r = pipeline (Get("http://some.url/"))
    r.map(_.entity.asString)
  }
}
