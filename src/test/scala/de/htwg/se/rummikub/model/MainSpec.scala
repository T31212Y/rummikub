package de.htwg.se.rummikub.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

import java.io.ByteArrayOutputStream
import java.io.PrintStream

import de.htwg.se.rummikub.model.{run => runMain}

class MainSpec extends AnyWordSpec {
  "Main" should {
    /*"print the layout" in {
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
    }*/
  }
}