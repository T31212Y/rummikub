package de.htwg.se.rummikub.model.fileIoComponent.fileIoXmlImpl

import de.htwg.se.rummikub.model.fileIoComponent.FileIOInterface
import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, TokenFactoryInterface, Color}
import de.htwg.se.rummikub.RummikubModule

import scala.xml.PrettyPrinter

import com.google.inject.Guice

class FileIoXml extends FileIOInterface {

    override def from: TokenInterface = {
        var token: Option[TokenInterface] = None
        val file = scala.xml.XML.loadFile("token.xml")
        val numAttr = (file \\ "token" \ "number")
        val colAttr = (file \\ "token" \ "color")
        val num = numAttr.text.trim.toInt
        val col = colAttr.text

        val injector = Guice.createInjector(new RummikubModule)
        val tokenFactory = injector.getInstance(classOf[TokenFactoryInterface])
        if (num != 0) {
            token = Some(tokenFactory.createNumToken(num, convertStringToColor(col)))
        } else {
            token = Some(tokenFactory.createJoker(convertStringToColor(col)))
        }

        token.get
    }

    override def to(token: TokenInterface): Unit = {
        import java.io._
        val pw = new PrintWriter(new File("token.xml"))
        val pp = new PrettyPrinter(120, 4)
        val xml = pp.format(tokenToXml(token))
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