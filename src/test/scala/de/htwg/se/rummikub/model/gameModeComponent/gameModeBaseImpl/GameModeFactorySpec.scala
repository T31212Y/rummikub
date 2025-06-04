package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

import scala.util.{Success, Failure}
import org.scalactic.Fail

class GameModeFactorySpec extends AnyWordSpec {

  "GameModeFactory" should {

    "create a TwoPlayerMode for 2 players" in {
      val playerNames = List("Alice", "Bob")
      val mode = GameModeFactory.createGameMode(2, playerNames)

      mode shouldBe a [Success[TwoPlayerMode]]
      mode.get shouldBe a [TwoPlayerMode]
      mode.get.asInstanceOf[TwoPlayerMode].playerNames shouldBe playerNames
    }

    "create a ThreePlayerMode for 3 players" in {
      val playerNames = List("Alice", "Bob", "Charlie")
      val mode = GameModeFactory.createGameMode(3, playerNames)

      mode shouldBe a [Success[ThreePlayerMode]]
      mode.get shouldBe a [ThreePlayerMode]
      mode.get.asInstanceOf[ThreePlayerMode].playerNames shouldBe playerNames
    }

    "create a FourPlayerMode for 4 players" in {
      val playerNames = List("Alice", "Bob", "Charlie", "Diana")
      val mode = GameModeFactory.createGameMode(4, playerNames)

      mode shouldBe a [Success[FourPlayerMode]]
      mode.get shouldBe a [FourPlayerMode]
      mode.get.asInstanceOf[FourPlayerMode].playerNames shouldBe playerNames
    }

    "throw a MatchError for unsupported number of players" in {
      val playerNames = List("Alice")
      val mode = GameModeFactory.createGameMode(1, playerNames)

      mode shouldBe a [Failure[Nothing]]
      mode.failed.get shouldBe an [IllegalArgumentException]
    }
  }
}
