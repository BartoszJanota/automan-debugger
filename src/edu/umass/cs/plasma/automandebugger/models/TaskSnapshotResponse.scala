package edu.umass.cs.plasma.automandebugger.models

//import spray.json.DefaultJsonProtocol

/**
 * Created by bj on 25.06.15.
 */
/*trait TaskSnapshotJsonProtocol extends DefaultJsonProtocol{
  //def apply(t: TaskSnapshot[_]): TaskSnapshotResponse = new TaskSnapshotResponse(title = t.title, text = t.text)

  implicit val taskSnapshotResponseFormat = jsonFormat2(TaskSnapshotResponse.apply)
  implicit val tasks = jsonFormat1(Tasks.apply)
}*/

case class TaskSnapshotResponse(title: String, text: String) {}

case class Tasks(tasks: List[TaskSnapshotResponse]){}
