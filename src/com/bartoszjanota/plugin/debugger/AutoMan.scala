package com.bartoszjanota.plugin.debugger

import com.intellij.openapi.actionSystem.{CommonDataKeys, AnActionEvent, AnAction}
import com.intellij.openapi.ui.Messages

/**
 * Created by bj on 23.06.15.
 */
class AutoMan extends AnAction("AutoMan") {

  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getData(CommonDataKeys.PROJECT)
    Messages.showMessageDialog(project, "This is the AutoMan Debugger Plugin!", "Dialog", Messages.getInformationIcon)
  }

}
