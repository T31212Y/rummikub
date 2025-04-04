package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class PlayingFieldSpec extends AnyWordSpec {
  "PlayingField" should {
    "have a logo" in {
      val playingField = new PlayingField()
      playingField.logo().mkString("\n") should be (
        Array(
        " ____                                _  _            _      _",
        "|  _ \\  _   _  _ __ ___   _ __ ___  (_)| | __ _   _ | |__  | |",
        "| |_) || | | || '_ ` _ \\ | '_ ` _ \\ | || |/ /| | | || '_ \\ | |",
        "|  _ < | |_| || | | | | || | | | | || ||   < | |_| || |_) ||_|",
        "|_| \\_\\\\___,_||_| |_| |_||_| |_| |_||_||_|\\_\\\\___,_||_.__/ (_)"
        ).mkString("\n")
      )
    }

    "have a layout" in {
      val playingField = new PlayingField()
      playingField.pFieldLayout().mkString("\n") should be (
        Array(
        "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ",
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
        "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * "
        ).mkString("\n")
      )
    }

    "have a correct number of rows and columns" in {
      val playingField = new PlayingField()
      playingField.cntRows should be(20)
      playingField.cntCols should be(40)
    }
    
    "have a correct number of spaces" in {
      val playingField = new PlayingField()
      playingField.cntSpaces should be(24)
    }

    "have a correct edgeUpDown" in {
      val playingField = new PlayingField()
      playingField.edgeUpDown should have size 1
    }

    "have a correct boardP1" in {
      val playingField = new PlayingField()
      playingField.boardP1 should have size 2
    }

    "have a correct innerField" in {
      val playingField = new PlayingField()
      playingField.innerField should have size (playingField.cntRows - 4)
    }

    "have a correct boardEUP2" in {
      val playingField = new PlayingField()
      playingField.boardEUP2 should have size 1
    }

    "have a correct boardUP2" in {
      val playingField = new PlayingField()
      playingField.boardUP2 should have size 1
    }

    "have a correct boardDP2" in {
      val playingField = new PlayingField()
      playingField.boardDP2 should have size 1
    }
  }
}