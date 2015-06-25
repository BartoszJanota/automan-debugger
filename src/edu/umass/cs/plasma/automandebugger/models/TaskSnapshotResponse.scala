package edu.umass.cs.plasma.automandebugger.models

import spray.json.DefaultJsonProtocol

//import spray.json.DefaultJsonProtocol

/**
 * Created by bj on 25.06.15.
 */
object TaskSnapshotJsonProtocol extends DefaultJsonProtocol{
  //def apply(t: TaskSnapshot[_]): TaskSnapshotResponse = new TaskSnapshotResponse(title = t.title, text = t.text)

  implicit val taskSnapshotResponseFormat = jsonFormat2(TaskSnapshotResponse)
  implicit val tasks = jsonFormat1(Tasks)
}

case class TaskSnapshotResponse(title: String, text: String) {}

case class Tasks(tasks: List[TaskSnapshotResponse]){
  override def toString: String = {
    var string = ""
    tasks.foreach{ task =>
      string ++= "\n" + task.title
    }
    string
  }
}
