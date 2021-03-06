package ftanml.objects

import java.io.Writer
import ftanml.streams.Acceptor

case class FtanNumber(value: Double) extends FtanValue {

  override def send(acceptor: Acceptor) {
    acceptor.processNumber(value)
  }
}

object FtanNumber extends FtanNumber(0)