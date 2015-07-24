package front.components

/**
 * Created by bj on 23.07.15.
 */

import japgolly.scalajs.react.ReactComponentC.ReqProps

import scala.scalajs.js
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all.key
import japgolly.scalajs.react.vdom.svg.all._
import paths.high.Pie

import colors._


object piechart {
  case class TaskAnswer(answer: String, count: Int)

  private def move(p: js.Array[Double]) = s"translate(${ p(0) },${ p(1) })"
  private val palette = mix(Color(130, 140, 210), Color(180, 205, 150))

  val PieChart: ReqProps[List[TaskAnswer], Unit, Unit, TopNode] = ReactComponentB[List[TaskAnswer]]("Pie chart")
    .render(tasks => {
    println(tasks)
    val pie = Pie[TaskAnswer](
      data = tasks,
      accessor = _.count,
      r = 10,
      R = 80,
      center = (0, 0)
    )
    println("rendering")
    val slices = pie.curves map { curve =>
      g(key := curve.item.answer)(
        lineargradient(
          id := s"grad-${ curve.index }",
          stop(stopColor := string(palette(curve.index)), offset := "10%"),
          stop(stopColor := string(lighten(palette(curve.index))), offset := "100%")
        ),
        path(d := curve.sector.path.print, fill := s"url(#grad-${ curve.index })"),
        text(
          transform := move(curve.sector.centroid),
          textAnchor := "middle",
          fontSize := "small",
          curve.item.answer + " (" + curve.item.count + ")"
        )
      )
    }

    svg(width := 400, height := 200,
      g(transform := move(js.Array(200, 110)), slices)
    )
  })
    .build
}
