package de.htwg.se.rummikub.model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

import java.io.ByteArrayOutputStream
import java.io.PrintStream

import de.htwg.se.rummikub.model.{run => runMain}


class MainSpec extends AnyWordSpec {
  "Main" should {
    "print the logo" in {
        val output = new ByteArrayOutputStream()
        Console.withOut(new PrintStream(output)) {
          runMain()
        }

        val lines = output.toString.split("\n")
        val logoLines = lines.take(5)

        val expectedLogo = Array(
            " ____                                _  _            _      _",
            "|  _ \\  _   _  _ __ ___   _ __ ___  (_)| | __ _   _ | |__  | |",
            "| |_) || | | || '_ ` _ \\ | '_ ` _ \\ | || |/ /| | | || '_ \\ | |",
            "|  _ < | |_| || | | | | || | | | | || ||   < | |_| || |_) ||_|",
            "|_| \\_\\\\___,_||_| |_| |_||_| |_| |_||_||_|\\_\\\\___,_||_.__/ (_)"
        ).mkString("\n")
        
        logoLines.mkString("\n") should be (expectedLogo)
        logoLines.length should be (5)
    }
    "print the layout" in {
        val output = new ByteArrayOutputStream()
        val printStream = new PrintStream(output)
        Console.withOut(printStream) {
          runMain()
        }

        val lines = output
            .toString
            .split("\n")
            .map(_.trim.replaceAll("\\s", " "))
        val layoutLines = lines.drop(6).take(23) 
        printf(layoutLines.mkString("\n"))

        val expectedLayout = Array(
            "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *",
            "|              |_______________________________________________|              |",
            "|              |_______________________________________________|              |",                                                                                                                                                         
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|                                                                             |",
            "|               _______________________________________________               |",                                                                                                                                                         
            "|              |_______________________________________________|              |",                                                                                                                                                          
            "|              |                                               |              |",                                                                                                                                                         
            "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *"
        ).mkString("\n")

        layoutLines.mkString("\n") should be (expectedLayout)
        layoutLines.length should be (23)
    }
  }
}
