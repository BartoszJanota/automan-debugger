package edu.umass.cs.plasma.automandebugger.idea

import com.intellij.openapi.actionSystem.{CommonDataKeys, AnActionEvent, AnAction}
import com.intellij.openapi.ui.Messages


/**
 * Created by bj on 24.06.15.
 */
class IdeaPlugin extends AnAction("IDEA_PLUGIN"){
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getData(CommonDataKeys.PROJECT)

    val tasks: String = get("http://localhost:8888/state")

    Messages.showMessageDialog(project, "Hello from AID! This is plugin response: " + tasks, "AID", Messages.getInformationIcon)
  }

  def get(url: String) = scala.io.Source.fromURL(url).mkString
}
