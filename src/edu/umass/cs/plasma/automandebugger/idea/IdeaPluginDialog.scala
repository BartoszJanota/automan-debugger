package edu.umass.cs.plasma.automandebugger.idea

import com.intellij.openapi.actionSystem.{CommonDataKeys, AnActionEvent, AnAction}
import com.intellij.openapi.ui.Messages
import edu.umass.cs.plasma.automandebugger.idea.utils.httpHelpers
import edu.umass.cs.plasma.automandebugger.models.Tasks

/**
  * Created by bj on 24.06.15.
 */
class IdeaPluginDialog extends AnAction("IDEA_PLUGIN") with httpHelpers{
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getData(CommonDataKeys.PROJECT)

    import spray.json._
    import edu.umass.cs.plasma.automandebugger.models.TaskSnapshotJsonProtocol._


    //it needs to be parsed the way below
    val res: JsValue = get("http://localhost:8888/state").toJson
    //val tasks = res.convertTo[Tasks](jsonReader)

    Messages.showMessageDialog(project, "Hello from AID Dialog! Here you can see your current tasks:\n" + res.prettyPrint, "AID", Messages.getInformationIcon)
  }
}
