package edu.umass.cs.plasma.automandebugger.idea.components

import java.awt.{Color, GridLayout, Dimension, BorderLayout}
import java.awt.event.{ActionEvent, KeyEvent, ActionListener}
import javax.swing._

import edu.umass.cs.plasma.automandebugger.models.{TaskSnapshotResponse, Tasks}

/**
 * Created by bj on 30.06.15.
 */
case class RadioButtonTasksPanel() extends JPanel(new BorderLayout()) with ActionListener {

  val taskArea = new JTextPane()
  taskArea.setBackground(Color.darkGray)
  //taskArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 20 , 0))

  var currentTasks = Map[String, TaskSnapshotResponse]().empty

  def createRadioButtonTasks(taskSnapshots: Tasks): RadioButtonTasksPanel = {

    currentTasks = Map.empty[String, TaskSnapshotResponse]
    taskSnapshots.tasks.foreach{ task =>
      currentTasks += task.task_id -> task
    }

    val taskButtons: List[JRadioButton] = taskSnapshots.tasks.map{ task =>
      val taskButton = new JRadioButton(task.title)
      taskButton.setActionCommand(task.task_id)
      taskButton.addActionListener(this)
      taskButton
    }

    val buttonGroup = new ButtonGroup()
    taskButtons.foreach(buttonGroup add _)

    val radioPanel = new JPanel(new GridLayout(0, 1))
    radioPanel.setToolTipText("Tooltip text here")
    radioPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20 , 20))
    taskButtons.foreach(radioPanel add _)

    //taskArea.setPreferredSize(new Dimension(100, 100))
    taskArea.setText("\n This is AutoMan IntelliJ Debugger (AID) Tool Window version!\n\n" +
      " AID is able to show you the current state of your AutoMan program.\n" +
      " Here you can see your " + currentTasks.size + " current tasks. Just choose one on the left hand side.")

    val label: JLabel = new JLabel("AutoMan Intellij Debugger (AID) Tool Window - supervise your tasks easily.")
    label.setPreferredSize(new Dimension(100, 30))
    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0))

    val radioButtonTasks = new RadioButtonTasksPanel()
    radioButtonTasks.add(label, BorderLayout.NORTH)
    radioButtonTasks.add(radioPanel, BorderLayout.LINE_START)
    radioButtonTasks.add(taskArea, BorderLayout.CENTER)
    radioButtonTasks.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20))
    radioButtonTasks
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    val chosenTask = currentTasks(e.getActionCommand)
    taskArea.setText(" Chosen task details: \n" + chosenTask.toString + "\n\n What you can see here is a Swing Panel." +
    "\n Now only textual details are displayed, but in the nearest future it will be replaced with Swing charts." +
    "\n As a final result it should be replaced with a browser engine and Scala.js (just dreaming :>) charts.\n")
  }
}