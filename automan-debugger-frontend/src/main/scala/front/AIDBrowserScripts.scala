package front

import java.util.Date

import front.components.htmlDOMUtils
import front.components.piechart.{StringIntVal, _}
import japgolly.scalajs.react.React
import models.{TaskSnapshotResponse, Tasks}
import org.scalajs.dom
import org.scalajs.dom.raw._
import upickle._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global
import scala.scalajs.js.annotation.JSExportAll
import scala.util.Try

@JSExportAll
object AIDBrowserScripts extends js.JSApp with htmlDOMUtils{

  val d: HTMLDocument = dom.document

  var tasksPerQuestionMap: Map[String, List[String]] = Map.empty
  var questionNames: Map[String, String] = Map.empty
  var tasksMap: Map[String, TaskSnapshotResponse] = Map.empty
  var taskAnswersCount: Map[String, Int] = Map.empty
  var taskStatesCount: Map[String, Int] = Map.empty
  var tasksTimelineData = scala.collection.mutable.Map[String, List[(Double, String)]]()

  def displayQuestionTasks(question: String): Unit = {
    global.console.log("logValue: " + question)
    val t_select = d getElementById "t_select"
    removeAllElementChildren(t_select)
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

  def displayTaskInfo(taskId: String) = {
    global.console.log(taskId)
    d.getElementById("task-detail-id").innerHTML = " (id = " + taskId + ")"
    val taskDetail: Element = d.getElementById("task-detail")
    val task: TaskSnapshotResponse = tasksMap(taskId)

    removeAllElementChildren(taskDetail)

    val firstRow = createRow(d)
    firstRow appendChild displayInfoLabel("Cost", task.cost.toString)
    firstRow appendChild displayInfoLabel("Created at", new Date(task.created_at.toLong).toString)
    taskDetail appendChild firstRow

    val secondRow = createRow(d)
    secondRow appendChild displayInfoLabel("State", task.state)
    secondRow appendChild displayInfoLabel("Changed at", new Date(task.state_changed_at.toLong).toString)
    taskDetail appendChild secondRow

    val thirdRow = createRow(d)
    thirdRow appendChild displayInfoLabel("Worker", task.worker_id)
    thirdRow appendChild displayInfoLabel("Answer", task.answer)
    taskDetail appendChild thirdRow
  }

  def renderSelectBoxes() = {
    if (tasksMap.nonEmpty) {
      d.getElementById("no_questions_and_tasks_warn").setAttribute("style", "display: none")
      d.getElementById("selects").removeAttribute("style")

      val q_select = d getElementById "q_select"
      q_select setAttribute("onchange", "front.AIDBrowserScripts().displayQuestionTasks(value)")

      val t_select = d getElementById "t_select"
      t_select setAttribute("onchange", "front.AIDBrowserScripts().displayTaskInfo(value)")

      removeAllElementChildren(q_select)

      tasksPerQuestionMap.foreach { q =>
        val select = d createElement "option"
        select setAttribute("value", q._1)
        select.innerHTML = questionNames(q._1)
        q_select appendChild select
      }

      displayQuestionTasks(tasksPerQuestionMap.head._1)
    }
  }

  //TODO: this method is not complete, it is mocked right now
  def renderPredictions() = {
    val parentDiv = d getElementById "tasks-predictions-data"
    removeAllElementChildren(parentDiv)

    if (taskAnswersCount.nonEmpty){

      val topAnswer = taskAnswersCount.toList.sortBy(-_._2).head

      val firstRow = createRow(d)
      firstRow appendChild displayInfoLabel("Top Answer", topAnswer._1)
      //mocked time, it needs to be estimated somehow
      firstRow appendChild displayInfoLabel("How much time the question is going to take", "17 seconds")
      parentDiv appendChild firstRow

      val totalTasksCost: Double = tasksMap.map(_._2.cost).sum

      //mocked function, it needs to be estimated somehow
      val predictedCost = totalTasksCost * 1.17
      val roundedPredictedCost = roundUpAndAddCurrency(predictedCost)

      val secondRow = createRow(d)
      //mocked num of tasks, it needs to be estimated somehow
      secondRow appendChild displayInfoLabel("How much the question will cost", roundedPredictedCost)
      secondRow appendChild displayInfoLabel("How many more tasks AutoMan will need to schedule", "7")
      parentDiv appendChild secondRow
    }
  }

  def renderTaskAnswers() = {
    val parentDiv = d getElementById "tasks-completion"
    val args: List[StringIntVal] = taskAnswersCount.map(t => StringIntVal(t._1, t._2)).toList
    val chart = PieChart(args)
    React.render(chart, parentDiv)
  }

  def renderTaskStates() = {
    val parentDiv = d getElementById "tasks-states"
    val args: List[StringIntVal] = taskStatesCount.map(t => StringIntVal(t._1, t._2)).toList
    val chart = PieChart(args)
    React.render(chart, parentDiv)
  }

  def displayGeneralInfo() = {
    val generalInfo: Element = d.getElementById("general-info")

    removeAllElementChildren(generalInfo)

    val firstRow = createRow(d)
    firstRow appendChild displayInfoLabel("Number of questions", questionNames.size.toString)
    firstRow appendChild displayInfoLabel("Number of tasks", tasksMap.size.toString)
    generalInfo appendChild firstRow

    val totalTasksCost: Double = tasksMap.map(_._2.cost).sum
    val avgTaskCost = totalTasksCost / tasksMap.size

    val roundedAvgTaskCost = roundUpAndAddCurrency(avgTaskCost)
    val roundedTotalTasksCost = roundUpAndAddCurrency(totalTasksCost)

    val secondRow = createRow(d)
    secondRow appendChild displayInfoLabel("Average task cost", roundedAvgTaskCost)
    secondRow appendChild displayInfoLabel("Total tasks cost", roundedTotalTasksCost)
    generalInfo appendChild secondRow
  }

  def renderTimeline() = {
    val tab = d getElementById "timeline-tab"
    removeAllElementChildren(tab)
    createTableHeaders(tab, d)

    val now = System.currentTimeMillis() / 1000
    val minuteAgo = now - 60

    tasksTimelineData.foreach { taskData =>
      val tr = d createElement "tr"
      val td = d createElement "td"
      td.setAttribute("width", "10%")
      td.setAttribute("style", "font-size: 9px")
      td.innerHTML = taskData._1.substring(0, 8) + "..."
      tr appendChild td
      var offset = 0
      taskData._2.filter(_._1 / 1000 >= minuteAgo).reverse.foreach { state =>
        val cellsCount: Int = ((state._1 / 1000) - minuteAgo).toInt
        val cellsCountWithOffset: Int = cellsCount - offset
        offset = cellsCount
        0 to cellsCountWithOffset - 1 foreach { i =>
          val td: Element = createTd(state, d)
          tr appendChild td
        }
      }

      val lastState = taskData._2.head
      0 to 60 - offset - 1 foreach { i =>
        val td = createTd(lastState, d)
        tr appendChild td
      }

      tab appendChild tr
    }
  }

  def updateTasksPerQuestionMap(questionTaskTuples: Seq[(String, String)]): Unit = {
    tasksPerQuestionMap = questionTaskTuples.groupBy(_._1).map {
      case (k, v) => (k, v.map(_._2).toList)
    }
  }

  def update[A, B](m: scala.collection.mutable.Map[A, B], k: A)(f: B => B) = m.updated(k, f(m(k)))

  def updateTasksTimelineData(parsedTasks: Tasks): Unit = {
    parsedTasks.tasks.foreach { task =>
      tasksTimelineData.get(task.task_id) match {
        case Some(t) =>
          if (t.head._1 != task.state_changed_at) {
            tasksTimelineData = update(tasksTimelineData, task.task_id)(t => (task.state_changed_at, task.state) :: t)
          }
        case None =>
          tasksTimelineData = tasksTimelineData + (task.task_id -> List(task.state_changed_at -> task.state))
      }
    }
  }

  def updateTaskStatesCount(parsedTasks: Tasks): Unit = {
    taskStatesCount = parsedTasks.tasks.map(t => t.state -> t).groupBy(_._1).map {
      case (k, v) => (k, v.map(_._2).size)
    }
  }

  def updateTaskAnswersMap(parsedTasks: Tasks): Unit = {
    taskAnswersCount = parsedTasks.tasks.map(t => t.answer -> t).groupBy(_._1).map {
      case (k, v) => (k, v.map(_._2).size)
    }
  }

  def updateTasksMap(parsedTasks: Tasks): Unit = {
    tasksMap = parsedTasks.tasks.map(t => t.task_id -> t).groupBy(_._1).map {
      case (k, v) => (k, v.map(_._2).head)
    }
  }

  def updateQuestionNames(questionNamesTuples: Seq[(String, String)]): Unit = {
    questionNames = questionNamesTuples.groupBy(_._1).map {
      case (k, v) => (k, v.map(_._2).head)
    }
  }

  def printToDebugConsole(text: String): Node = {
    val consoleDebug = d getElementById "console-debug"
    val p = d createElement "p"
    p innerHTML = text
    consoleDebug.appendChild(p)
  }

  def main(): Unit = {

    val ws = new WebSocket("ws://localhost:8128/")

    global.console.log("App has been started")

    ws.onopen = { (event: Event) ⇒
      global.console.log("WS has been opened")
      printToDebugConsole("WS has been opened")
      renderTimelineLegend(d)
      event
    }
    ws.onerror = { (event: ErrorEvent) ⇒
      global.console.log(s"WS failed: code: ${event.colno}")
    }
    ws.onmessage = { (event: MessageEvent) ⇒
      //global.console.log(s"WS received a message:\n${event.data.toString}")
      Try {
        val parsedTasks = read[Tasks](event.data.toString)
        val questionTaskTuples = parsedTasks.tasks.map(t => t.question_id -> t.task_id)
        val questionNamesTuples = parsedTasks.tasks.map(t => t.question_id -> t.title)

        //for select box with questions
        updateQuestionNames(questionNamesTuples)

        //for select box with questions
        updateTasksPerQuestionMap(questionTaskTuples)

        //for a single task general info
        updateTasksMap(parsedTasks)

        //for task answers chart
        updateTaskAnswersMap(parsedTasks)

        //for task states chart
        updateTaskStatesCount(parsedTasks)

        //for tasks timeline
        updateTasksTimelineData(parsedTasks)

        renderTimeline()
        renderTaskAnswers()
        renderTaskStates()
        renderPredictions()
        displayGeneralInfo()
        renderSelectBoxes()
      }
    }
    ws.onclose = { (event: Event) ⇒
      global.console.log("Connection to WS lost. You can try to rejoin manually.")
      printToDebugConsole("Connection to WS lost. You can try to rejoin manually.")
    }
  }
}

