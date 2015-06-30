package edu.umass.cs.plasma.automandebugger.idea.exceptions

/**
 * Created by bj on 30.06.15.
 */
case class AutoManConnectionException(message: String) extends Exception {
  override def getMessage: String = message
}
