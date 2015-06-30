package edu.umass.cs.plasma.automandebugger.idea

import java.awt._
import java.awt.event.{ActionEvent, ActionListener, KeyEvent}
import javax.swing._

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import edu.umass.cs.plasma.automandebugger.idea.components.RadioButtonTasksPanel
import edu.umass.cs.plasma.automandebugger.idea.utils.httpHelpers
import edu.umass.cs.plasma.automandebugger.models.Tasks

/**
 * Created by bj on 25.06.15.
 */
class IdeaPluginToolWindow extends ToolWindowFactory with httpHelpers {
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val component: JComponent = toolWindow.getComponent
    tryToGetAnUpdateOfAutoManProgramState(project, component)
  }

  def tryToGetAnUpdateOfAutoManProgramState(project: Project, mainComponent: JComponent): Unit = {
    getCurrentTasksSnapshot match {
      case Left(exception) => showErrorPanel(project, mainComponent, exception)
      case Right(tasks) => showTasksPanel(project, mainComponent, tasks)
    }
  }

  def showTasksPanel(project: Project, mainComponent: JComponent, tasks: Tasks): Unit = {
    val panel: RadioButtonTasksPanel = RadioButtonTasksPanel().createRadioButtonTasks(tasks)
    panel.setVisible(true)
    mainComponent.add(panel, BorderLayout.NORTH)
  }

  def showErrorPanel(project: Project, mainComponent: JComponent, exception: Exception): Unit = {
    mainComponent.add(new JTextArea("Something went wrong, error message:\n\n" + exception.getMessage), BorderLayout.CENTER)
  }
}

