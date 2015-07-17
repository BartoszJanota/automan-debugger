package front

import org.scalajs.dom.raw.{Element, HTMLDocument}

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom

object AIDBrowserScripts extends js.JSApp {
  def main(): Unit = {
    val d = dom.document

    createChatTab(d)
    val chatDiv = createChatContent(d)
  }

  def createChatContent(d: HTMLDocument): Element = {
    val div = d getElementById "chat"

    div.setAttribute("id", "chat")
    div.setAttribute("class", "tab-pane fade")
    val h4 = d createElement "h4"
    h4 innerHTML = "Chat"
    val p = d createElement "p"
    p innerHTML = "This is a container prepared for WebSockets chat!"

    val firstChild = div.firstChild
    div.insertBefore(p, firstChild)
    div.insertBefore(h4, p)

    d.getElementById("tab-contents").appendChild(div)
    div
  }

  def createChatTab(d: HTMLDocument): Unit = {
    val li = d createElement "li"
    val a = d createElement "a"
    a setAttribute("data-toggle", "tab")
    a setAttribute("href", "#chat")
    a innerHTML = "Chat"
    li appendChild a

    d.getElementById("navs") appendChild li
  }
}

