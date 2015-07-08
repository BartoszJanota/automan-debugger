package edu.umass.cs.plasma.automandebugger.idea

import java.awt._
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing._

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import edu.umass.cs.plasma.automandebugger.idea.utils.toolWindowHelpers

/**
 * Created by bj on 25.06.15.
 */
class IdeaPluginToolWindow extends ToolWindowFactory with toolWindowHelpers {


  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val component: JComponent = toolWindow.getComponent
    var autoManAreaComponent: JComponent = null

    val refreshPanel = new JPanel(new BorderLayout())
    refreshPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10))

    val refreshButton: JButton = new JButton("Refresh AutoMan Program now")
    refreshButton.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        println("refreshing now!")
        autoManAreaComponent = tryToGetAnUpdateOfAutoManProgramState(project, component, refreshPanel)
        //component.add(refreshPanel, BorderLayout.SOUTH)
      }
    })

    def timerListener = new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        println("refreshing automatically")
        //clearMainComponent(component, autoManAreaComponent)
        autoManAreaComponent = tryToGetAnUpdateOfAutoManProgramState(project, component, refreshPanel)
        //component.add(refreshPanel, BorderLayout.SOUTH)
      }
    }

    val displayTimer = new Timer(10000, timerListener)


    val refreshAutomaticallyButton: JButton = new JButton("Enable AutoMan Program auto refresh")
    refreshAutomaticallyButton.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (displayTimer.isRunning) {
          displayTimer.stop()
          refreshAutomaticallyButton.setText("Enable AutoMan Program auto refresh (every 20 seconds)")
          println("stopping auto refresh")
        } else {
          displayTimer.start()
          refreshAutomaticallyButton.setText("Disable AutoMan Program auto refresh")
          println("starting auto refresh")
        }
      }
    })


    refreshPanel.add(refreshButton, BorderLayout.LINE_START)
    refreshPanel.add(refreshAutomaticallyButton, BorderLayout.LINE_END)

    autoManAreaComponent = tryToGetAnUpdateOfAutoManProgramState(project, component, refreshPanel)

  }


  def clearMainComponent(component: JComponent, autoManAreaComponent: JComponent): Unit = {
    if (autoManAreaComponent != null) {
      component.remove(autoManAreaComponent)
    }
  }
}

