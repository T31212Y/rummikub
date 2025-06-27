package de.htwg.se.rummikub.model.fileIoComponent.fileIoCsvImpl

import de.htwg.se.rummikub.model.fileIoComponent.FileIOInterface
import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, TokenFactoryInterface, Color}
import de.htwg.se.rummikub.RummikubModule

import scala.io.Source

import com.google.inject.Guice

class FileIoCsv extends FileIOInterface {

    override def from: List[TokenInterface] = {
        val file = Source.fromFile("tokens.csv").getLines.drop(1).toList
        
        val injector = Guice.createInjector(new RummikubModule)
        val tokenFactory = injector.getInstance(classOf[TokenFactoryInterface])

        file.map { line =>
            val Array(colAttr, numAttr) = line.split(";")
            val num = numAttr.trim.toInt
            val col = colAttr.trim

            if (num != 0) {
                tokenFactory.createNumToken(num, convertStringToColor(col))
            } else {
                tokenFactory.createJoker(convertStringToColor(col))
            }
        }
    }

    override def to(tokens: List[TokenInterface]): Unit = {
        import java.io._
        val pw = new PrintWriter(new File("tokens.csv"))
        pw.println("color;number")
        tokens.foreach { token =>
            val color = token.getColor.toString.drop(1)
            val number = token.getNumber.getOrElse(0)
            pw.println(s"$color;$number")
        }
        pw.close
    }

    override def convertStringToColor(col: String): Color = {
        col match {
            case "[31m" => Color.RED
            case "[34m" => Color.BLUE
            case "[32m" => Color.GREEN
            case "[30m" => Color.BLACK
        }
    }
}