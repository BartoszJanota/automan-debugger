package edu.umass.cs.plasma.automandebugger.idea.browser

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Dimension, BorderLayout}
import java.io._
import java.util
import java.util.Map.Entry
import java.util.Scanner
import javafx.application.Platform
import javafx.beans.value.{ObservableValue, ChangeListener}
import javafx.concurrent.Worker
import javafx.embed.swing.JFXPanel
import javafx.scene.{Group, Scene}
import javafx.scene.web.{WebEngine, WebView}
import javafx.stage.Stage
import javax.swing._
import javafx.concurrent.Worker.State;

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import com.typesafe.config.{ConfigValue, Config, ConfigFactory}
import edu.umass.cs.plasma.automandebugger.idea.browser.IdeaPluginToolBrowserWindow.Tab
import net.ceedubs.ficus.Ficus._

/**
 * Created by bj on 13.07.15.
 */

object IdeaPluginToolBrowserWindow {

  case class Tab(id: String, visible: Boolean)

}

class IdeaPluginToolBrowserWindow extends ToolWindowFactory {
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow)= {

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
    val index = loadTextResource("/index.html")
    webEngine.setJavaScriptEnabled(true)
    webEngine.loadContent(index)
    root.getChildren.add(webView)

    webEngine.getLoadWorker.stateProperty.addListener(new ChangeListener[State]() {
      override def changed(observableValue: ObservableValue[_ <: State], t: State, t1: State): Unit = {
        if (t1 == Worker.State.SUCCEEDED) {
          webEngine.executeScript(loadTextResource("/automan-debugger-frontend-jsdeps.js"))
          webEngine.executeScript(loadTextResource("/automan-debugger-frontend-fastopt.js"))
          webEngine.executeScript(loadTextResource("/automan-debugger-frontend-launcher.js"))

          loadConfigAndHideTabs(webEngine)
        }
      }
    })
    jfxPanel.setScene(scene)
  }

  def loadConfigAndHideTabs(webEngine: WebEngine): Unit = {
    val tabsToBeHidden: List[Tab] = getTabsVisibilityConfig
    tabsToBeHidden.filter(!_.visible) foreach { tab =>
      webEngine.getDocument.getElementById(tab.id).setAttribute("style", "display:none")
    }
  }

  def loadTextResource(path: String): String = {
    scala.io.Source.fromInputStream(getClass.getResourceAsStream(path)).mkString
  }

  def getTabsVisibilityConfig: List[Tab] = {
    import net.ceedubs.ficus.readers.ArbitraryTypeReader._

    val configFile = loadTextResource("/application.conf")
    ConfigFactory.parseString(configFile).as[List[Tab]]("aid.tabs")
  }
}
