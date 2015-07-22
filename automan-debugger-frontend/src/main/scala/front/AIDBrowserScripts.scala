package front

import models.{TaskSnapshotResponse, Tasks}
import org.scalajs.dom
import org.scalajs.dom.raw._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global
import upickle._

import scala.scalajs.js.annotation.JSExportAll
import scala.util.Try

@JSExportAll
object AIDBrowserScripts extends js.JSApp {

  val d = dom.document

  val qMap = Map("question_1" -> List("task 1 a", "task 1 b", "task 1 c"),
    "question_2" -> List("task 2 a", "task 2 b", "task 2 c"),
    "question_3" -> List("task 3 a", "task 3 b", "task 3 c"),
    "question_4" -> List("task 4 a", "task 4 b", "task 4 c"))

  def displayQuestionTasks(question: String): Unit = {
    global.console.log("logValue: " + question)
    val t_select = d getElementById "t_select"
    while (t_select.firstChild != null) {
      t_select.removeChild(t_select.firstChild)
    }
    qMap(question).foreach{t =>
      val select = d createElement "option"
      select setAttribute("value", t)
      select.innerHTML = t
      t_select appendChild select
    }
  }

  def main(): Unit = {

    val div = d getElementById "tasks-completion"

    val q_select = d getElementById "q_select"
    q_select setAttribute("onchange", "front.AIDBrowserScripts().displayQuestionTasks(value)")

    qMap.foreach{ q =>
      val select = d createElement "option"
      select setAttribute("value", q._1)
      select.innerHTML = q._1
      q_select appendChild select
    }

    displayQuestionTasks(qMap.head._1)

    val chat = new WebSocket("ws://localhost:8128/")

    global.console.log("chat has been created")

    chat.onopen = { (event: Event) ⇒
      chat.send("chat has been opened")
      global.console.log("chat has been opened")
      val p = d createElement "p"
      p innerHTML = "chat has been opened"
      div.appendChild(p)
      event
    }
    chat.onerror = { (event: ErrorEvent) ⇒
      global.console.log(s"Failed: code: ${event.colno}")
    }
    chat.onmessage = { (event: MessageEvent) ⇒
      global.console.log(s"Got a mess: ${event.data.toString}")
      Try{
        val tasks = read[Tasks](event.data.toString)
        tasks.tasks.foreach{ t =>
          val p = d createElement "p"
          p innerHTML = t.toString
          div.appendChild(p)
        }
      }

    }
    chat.onclose = { (event: Event) ⇒
      global.console.log("Connection to chat lost. You can try to rejoin manually.")
      val p = d createElement "p"
      p innerHTML = "Connection to chat lost. You can try to rejoin manually."
      div.appendChild(p)
    }
  }

}

