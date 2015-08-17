package edu.umass.cs.plasma.automandebugger.models

import java.util.{Date, UUID}

import edu.umass.cs.automan.core.info.QuestionType._
import edu.umass.cs.automan.core.logging.TaskSnapshot
import edu.umass.cs.automan.core.scheduler.SchedulerState
import spray.http.DateTime
import spray.json._

import DefaultJsonProtocol._

/**
 * Created by bj on 25.06.15.
 */
object TaskSnapshotJsonProtocol extends DefaultJsonProtocol {
  //def apply(t: TaskSnapshot[_]): TaskSnapshotResponse = new TaskSnapshotResponse(title = t.title, text = t.text)

  implicit val taskSnapshotResponseFormat = jsonFormat14(TaskSnapshotResponse.apply)
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
                              answer = t.answer.getOrElse("None").toString,
                              state_changed_at = t.state_changed_at.getTime,
                              question_type = t.question_type.toString,
                              task_id = t.task_id.toString,
                              question_id = t.question_id.toString)
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
                                question_type: String,
                                task_id: String,
                                question_id: String) {
  override def toString: String = {
    "\n  * title:   " + title + "\n" +
    "  * task_id:   " + task_id + "\n" +
    "  * question_id:   " + question_id + "\n" +
    "  * text:   " + text + "\n" +
    "  * round:   " + round + "\n" +
    "  * timeout_in_s:   " + timeout_in_s + "\n" +
    "  * worker_timeout:   " + worker_timeout + "\n" +
    "  * created_at:   " + new Date(created_at).toString + "\n" +
    "  * state:   " + state + "\n" +
    "  * worker_id:   " + worker_id + "\n" +
    "  * answer:   " + answer + "\n" +
    "  * state_changed_at:   " + new Date(state_changed_at).toGMTString + "\n" +
    "  * question_type:   " + question_type + "\n"
  }
}

case class Tasks(tasks: List[TaskSnapshotResponse]) {}
