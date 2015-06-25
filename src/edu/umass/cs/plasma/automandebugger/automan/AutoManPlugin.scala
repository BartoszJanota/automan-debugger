package edu.umass.cs.plasma.automandebugger.automan


import edu.umass.cs.automan.core.logging.TaskSnapshot
import edu.umass.cs.automan.core.{AutomanAdapter, Plugin}

/**
 * Created by bj on 24.06.15.
 */
object AutoManPlugin{
  def plugin = classOf[AutoManPlugin]
}

class AutoManPlugin extends Plugin{

  override def startup(adapter: AutomanAdapter): Unit = {
    println("Hello from BJ Plugin!")
    val snapshots: List[TaskSnapshot[_]] = adapter.state_snapshot()

    println("received " + snapshots.size + " tasks")
    snapshots.foreach{ snapshot =>
      println("task title: " + snapshot.title)
    }
  }

  override def shutdown(): Unit = {
    println("Bye bye from BJ Plugin!")
  }


}
