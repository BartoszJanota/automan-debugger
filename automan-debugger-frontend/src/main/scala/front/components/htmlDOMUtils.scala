package front.components

import org.scalajs.dom.raw.{Element, HTMLDocument}

/**
 * Created by bj on 20.08.15.
 */
trait htmlDOMUtils extends stateColors{
  def createRow(d: HTMLDocument): Element = {
    val row = d.createElement("div")
    row.setAttribute("class", "row left-margin")
    row
  }

  def removeAllElementChildren(el: Element) = {
    while (el.firstChild != null) {
      el.removeChild(el.firstChild)
    }
  }

  def createTableHeaders(tab: Element, d: HTMLDocument) = {
    val tr = d createElement "tr"
    tr appendChild createTh("Task Id", 1, d)
    tr appendChild createTh("Last 60 seconds", 60, d)
    tab appendChild tr
  }

  def createTh(text: String, colspan: Int, d: HTMLDocument) = {
    val th = d createElement "th"
    th.setAttribute("style", "font-size: 9px")
    th.setAttribute("colspan", colspan.toString)
    th.innerHTML = text
    th
  }

  def createTd(state: (Double, String), d: HTMLDocument) = {
    val td = d createElement "td"
    td.setAttribute("width", "1.5%")
    td.setAttribute("style", "font-size: 10px")
    td.setAttribute("bgcolor", stateColorPalette(state._2))
    td
  }

  def renderTimelineLegend(d: HTMLDocument) = {
    val legend = d getElementById "timeline-legend"
    val span = d createElement "span"
    span.setAttribute("style", "font-size:9px;margin-right: 4px")
    span.innerHTML = "Task States:"
    legend appendChild span
    stateColorPalette.foreach { color =>
      val span = d createElement "span"
      span.setAttribute("style", "color:" + color._2 + ";font-size:9px;margin-right: 4px")
      span.innerHTML = color._1
      legend appendChild span
    }
  }
}
