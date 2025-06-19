package de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.GameModeFactory
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.TwoPlayerMode
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.ThreePlayerMode
import de.htwg.se.rummikub.model.gameModeComponent.gameModeBaseImpl.FourPlayerMode
import scala.util.{Success, Failure}

import de.htwg.se.rummikub.RummikubDependencyModule.given

class GameModeFactorySpec extends AnyWordSpec {

  "GameModeFactory" should {

    "create a TwoPlayerMode for 2 players" in {
      val playerNames = List("Alice", "Bob")
      val mode = gameModeFactory.createGameMode(2, playerNames)

      mode shouldBe a [Success[TwoPlayerMode]]
      mode.get shouldBe a [TwoPlayerMode]
      mode.get.asInstanceOf[TwoPlayerMode].playerNames shouldBe playerNames
    }

    "create a ThreePlayerMode for 3 players" in {
      val playerNames = List("Alice", "Bob", "Charlie")
      val mode = gameModeFactory.createGameMode(3, playerNames)

      mode shouldBe a [Success[ThreePlayerMode]]
      mode.get shouldBe a [ThreePlayerMode]
      mode.get.asInstanceOf[ThreePlayerMode].playerNames shouldBe playerNames
    }

    "create a FourPlayerMode for 4 players" in {
      val playerNames = List("Alice", "Bob", "Charlie", "Diana")
      val mode = gameModeFactory.createGameMode(4, playerNames)

      mode shouldBe a [Success[FourPlayerMode]]
      mode.get shouldBe a [FourPlayerMode]
      mode.get.asInstanceOf[FourPlayerMode].playerNames shouldBe playerNames
    }

    "throw a MatchError for unsupported number of players" in {
      val playerNames = List("Alice")
      val mode = gameModeFactory.createGameMode(1, playerNames)

      mode shouldBe a [Failure[Nothing]]
      mode.failed.get shouldBe an [IllegalArgumentException]
    }
  }
}
