package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class PlayingFieldSpec extends AnyWordSpec {
  "A PlayingField" should {
    "be created with the correct number of players" in {
      val gameMode = GameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val players = gameMode.createPlayers()
      val playingField = gameMode.createPlayingField(players)

      playingField.players.size should be(2)
    }

    "have a board for each player" in {
      val gameMode = GameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val players = gameMode.createPlayers()
      val playingField = gameMode.createPlayingField(players)

      playingField.boards.size should be(2)
    }

    "have an inner field" in {
      val gameMode = GameModeFactory.createGameMode(2, List("Emilia", "Noah"))
      val players = gameMode.createPlayers()
      val playingField = gameMode.createPlayingField(players)

      playingField.innerField should not be null
    }
  }
}