package de.htwg.se.rummikub.model.fileIoComponent.fileIoXmlImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{NumToken, Joker, StandardTokenFactory}
import de.htwg.se.rummikub.model.tokenComponent.Color

class FileIoXmlSpec extends AnyWordSpec {
  "FileIoXml" should {
    val fileIo = new FileIoXml
    val tokenFactory = new StandardTokenFactory

    "write and read tokens correctly" in {
      val tokens = List(
        tokenFactory.createNumToken(5, Color.RED),
        tokenFactory.createNumToken(7, Color.BLUE),
        tokenFactory.createJoker(Color.GREEN)
      )
      fileIo.to(tokens)
      val loaded = fileIo.from
      loaded.size shouldBe 3
      loaded.head.getNumber shouldBe Some(5)
      loaded.head.getColor shouldBe Color.RED
      loaded(1).getNumber shouldBe Some(7)
      loaded(1).getColor shouldBe Color.BLUE
      loaded(2).getNumber shouldBe None
      loaded(2).getColor shouldBe Color.GREEN
    }

    "convert string to color correctly" in {
      fileIo.convertStringToColor("[31m") shouldBe Color.RED
      fileIo.convertStringToColor("[34m") shouldBe Color.BLUE
      fileIo.convertStringToColor("[32m") shouldBe Color.GREEN
      fileIo.convertStringToColor("[30m") shouldBe Color.BLACK
    }
  }
}

