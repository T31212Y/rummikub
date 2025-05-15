package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class GameModeFactorySpec extends AnyWordSpec {

  "GameModeFactory" should {

    "create a TwoPlayerMode for 2 players" in {
      val playerNames = List("Alice", "Bob")
      val mode = GameModeFactory.createGameMode(2, playerNames)

      mode shouldBe a [TwoPlayerMode]
      mode.asInstanceOf[TwoPlayerMode].playerNames shouldBe playerNames
    }

    "create a ThreePlayerMode for 3 players" in {
      val playerNames = List("Alice", "Bob", "Charlie")
      val mode = GameModeFactory.createGameMode(3, playerNames)

      mode shouldBe a [ThreePlayerMode]
      mode.asInstanceOf[ThreePlayerMode].playerNames shouldBe playerNames
    }

    "create a FourPlayerMode for 4 players" in {
      val playerNames = List("Alice", "Bob", "Charlie", "Diana")
      val mode = GameModeFactory.createGameMode(4, playerNames)

      mode shouldBe a [FourPlayerMode]
      mode.asInstanceOf[FourPlayerMode].playerNames shouldBe playerNames
    }

    "throw a MatchError for unsupported number of players" in {
      val playerNames = List("Alice")
      assertThrows[MatchError] {
        GameModeFactory.createGameMode(1, playerNames)
      }
    }
  }
}
