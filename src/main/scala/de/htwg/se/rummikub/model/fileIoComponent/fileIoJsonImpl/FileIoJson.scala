package de.htwg.se.rummikub.model.fileIoComponent.fileIoJsonImpl

import de.htwg.se.rummikub.model.fileIoComponent.FileIOInterface
import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, TokenFactoryInterface, Color}
import de.htwg.se.rummikub.RummikubModule

import play.api.libs.json._
import scala.io.Source

import com.google.inject.Guice

class FileIoJson extends FileIOInterface {
    override def from: List[TokenInterface] = {
        val file = Source.fromFile("tokens.json").getLines.mkString
        Json.parse(file).as[List[TokenInterface]]
    }

    override def to(tokens: List[TokenInterface]): Unit = {
        import java.io._
        val pw = new PrintWriter(new File("tokens.json"))
        pw.write(Json.prettyPrint(Json.toJson(tokens)))
        pw.close
    }

    implicit val tokenWrites: Writes[TokenInterface] = new Writes[TokenInterface] {
        def writes(token: TokenInterface) = Json.obj(
            "color" -> token.getColor.toString,
            "number" -> token.getNumber.getOrElse(0)
        )
    }

    implicit val tokenReads: Reads[TokenInterface] = new Reads[TokenInterface] {
        def reads(json: JsValue) = {
            val injector = Guice.createInjector(new RummikubModule)
            val tokenFactory = injector.getInstance(classOf[TokenFactoryInterface])

            for {
                num <- (json \ "number").validate[Int]
                col <- (json \ "color").validate[String]
            } yield {
                if (num != 0) {
                    tokenFactory.createNumToken(num, convertStringToColor(col))
                } else {
                    tokenFactory.createJoker(convertStringToColor(col))
                }
            }
        }
    }

    override def convertStringToColor(col: String): Color = {
        var color: Option[Color] = None

        col match {
            case "\u001b[31m" => Color.RED
            case "\u001b[34m" => Color.BLUE
            case "\u001b[32m" => Color.GREEN
            case "\u001b[30m" => Color.BLACK
        }
    }
}