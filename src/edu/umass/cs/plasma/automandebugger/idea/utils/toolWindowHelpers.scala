package edu.umass.cs.plasma.automandebugger.idea.utils

import java.awt.BorderLayout
import javax.swing.{JPanel, BorderFactory, JTextPane, JComponent}

import com.intellij.openapi.project.Project
import edu.umass.cs.plasma.automandebugger.idea.components.RadioButtonTasksPanel
import edu.umass.cs.plasma.automandebugger.models.Tasks

/**
 * Created by bj on 01.07.15.
 */
trait toolWindowHelpers extends httpHelpers{
  def tryToGetAnUpdateOfAutoManProgramState(project: Project, mainComponent: JComponent, refreshPanel: JPanel): JComponent= {
    getCurrentTasksSnapshot match {
      case Left(exception) =>
        showErrorPanel(project, mainComponent, exception, refreshPanel)
      case Right(tasks) =>
        showTasksPanel(project, mainComponent, tasks, refreshPanel)
    }

  }

  def showTasksPanel(project: Project, mainComponent: JComponent, tasks: Tasks, refreshPanel: JPanel): RadioButtonTasksPanel = {
    println("showing tasks panel now")
    val panel: RadioButtonTasksPanel = RadioButtonTasksPanel().createRadioButtonTasks(tasks)
    //panel.setVisible(true)
    mainComponent.removeAll()
    mainComponent.add(panel, BorderLayout.NORTH)
    mainComponent.add(refreshPanel, BorderLayout.SOUTH)
    panel
  }

  def showErrorPanel(project: Project, mainComponent: JComponent, exception: Exception, refreshPanel: JPanel): JTextPane = {
    println("showing error panel now: " + exception.getMessage)
    val errorPanel = new JPanel(new BorderLayout())
    val pane: JTextPane = new JTextPane()
    pane.setText("Something went wrong, error message:\n\n" + exception.getMessage)
    pane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20))
    mainComponent.removeAll()
    errorPanel.add(pane)
    errorPanel.setVisible(true)
    mainComponent.add(errorPanel, BorderLayout.NORTH)
    mainComponent.add(refreshPanel, BorderLayout.SOUTH)
    pane
  }
}
