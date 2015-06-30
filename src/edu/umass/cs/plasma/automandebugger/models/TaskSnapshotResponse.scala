package edu.umass.cs.plasma.automandebugger.models

import java.util.{Date, UUID}

import edu.umass.cs.automan.core.info.QuestionType._
import edu.umass.cs.automan.core.logging.TaskSnapshot
import edu.umass.cs.automan.core.scheduler.SchedulerState
import spray.json._

import DefaultJsonProtocol._

/**
 * Created by bj on 25.06.15.
 */
object TaskSnapshotJsonProtocol extends DefaultJsonProtocol {
  //def apply(t: TaskSnapshot[_]): TaskSnapshotResponse = new TaskSnapshotResponse(title = t.title, text = t.text)

  implicit val taskSnapshotResponseFormat = jsonFormat12(TaskSnapshotResponse.apply)
  //implicit val tasks = jsonFormat1(Tasks.apply)

  implicit object TasksJson extends RootJsonFormat[Tasks] {

    override def read(value: JsValue): Tasks = {
      Tasks(value.asJsObject.getFields("tasks").head.convertTo[List[TaskSnapshotResponse]])
    }

    override def write(obj: Tasks): JsValue = JsObject("tasks" -> JsArray(obj.tasks.map(taskSnapshotResponseFormat.write).toVector))
  }
}

object TaskSnapshotResponse {
  def applyFromTaskSnapshot(t: TaskSnapshot[_]): TaskSnapshotResponse =
    new TaskSnapshotResponse(title = t.title,
                              text = t.text,
                              round = t.round,
                              timeout_in_s = t.timeout_in_s,
                              worker_timeout = t.worker_timeout,
                              cost = t.cost,
                              created_at = t.created_at.getTime,
                              state = t.state.toString,
                              worker_id = t.worker_id.getOrElse("Unavailable"),
                              answer = t.answer.toString,
                              state_changed_at = t.state_changed_at.getTime,
                              question_type = t.question_type.toString)
}

case class TaskSnapshotResponse(
                                title: String,
                                text: String,
                                round: Int,
                                timeout_in_s: Int,
                                worker_timeout: Int,
                                cost: BigDecimal,
                                created_at: Long,
                                state: String,
                                worker_id: String,
                                answer: String,
                                state_changed_at: Long,
                                question_type: String) {}

case class Tasks(tasks: List[TaskSnapshotResponse]) {}
