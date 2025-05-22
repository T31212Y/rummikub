package de.htwg.se.rummikub.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

import java.io.ByteArrayOutputStream
import java.io.PrintStream

import de.htwg.se.rummikub.model.{run => runMain}

class MainSpec extends AnyWordSpec {
  "Main" should {
    "print the layout" in {
        val output = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(output)) {
          runMain()
        }
        val lines = output.toString.split("\n").map(_.trim.replaceAll("\\s", " "))

        val expectedLayout = Vector(
            "*****Azra************************************************************************",
            "|               |                                               |               |",
            "|               +-----------------------------------------------+               |",
            "|               |                                               |               |",
            "|               +-----------------------------------------------+               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|                                                                               |",
            "|               +-----------------------------------------------+               |",
            "|               |                                               |               |",
            "|               +-----------------------------------------------+               |",
            "|               |                                               |               |",
            "*****Moritz**********************************************************************",
            "\n"
        ).mkString("\n")

        lines.mkString("\n") should be (expectedLayout)
        lines.length should be (28)
    }
    "print an error if game mode creation fails" in {
      val output = new ByteArrayOutputStream()
      Console.withOut(new PrintStream(output)) {

        val gameModeTry = GameModeFactory.createGameMode(1, List("Azra"))
        gameModeTry match {
          case scala.util.Failure(exception) =>
            println(s"Failed to create game mode: ${exception.getMessage}")
          case _ =>
        }
      }
      output.toString should include ("Failed to create game mode")
      output.toString should include ("Invalid number of players")
    }
  }
}