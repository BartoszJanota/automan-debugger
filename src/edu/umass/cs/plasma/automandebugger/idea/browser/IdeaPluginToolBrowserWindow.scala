package edu.umass.cs.plasma.automandebugger.idea.browser

import java.awt.event.{ActionEvent, ActionListener}
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
    //mainPanel.setPreferredSize(new Dimension(300, 450))

    val jfxPanel: JFXPanel = new JFXPanel()
    //jfxPanel.setPreferredSize(new Dimension(500, 800))
    jfxPanel.setSize(500, 800)

    Platform.setImplicitExit(false)

    Platform.runLater(new Runnable {
      override def run(): Unit = {
        loadPage(jfxPanel)
        mainPanel.add(jfxPanel, BorderLayout.NORTH)
      }
    })

    val refreshButton: JButton = new JButton("Refresh AID")
    refreshButton.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        println("refreshing now!")
        Platform.runLater(new Runnable {
          override def run(): Unit = {
            mainPanel.remove(jfxPanel)
            loadPage(jfxPanel)
            mainPanel.add(jfxPanel, BorderLayout.NORTH)
          }
        })
      }
    })


    mainPanel.add(refreshButton, BorderLayout.SOUTH)
    component.add(mainPanel, BorderLayout.CENTER)
  }

  def loadPage(jfxPanel: JFXPanel): Unit = {
    val stage = new Stage()
    stage.setTitle("StageJava FX")
    stage.setResizable(true)
    stage.setHeight(800)
    stage.setWidth(500)

    val root = new Group()
    val scene = new Scene(root, 500, 800)
    stage.setScene(scene)
    val webView = new WebView()

    val webEngine = webView.getEngine
    val text = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/index.html")).mkString
    val debuggerJsFastopt = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/automan-debugger-frontend-fastopt.js")).mkString
    val debuggerJsLauncher = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/automan-debugger-frontend-launcher.js")).mkString
    val debuggerJsDeps = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/automan-debugger-frontend-jsdeps.js")).mkString

    webEngine.setJavaScriptEnabled(true)
    webEngine.loadContent(text)
    val children = root.getChildren
    children.add(webView)

    webEngine.getLoadWorker.stateProperty.addListener(new ChangeListener[State]() {
      override def changed(observableValue: ObservableValue[_ <: State], t: State, t1: State): Unit = {
        if (t1 == Worker.State.SUCCEEDED) {
          webEngine.executeScript(debuggerJsDeps)
          webEngine.executeScript(debuggerJsFastopt)
          webEngine.executeScript(debuggerJsLauncher)
        }

      }
    })
    jfxPanel.setScene(scene)
  }
}
