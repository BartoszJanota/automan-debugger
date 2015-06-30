package edu.umass.cs.plasma.automandebugger.idea

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.ui.Messages
import edu.umass.cs.plasma.automandebugger.idea.utils.httpHelpers
import edu.umass.cs.plasma.automandebugger.models.Tasks

/**
  * Created by bj on 24.06.15.
 */
class IdeaPluginDialog extends AnAction("IDEA_PLUGIN") with httpHelpers{
  //implicit val system: ActorSystem = ActorSystem("AutoManDebuggerDialog")
  //implicit val executionContext = system.dispatcher

  override def update(e: AnActionEvent): Unit = super.update(e)

  override def actionPerformed(e: AnActionEvent): Unit = {

    val project = e.getData(CommonDataKeys.PROJECT)

    import spray.json._
    import DefaultJsonProtocol._

    import edu.umass.cs.plasma.automandebugger.models.TaskSnapshotJsonProtocol._

    val res: String = get("http://localhost:8888/state")
    val json: JsValue = res.parseJson

    val taskSnapshots: Tasks = TasksJson.read(json)

    Messages.showMessageDialog(project, "Hello from AID Dialog!\n Here you can see your " + taskSnapshots.tasks.size + "current tasks:\n" + taskSnapshots.tasks.toString(), "AID", Messages.getInformationIcon)

  }
}
