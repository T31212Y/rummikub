package de.htwg.se.rummikub.model.fileIoComponent.fileIoXmlImpl

import de.htwg.se.rummikub.model.fileIoComponent.FileIOInterface
import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, TokenFactoryInterface, Color}
import de.htwg.se.rummikub.RummikubModule

import scala.xml.PrettyPrinter

import com.google.inject.Guice

class FileIoXml extends FileIOInterface {

    override def from: List[TokenInterface] = {
        val file = scala.xml.XML.loadFile("tokens.xml")

        val injector = Guice.createInjector(new RummikubModule)
        val tokenFactory = injector.getInstance(classOf[TokenFactoryInterface])

        val tokenElems = file \\ "token"

        tokenElems.map { token =>
            val numAttr = (token \ "number")
            val colAttr = (token \ "color")

            val num = numAttr.text.trim.toInt
            val col = colAttr.text.trim

            if (num != 0) {
                tokenFactory.createNumToken(num, convertStringToColor(col))
            } else {
                tokenFactory.createJoker(convertStringToColor(col))
            }
        }.toList
    }

    override def to(tokens: List[TokenInterface]): Unit = {
        import java.io._
        val pw = new PrintWriter(new File("tokens.xml"))
        val pp = new PrettyPrinter(120, 4)

        val xml = pp.format(tokensToXml(tokens))
        pw.write(xml)
        pw.close
    }

    def tokenToXml(token: TokenInterface) = {
        <token>
            <color>{token.getColor}</color>
            {
                if (token.getNumber.isDefined) {
                    <number>{token.getNumber.get}</number>
                } else {
                    <number>{0}</number>
                }
            }
        </token>
    }

    def tokensToXml(tokens: List[TokenInterface]) = {
        <tokens>
            {tokens.map(tokenToXml)}
        </tokens>
    }

    def convertStringToColor(col: String): Color = {
        var color: Option[Color] = None

        col match {
            case "[31m" => Color.RED
            case "[34m" => Color.BLUE
            case "[32m" => Color.GREEN
            case "[30m" => Color.BLACK
        }
    }
}