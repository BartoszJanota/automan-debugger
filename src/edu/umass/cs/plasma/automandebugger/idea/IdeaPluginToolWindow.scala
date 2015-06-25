package edu.umass.cs.plasma.automandebugger.idea

import javax.swing.{JComponent, JLabel}

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import edu.umass.cs.plasma.automandebugger.idea.utils.httpHelpers

/**
 * Created by bj on 25.06.15.
 */
class IdeaPluginToolWindow extends ToolWindowFactory with httpHelpers{
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val component: JComponent = toolWindow.getComponent

    val tasks: String = get("http://localhost:8888/state")
    component.getParent.add(new JLabel("Hello from AID WindowTool!\n Here you can see your current tasks: " + tasks), -1)

  }
}
