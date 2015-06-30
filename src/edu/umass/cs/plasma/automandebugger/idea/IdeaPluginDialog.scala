package edu.umass.cs.plasma.automandebugger.idea

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import edu.umass.cs.plasma.automandebugger.idea.utils.httpHelpers
import edu.umass.cs.plasma.automandebugger.models.Tasks

/**
  * Created by bj on 24.06.15.
 */
class IdeaPluginDialog extends AnAction("IDEA_PLUGIN") with httpHelpers{

  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getData(CommonDataKeys.PROJECT)
    tryToGetAnUpdateOfAutoManProgramState(project)
  }

  def tryToGetAnUpdateOfAutoManProgramState(project: Project): Unit = {
    getCurrentTasksSnapshot match {
      case Left(exception) => showErrorMessageDialog(project, exception)
      case Right(tasks) => showCurrentTasksMessageDialog(project, tasks)
    }
  }

  def showCurrentTasksMessageDialog(project: Project, taskSnapshots: Tasks): Unit = {
    val tasksPrettyPrint = taskSnapshots.tasks.zipWithIndex.map(t => "Task no. " + t._2 + " properties:" + t._1.toString + "\n  ").mkString

    Messages.showMessageDialog(project, "This is AutoMan IntelliJ Debugger (AID)!\n\n" +
    "AID is able to show you the current state of your AutoMan program.\n\n" +
    "Here you can see your " + taskSnapshots.tasks.size + " current tasks:\n\n" + tasksPrettyPrint, "AutoMan IntelliJ Debugger (AID)", Messages.getInformationIcon)

    showRefreshDialogMessage(project)
  }

  def showRefreshDialogMessage(project: Project): Unit = {
    Messages.showYesNoDialog(project, "Would you like to get an update of your AutoMan program state and show AID again?", "AID AutoMan Reload", Messages.getQuestionIcon) match {
      case 0 => tryToGetAnUpdateOfAutoManProgramState(project)
      case _ => showEncouragement(project)
    }
  }

  def showErrorMessageDialog(project: Project, e: Exception): Unit = {
    Messages.showMessageDialog(project, "Something went wrong, error message:\n\n" + e.getMessage, "AID Error", Messages.getErrorIcon)
    showRefreshDialogMessage(project)
  }

  def showEncouragement(project: Project): Unit = {
    Messages.showMessageDialog(project, "You can always run AID again, just simply click on 'AutoMan Debugger' tab and choose 'Run AutoMan Debugger'.", "AID Remainder", Messages.getInformationIcon)
  }
}
