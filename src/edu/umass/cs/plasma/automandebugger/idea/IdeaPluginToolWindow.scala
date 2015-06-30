package edu.umass.cs.plasma.automandebugger.idea

import java.awt._
import java.awt.event.{ActionEvent, ActionListener, KeyEvent}
import javax.swing._

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import edu.umass.cs.plasma.automandebugger.idea.utils.httpHelpers

/**
 * Created by bj on 25.06.15.
 */
class IdeaPluginToolWindow extends ToolWindowFactory with httpHelpers {
  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    val component: JComponent = toolWindow.getComponent

    val panel: RadioButtonTasks = RadioButtonTasks().createRadioButtonTasks("test sting")
    panel.setVisible(true)
    component.add(panel)

  }
}

case class RadioButtonTasks() extends JPanel(new BorderLayout()) with ActionListener {

  val label = new JLabel()

  def createRadioButtonTasks(test: String): RadioButtonTasks = {
    val firstButton = new JRadioButton("first task")
    firstButton.setMnemonic(KeyEvent.VK_B)
    firstButton.setActionCommand("first task title")
    firstButton.setSelected(true)

    val secondButton = new JRadioButton("second task")
    secondButton.setMnemonic(KeyEvent.VK_C)
    secondButton.setActionCommand("second task title")

    val buttonGroup = new ButtonGroup()
    buttonGroup.add(firstButton)
    buttonGroup.add(secondButton)

    firstButton.addActionListener(this)
    secondButton.addActionListener(this)

    label.setPreferredSize(new Dimension(100, 100))
    label.setText("Choose task you want to display")

    val radioPanel = new JPanel(new GridLayout(0, 1))
    radioPanel.add(firstButton)
    radioPanel.add(secondButton)

    val radioButtonTasks = new RadioButtonTasks()
    radioButtonTasks.add(radioPanel, BorderLayout.LINE_START);
    radioButtonTasks.add(label, BorderLayout.CENTER);
    radioButtonTasks.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    radioButtonTasks
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    label.setText("Chosen task title: '" + e.getActionCommand + "'")
  }
}
