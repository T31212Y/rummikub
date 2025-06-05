package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory

class PlayingFieldSpec extends AnyWordSpec {
  "A PlayingField" should {
    "be created with the correct number of players" in {
      val gameModeFactory = new GameModeFactory
      val gameModeTry = gameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val gameMode = gameModeTry.get
      val players = gameMode.createPlayers
      val playingField = gameMode.createPlayingField(players).get

      playingField.getPlayers.size should be(2)
    }

    "have a board for each player" in {
      val gameModeFactory = new GameModeFactory
      val gameModeTry = gameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val gameMode = gameModeTry.get
      val players = gameMode.createPlayers
      val playingField = gameMode.createPlayingField(players).get

      playingField.getBoards.size should be(2)
    }

    "have an inner field" in {
      val gameModeFactory = new GameModeFactory
      val gameModeTry = gameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val gameMode = gameModeTry.get
      val players = gameMode.createPlayers
      val playingField = gameMode.createPlayingField(players)

      playingField.get.getInnerField should not be null
    }
  }
}