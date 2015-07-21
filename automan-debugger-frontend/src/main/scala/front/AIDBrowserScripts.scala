package front

import org.scalajs.dom.raw._

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom
import scala.scalajs.js.Dynamic.global

object AIDBrowserScripts extends js.JSApp {
  def main(): Unit = {
    val d = dom.document

    createChatTab(d)
    val chatDiv = createChatContent(d)
    val div = d getElementById "tasks-completion"

    val chat = new WebSocket("ws://localhost:8128/")

    global.console.log("chat has been created")

    chat.onopen = { (event: Event) ⇒
      chat.send("chat has been opened")
      global.console.log("chat has been opened")
      val p = d createElement "p"
      p innerHTML = "chat has been opened"
      div.appendChild(p)
      event
    }
    chat.onerror = { (event: ErrorEvent) ⇒
      global.console.log(s"Failed: code: ${event.colno}")
    }
    chat.onmessage = { (event: MessageEvent) ⇒
      global.console.log(s"Got a mess: ${event.data.toString}")
      val p = d createElement "p"
      p innerHTML = event.data.toString
      div.appendChild(p)
    }
    chat.onclose = { (event: Event) ⇒
      global.console.log("Connection to chat lost. You can try to rejoin manually.")
      val p = d createElement "p"
      p innerHTML = "Connection to chat lost. You can try to rejoin manually."
      div.appendChild(p)
    }
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

