package models

import java.util.Date


/**
 * Created by bj on 25.06.15.
 */

case class TaskSnapshotResponse(
                                 title: String,
                                 text: String,
                                 round: Int,
                                 timeout_in_s: Int,
                                 worker_timeout: Int,
                                 cost: Double,
                                 created_at: Double,
                                 state: String,
                                 worker_id: String,
                                 answer: String,
                                 state_changed_at: Double,
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
      "  * created_at:   " + new Date(created_at.toLong).toString + "\n" +
      "  * state:   " + state + "\n" +
      "  * worker_id:   " + worker_id + "\n" +
      "  * answer:   " + answer + "\n" +
      "  * state_changed_at:   " + new Date(state_changed_at.toLong).toGMTString + "\n" +
      "  * question_type:   " + question_type + "\n"
  }
}


case class Tasks(tasks: Seq[TaskSnapshotResponse])
