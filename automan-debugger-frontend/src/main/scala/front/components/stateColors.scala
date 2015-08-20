package front.components

import org.scalajs.dom.ext.Color

/**
 * Created by bj on 20.08.15.
 */
trait stateColors {
  val stateColorPalette: Map[String, String] =
    Map("READY" -> getHex(Color.Blue),
      "RUNNING" -> getHex(Color.Green),
      "ANSWERED" -> getHex(Color.White),
      "DUPLICATE" -> getHex(Color.Cyan),
      "ACCEPTED" -> getHex(Color.Magenta),
      "REJECTED" -> getHex(Color.Yellow),
      "TIMEOUT" -> getHex(Color.Red),
      "CANCELLED" -> getHex(Color.Black)
    )

  def getHex(c: Color): String = {
    String.format("#%02x%02x%02x", new Integer(c.r), new Integer(c.g), new Integer(c.b))
  }
}
