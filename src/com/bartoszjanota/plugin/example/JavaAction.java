package com.bartoszjanota.plugin.example;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.bartoszjanota.plugin.example.ScalaClass;

/**
 * Created by bj on 24.03.15.
 */
public class JavaAction extends AnAction{
    public JavaAction() {
        super("JAVA_ITEM");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        ScalaClass scalaClass = new ScalaClass();
        Messages.showMessageDialog(project, "Hello From Java Action! This is ScalaClass intro:" + scalaClass.printYourself(), "Dialog", Messages.getInformationIcon());
    }

}
