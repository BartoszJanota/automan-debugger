package front

import java.util.Date

import front.components.piechart.{StringIntVal, _}
import japgolly.scalajs.react.React
import models.{TaskSnapshotResponse, Tasks}
import org.scalajs.dom
import org.scalajs.dom.ext.Color
import org.scalajs.dom.raw._
import upickle._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global
import scala.scalajs.js.annotation.JSExportAll
import scala.util.Try

@JSExportAll
object AIDBrowserScripts extends js.JSApp {

  val d = dom.document

  var tasksPerQuestionMap: Map[String, List[String]] = Map.empty
  var questionNames: Map[String, String] = Map.empty
  var tasksMap: Map[String, TaskSnapshotResponse] = Map.empty
  var taskAnswersCount: Map[String, Int] = Map.empty
  var taskStatesCount: Map[String, Int] = Map.empty
  var tasksTimelineData = scala.collection.mutable.Map[String, List[(Double, String)]]()

  val palette: Map[String, String] = Map("READY" -> getHex(Color.Blue),
                                          "RUNNING" -> getHex(Color.Green),
                                          "ANSWERED" -> getHex(Color.White),
                                          "DUPLICATE" -> getHex(Color.Cyan),
                                          "ACCEPTED" -> getHex(Color.Magenta),
                                          "REJECTED" -> getHex(Color.Yellow),
                                          "TIMEOUT" -> getHex(Color.Red),
                                          "CANCELLED" -> getHex(Color.Black)
  )

  def getHex(c: Color): String = {
    String.format("#%02x%02x%02x", new Integer(c.r), new Integer(c.g), new Integer(c.b))
  }

  def displayQuestionTasks(question: String): Unit = {
    global.console.log("logValue: " + question)
    val t_select = d getElementById "t_select"
    removeChildren(t_select)
    tasksPerQuestionMap(question).foreach { t =>
      val select = d createElement "option"
      select setAttribute("value", t)
      select.innerHTML = t
      t_select appendChild select
    }

    displayTaskInfo(tasksPerQuestionMap(question).head)
  }

  def displayInfoLabel(labelName: String, value: String): _root_.org.scalajs.dom.raw.Node = {
    val div = d createElement "div"
    div setAttribute("class", "bottom-margin")
    div setAttribute("style", "display: inline-block; width: 30%")
    val label = d createElement "label"
    label setAttribute("class", "control-label small")
    label.innerHTML = labelName
    val innerDiv = d createElement "div"
    innerDiv setAttribute("class", "controls readonly")
    innerDiv.innerHTML = value
    div appendChild label
    div appendChild innerDiv
    div
  }

  def createRow: Element = {
    val row = d.createElement("div")
    row.setAttribute("class", "row left-margin")
    row
  }

  def displayTaskInfo(taskId: String) = {
    global.console.log(taskId)
    d.getElementById("task-detail-id").innerHTML = " (id = " + taskId + ")"
    val taskDetail: Element = d.getElementById("task-detail")
    val task: TaskSnapshotResponse = tasksMap(taskId)

    removeChildren(taskDetail)

    val firstRow = createRow
    firstRow appendChild displayInfoLabel("Cost", task.cost.toString)
    firstRow appendChild displayInfoLabel("Created at", new Date(task.created_at.toLong).toString)
    taskDetail appendChild firstRow

    val secondRow = createRow
    secondRow appendChild displayInfoLabel("State", task.state)
    secondRow appendChild displayInfoLabel("Changed at", new Date(task.state_changed_at.toLong).toString)
    taskDetail appendChild secondRow

    val thirdRow = createRow
    thirdRow appendChild displayInfoLabel("Worker", task.worker_id)
    thirdRow appendChild displayInfoLabel("Answer", task.answer)
    taskDetail appendChild thirdRow
  }

  protected def appendBrTo(el: Element): Unit = {
    el.appendChild(d createElement "br")
  }

  def removeChildren(el: Element): Unit = {
    while (el.firstChild != null) {
      el.removeChild(el.firstChild)
    }
  }

