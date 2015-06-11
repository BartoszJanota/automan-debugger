package com.bartoszjanota.plugin;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Created by bj on 24.03.15.
 */
public class JavaAction extends AnAction{
    public JavaAction() {
        super("JAVA_ITEM");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Messages.showMessageDialog(project, "Hello From Java Action!", "Dialog", Messages.getInformationIcon());
    }

}
