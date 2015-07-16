package front

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom

object AIDBrowserScripts extends js.JSApp {
  def main(): Unit = {
    val paragraph = dom.document.createElement("p")
    paragraph.innerHTML = "<strong>AID scripts work well!</strong>"
    dom.document.getElementById("main-component").appendChild(paragraph)
  }
}

