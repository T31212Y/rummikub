package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class PlayingFieldSpec extends AnyWordSpec {
  "A PlayingField" should {
    "be created with the correct number of players" in {
      val gameModeTry = GameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val gameMode = gameModeTry.get
      val players = gameMode.createPlayers()
      val playingField = gameMode.createPlayingField(players).get

      playingField.players.size should be(2)
    }

    "have a board for each player" in {
      val gameModeTry = GameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val gameMode = gameModeTry.get
      val players = gameMode.createPlayers()
      val playingField = gameMode.createPlayingField(players).get

      playingField.boards.size should be(2)
    }

    "have an inner field" in {
      val gameModeTry = GameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val gameMode = gameModeTry.get
      val players = gameMode.createPlayers()
      val playingField = gameMode.createPlayingField(players)

      playingField.get.innerField should not be null
    }
  }
}