package edu.umass.cs.plasma.automandebugger.idea

import com.intellij.openapi.actionSystem.{CommonDataKeys, AnActionEvent, AnAction}
import com.intellij.openapi.ui.Messages
import edu.umass.cs.plasma.automandebugger.idea.utils.httpHelpers


/**
 * Created by bj on 24.06.15.
 */
class IdeaPluginDialog extends AnAction("IDEA_PLUGIN") with httpHelpers{
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getData(CommonDataKeys.PROJECT)

    val tasks: String = get("http://localhost:8888/state")

    Messages.showMessageDialog(project, "Hello from AID Dialog! Here you can see your current tasks: " + tasks, "AID", Messages.getInformationIcon)
  }
}
