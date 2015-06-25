package edu.umass.cs.plasma.automandebugger.idea.utils

/**
 * Created by bj on 25.06.15.
 */
trait httpHelpers {
  def get(url: String) = scala.io.Source.fromURL(url).mkString
}
