package edu.umass.cs.plasma.automandebugger.idea.browser

import java.awt.{Dimension, BorderLayout}
import java.io._
import java.util.Scanner
import javafx.application.Platform
import javafx.beans.value.{ObservableValue, ChangeListener}
import javafx.concurrent.Worker
import javafx.embed.swing.JFXPanel
import javafx.scene.{Group, Scene}
import javafx.scene.web.WebView
import javafx.stage.Stage
import javax.swing._
import javafx.concurrent.Worker.State;

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}

/**
 * Created by bj on 13.07.15.
 */
class IdeaPluginToolBrowserWindow extends ToolWindowFactory{

  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val component: JComponent = toolWindow.getComponent

    val mainPanel = new JPanel(new BorderLayout())
    mainPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3))
    mainPanel.setPreferredSize(new Dimension(150, 450))

    val browserPanel = new JPanel(new BorderLayout())

    val jfxPanel = new JFXPanel()

    Platform.runLater(new Runnable {
      override def run(): Unit = {

        val stage = new Stage()
        stage.setTitle("StageJava FX")
        stage.setResizable(true)

        val root = new Group()
        val scene = new Scene(root, 100, 450)
        stage.setScene(scene)
        //scene.getStylesheets.
        //val css = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/bootstrap.css")).mkString
        //scene.getStylesheets.add(css)
        val webView = new WebView()
        //webView.getStylesheets.add(css)

        val webEngine = webView.getEngine
        val text = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/index.html")).mkString
        val debuggerJsFastopt = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/automan-debugger-frontend-fastopt.js")).mkString
        val debuggerJsLauncher = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/automan-debugger-frontend-launcher.js")).mkString

        webEngine.setJavaScriptEnabled(true)
        webEngine.loadContent(text)
        val children = root.getChildren
        children.add(webView)

        webEngine.getLoadWorker.stateProperty.addListener(new ChangeListener[State]() {
          override def changed(observableValue: ObservableValue[_ <: State], t: State, t1: State): Unit = {
            if (t1 == Worker.State.SUCCEEDED) {
              webEngine.executeScript(debuggerJsFastopt)
              webEngine.executeScript(debuggerJsLauncher)
            }

          }
        })
        jfxPanel.setScene(scene)
      }
    })

    browserPanel.add(jfxPanel, BorderLayout.CENTER)
    mainPanel.add(browserPanel, BorderLayout.CENTER)

    component.add(mainPanel, BorderLayout.CENTER)
  }
}
