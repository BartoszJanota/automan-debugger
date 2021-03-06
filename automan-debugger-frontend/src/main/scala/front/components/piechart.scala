package front.components

/**
 * Created by bj on 23.07.15.
 */

import front.components.chartColors._
import japgolly.scalajs.react.ReactComponentC.ReqProps
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all.key
import japgolly.scalajs.react.vdom.svg.all._
import paths.high.Pie

import scala.scalajs.js


object piechart {

  case class StringIntVal(label: String, count: Int)

  private def move(p: js.Array[Double]) = s"translate(${p(0)},${p(1)})"

  private val palette = mix(Color(130, 140, 210), Color(180, 205, 150))

  val PieChart: ReqProps[List[StringIntVal], Unit, Unit, TopNode] = ReactComponentB[List[StringIntVal]]("Pie chart")
    .render(tasks => {
    val pie = Pie[StringIntVal](
      data = tasks,
      accessor = _.count,
      r = 10,
      R = 90,
      center = (0, 0)
    )
    val slices = pie.curves map { curve =>
      g(key := curve.item.label)(
        lineargradient(
          id := s"grad-${curve.index}",
          stop(stopColor := string(palette(curve.index)), offset := "0%"),
          stop(stopColor := string(lighten(palette(curve.index))), offset := "100%")
        ),
        path(d := curve.sector.path.print, fill := s"url(#grad-${curve.index})"),
        text(
          transform := move(curve.sector.centroid),
          textAnchor := "middle",
          fontSize := "10px",
          curve.item.label + " (" + curve.item.count + ")"
        )
      )
    }
    svg(width := 400, height := 200,
      g(transform := move(js.Array(200, 110)), slices)
    )
  })
    .build
}
