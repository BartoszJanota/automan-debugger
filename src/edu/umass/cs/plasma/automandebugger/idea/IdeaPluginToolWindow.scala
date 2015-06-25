package edu.umass.cs.plasma.automandebugger.idea

import java.awt.{Dimension, GridLayout}
import javax.swing._

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import edu.umass.cs.plasma.automandebugger.idea.utils.httpHelpers
import edu.umass.cs.plasma.automandebugger.models.Tasks

import scala.collection.immutable
import scala.collection.immutable.Seq

/**
 * Created by bj on 25.06.15.
 */
class IdeaPluginToolWindow extends ToolWindowFactory with httpHelpers{
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val component: JComponent = toolWindow.getComponent

    import spray.json._
    import edu.umass.cs.plasma.automandebugger.models.TaskSnapshotJsonProtocol._

    val res = get("http://localhost:8888/state").toJson

    //it needs to be parsed the way belowK
    //val tasks = res.convertTo[Tasks]

    val panel = new JPanel()
    panel.setPreferredSize(new Dimension(200, 400));
    panel.setLayout(new GridLayout(2,1))
    panel.add(new JButton("Refresh"))
    panel.add(new JTextArea("Here you can see your current tasks: \n" + res.prettyPrint))

    panel.setVisible(true);

    component.getParent.add(panel)

  }
}