package edu.umass.cs.plasma.automandebugger.idea

import java.awt._
import java.awt.event.{ActionEvent, ActionListener, KeyEvent}
import javax.accessibility.Accessible
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
    var autoManAreaComponent = tryToGetAnUpdateOfAutoManProgramState(project, component)

    val refreshButton: JButton = new JButton("Refresh AutoMan Program state")
    refreshButton.setSize(100, 30)
    refreshButton.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (autoManAreaComponent != null){
          component.remove(autoManAreaComponent)
        }
        refreshButton.setText("Loading...")
        autoManAreaComponent = tryToGetAnUpdateOfAutoManProgramState(project, component)
        refreshButton.setText("Refresh AutoMan Program state")
      }
    })
    component.add(refreshButton, BorderLayout.SOUTH)

  }

  def tryToGetAnUpdateOfAutoManProgramState(project: Project, mainComponent: JComponent): JComponent= {
    getCurrentTasksSnapshot match {
      case Left(exception) =>
        showErrorPanel(project, mainComponent, exception)
      case Right(tasks) =>
        showTasksPanel(project, mainComponent, tasks)
    }

  }

  def showTasksPanel(project: Project, mainComponent: JComponent, tasks: Tasks): RadioButtonTasksPanel = {
    val panel: RadioButtonTasksPanel = RadioButtonTasksPanel().createRadioButtonTasks(tasks)
    panel.setVisible(true)
    mainComponent.add(panel, BorderLayout.CENTER)
    panel
  }

  def showErrorPanel(project: Project, mainComponent: JComponent, exception: Exception): JTextPane = {
    val pane: JTextPane = new JTextPane()
    pane.setText("Something went wrong, error message:\n\n" + exception.getMessage)
    pane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20))
    mainComponent.add(pane, BorderLayout.CENTER)
    pane
  }
}

