package com.bartoszjanota.plugin

import com.intellij.openapi.actionSystem._
import com.intellij.openapi.ui.Messages

class ScalaAction() extends AnAction("SCALA_ITEM") {

  override def actionPerformed(e: AnActionEvent): Unit = {
    val project = e.getData(CommonDataKeys.PROJECT)
    Messages.showMessageDialog(project, "Hello from Scala Action!", "Dialog", Messages.getInformationIcon)
  }

}

object ScalaAction {

}