  def renderSelectBoxes = {
    d.getElementById("no_questions_and_tasks_warn").setAttribute("style", "display: none")
    d.getElementById("selects").removeAttribute("style")

    val q_select = d getElementById "q_select"
    q_select setAttribute("onchange", "front.AIDBrowserScripts().displayQuestionTasks(value)")

    val t_select = d getElementById "t_select"
    t_select setAttribute("onchange", "front.AIDBrowserScripts().displayTaskInfo(value)")


    removeChildren(q_select)
    //q_select setAttribute("style", "float: left")

    tasksPerQuestionMap.foreach { q =>
      val select = d createElement "option"
      select setAttribute("value", q._1)
      select.innerHTML = questionNames(q._1)
      q_select appendChild select
    }

    displayQuestionTasks(tasksPerQuestionMap.head._1)
  }

  def renderTaskAnswers() = {
    val parentDiv = d getElementById ("tasks-completion")

    val args: List[StringIntVal] = taskAnswersCount.map(t => StringIntVal(t._1, t._2)).toList

    val chart = PieChart(args)
    React.render(chart, parentDiv)

  }

  def renderTaskStates() = {
    val parentDiv = d getElementById ("tasks-states")
    val args: List[StringIntVal] = taskStatesCount.map(t => StringIntVal(t._1, t._2)).toList
    val chart = PieChart(args)
    React.render(chart, parentDiv)
  }

  def displayGeneralInfo() = {
    val generalInfo: Element = d.getElementById("general-info")

    removeChildren(generalInfo)

    val firstRow = createRow
    firstRow appendChild displayInfoLabel("Number of questions", questionNames.size.toString)
    firstRow appendChild displayInfoLabel("Number of tasks", tasksMap.size.toString)
    generalInfo appendChild firstRow

    val totalTasksCost: Double = tasksMap.map(_._2.cost).sum
    val avgTaskCost = totalTasksCost / tasksMap.size

    val roundedAvgTaskCost = BigDecimal(avgTaskCost).setScale(2, BigDecimal.RoundingMode.HALF_UP)
    val roundedTotalTasksCost = BigDecimal(totalTasksCost).setScale(2, BigDecimal.RoundingMode.HALF_UP)

    val secondRow = createRow
    secondRow appendChild displayInfoLabel("Average task cost", "$" + roundedAvgTaskCost.toString)
    secondRow appendChild displayInfoLabel("Total tasks cost", "$" + roundedTotalTasksCost.toString)
    generalInfo appendChild secondRow
  }

  def renderTimeline() = {
    val tab = d getElementById "timeline-tab"
    removeChildren(tab)
    createTableHeaders(tab)

    val now = System.currentTimeMillis() / 1000
    val minuteAgo = now - 60

    tasksTimelineData.foreach{ taskData =>
      val tr = d createElement "tr"
      val td = d createElement "td"
      td.setAttribute("width", "10%")
      td.setAttribute("style", "font-size: 9px")
      td.innerHTML = taskData._1.substring(0,8) + "..."
      tr appendChild td
      var offset = 0
      taskData._2.filter(_._1 / 1000 >= minuteAgo).reverse.foreach{ state =>
        val cellsCount: Int = ((state._1 / 1000) - minuteAgo).toInt
        val cellsCountWithOffset: Int = cellsCount - offset
        offset = cellsCount
        0 to cellsCountWithOffset - 1 foreach { i =>
          val td: Element = createTd(state)
          tr appendChild td
        }
      }


      val lastState = taskData._2.head
      0 to 60 - offset - 1 foreach { i =>
        val td = createTd(lastState)
        tr appendChild td
      }

      tab appendChild tr
    }
  }

  def createTableHeaders(tab: Element): Node = {
    val tr = d createElement "tr"
    tr appendChild createTh("Task Id", 1)
    tr appendChild createTh("Last 60 seconds", 60)
    tab appendChild tr
  }

  def createTh(text: String, colspan: Int)= {
    val th = d createElement "th"
    th.setAttribute("style", "font-size: 9px")
    th.setAttribute("colspan", colspan.toString)
    th.innerHTML = text
    th
  }

  def createBlankTd() = {
    val td = d createElement "td"
    td.setAttribute("width", "1.5%")
    td.setAttribute("style", "font-size: 9px")
    td
  }

  def createTd(state: (Double, String)): Element = {
    val td = d createElement "td"
    td.setAttribute("width", "1.5%")
    td.setAttribute("style", "font-size: 10px")
    td.setAttribute("bgcolor", palette(state._2))
    //td.innerHTML = state._2.substring(0, 1)
    td
  }

  def renderTimelineLegend() = {
    val legend = d getElementById "timeline-legend"
    val span = d createElement "span"
    span.setAttribute("style", "font-size:9px;margin-right: 5px")
    span.innerHTML = "Task States:"
    legend appendChild span
    palette.foreach{ color =>
      val span = d createElement "span"
      span.setAttribute("style", "color:" + color._2 + ";font-size:9px;margin-right: 5px")
      span.innerHTML = color._1
      legend appendChild span
    }
  }

  def main(): Unit = {

    val consoleDebug = d getElementById "console-debug"

    val ws = new WebSocket("ws://localhost:8128/")

    global.console.log("chat has been created")

    renderTimelineLegend()

    ws.onopen = { (event: Event) ⇒
      //ws.send("chat has been opened")
      global.console.log("chat has been opened")
      val p = d createElement "p"
      p innerHTML = "chat has been opened"
      consoleDebug.appendChild(p)
      event
    }
    ws.onerror = { (event: ErrorEvent) ⇒
      global.console.log(s"Failed: code: ${event.colno}")
    }
    ws.onmessage = { (event: MessageEvent) ⇒
      //global.console.log(s"Got a mess: ${event.data.toString}")
      Try {
        val parsedTasks = read[Tasks](event.data.toString)
//        parsedTasks.tasks.foreach { t =>
//          val p = d createElement "p"
//          p innerHTML = t.toString
//          consoleDebug.appendChild(p)
//        }
        val questionTaskTuples = parsedTasks.tasks.map(t => t.question_id -> t.task_id)

        val questionNamesTuples = parsedTasks.tasks.map(t => t.question_id -> t.title)

        questionNames = questionNamesTuples.groupBy(_._1).map {
          case (k, v) => (k, v.map(_._2).head)
        }

        tasksMap = parsedTasks.tasks.map(t => t.task_id -> t).groupBy(_._1).map {
          case (k, v) => (k, v.map(_._2).head)
        }

        taskAnswersCount = parsedTasks.tasks.map(t => t.answer -> t).groupBy(_._1).map {
          case (k, v) => (k, v.map(_._2).size)
        }

        taskStatesCount = parsedTasks.tasks.map(t => t.state -> t).groupBy(_._1).map {
          case (k, v) => (k, v.map(_._2).size)
        }

        parsedTasks.tasks.foreach{ task =>
          tasksTimelineData.get(task.task_id) match {
            case Some(t) =>
              if (t.head._1 != task.state_changed_at){
                tasksTimelineData = update(tasksTimelineData, task.task_id)(t => (task.state_changed_at, task.state) :: t)
              }
            case None =>
              tasksTimelineData = tasksTimelineData + (task.task_id -> List(task.state_changed_at -> task.state))
          }
        }

        println("taskTimeLineData size is " + tasksTimelineData.size)
        tasksTimelineData.foreach(println)
        println("end of timeline data")

        renderTimeline()
        renderTaskAnswers()
        renderTaskStates()
        displayGeneralInfo()

        tasksPerQuestionMap = questionTaskTuples.groupBy(_._1).map {
          case (k, v) => (k, v.map(_._2).toList)
        }
        if (!tasksMap.isEmpty) {
          renderSelectBoxes
        }
      }

    }
    ws.onclose = { (event: Event) ⇒
      global.console.log("Connection to chat lost. You can try to rejoin manually.")
      val p = d createElement "p"
      p innerHTML = "Connection to chat lost. You can try to rejoin manually."
      consoleDebug.appendChild(p)
    }
  }

  def update[A, B](m: scala.collection.mutable.Map[A, B], k: A)(f: B => B) = m.updated(k, f(m(k)))

}

