package de.htwg.se.rummikub.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class PlayingFieldSpec extends AnyWordSpec {
  "A PlayingField" should {
    "initialize correctly with 2 players" in {
      val player1 = new Player("Emilia")
      val player2 = new Player("Noah")
      val playingField = new PlayingField(2, List(player1, player2))

      playingField.amountOfPlayers should be(2)
      playingField.players should contain allOf (player1, player2)
      playingField.innerField2Players should not be null
      playingField.innerField34Players should not be null
    }

    "initialize correctly with 3 players" in {
      val player1 = new Player("Emilia")
      val player2 = new Player("Noah")
      val player3 = new Player("Sophia")
      val playingField = new PlayingField(3, List(player1, player2, player3))

      playingField.amountOfPlayers should be(3)
      playingField.players should contain allOf (player1, player2, player3)
      playingField.innerField34Players should not be null
    }

    "initialize correctly with 4 players" in {
      val player1 = new Player("Emilia")
      val player2 = new Player("Noah")
      val player3 = new Player("Sophia")
      val player4 = new Player("Liam")
      val playingField = new PlayingField(4, List(player1, player2, player3, player4))

      playingField.amountOfPlayers should be(4)
      playingField.players should contain allOf (player1, player2, player3, player4)
      playingField.innerField34Players should not be null
    }

    "update the playing field correctly for 2 players" in {
      val player1 = new Player("Emilia", List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken",2, Color.BLUE)))
      val player2 = new Player("Noah", List(TokenFactory.createToken("NumToken", 3, Color.GREEN), TokenFactory.createToken("NumToken", 4, Color.BLACK)))
      val playingField = new PlayingField(2, List(player1, player2))
      val updatedField = playingField.updatePlayingField()

      updatedField.amountOfPlayers should be(2)
      updatedField.innerField2Players should not be null
    }

    "update the playing field correctly for 2 players with more than 24 tokens on board" in {
      var stack = TokenStack()
      val player1 = new Player("Emilia", stack.drawMultipleTokens(25))
      val player2 = new Player("Noah", stack.drawMultipleTokens(25))
      val playingField = new PlayingField(2, List(player1, player2))
      val updatedField = playingField.updatePlayingField()

      updatedField.amountOfPlayers should be(2)
      updatedField.innerField2Players should not be null
    }

    "update the playing field correctly for 3 players" in {
      val player1 = new Player("Emilia", List(TokenFactory.createToken("NumToken",1, Color.RED), TokenFactory.createToken("NumToken", 2, Color.BLUE)))
      val player2 = new Player("Noah", List(TokenFactory.createToken("NumToken", 3, Color.GREEN), TokenFactory.createToken("NumToken", 4, Color.BLACK)))
      val player3 = new Player("Sophia", List(TokenFactory.createToken("NumToken", 5, Color.RED), TokenFactory.createToken("NumToken", 6, Color.BLUE)))
      val playingField = new PlayingField(3, List(player1, player2, player3))
      val updatedField = playingField.updatePlayingField()

      updatedField.amountOfPlayers should be(3)
      updatedField.innerField34Players should not be null
    }

    "update the playing field correctly for 3 players with more than 24 tokens on board" in {
      var stack = TokenStack()
      val player1 = new Player("Emilia", stack.drawMultipleTokens(25))
      val player2 = new Player("Noah", stack.drawMultipleTokens(25))
      val player3 = new Player("Sophia", stack.drawMultipleTokens(25))
      val playingField = new PlayingField(3, List(player1, player2, player3))
      val updatedField = playingField.updatePlayingField()

      updatedField.amountOfPlayers should be(3)
      updatedField.innerField34Players should not be null
    }

    "update the playing field correctly for 4 players" in {
      val player1 = new Player("Emilia", List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 2, Color.BLUE)))
      val player2 = new Player("Noah", List(TokenFactory.createToken("NumToken", 3, Color.GREEN), TokenFactory.createToken("NumToken", 4, Color.BLACK)))
      val player3 = new Player("Sophia", List(TokenFactory.createToken("NumToken", 5, Color.RED), TokenFactory.createToken("NumToken", 6, Color.BLUE)))
      val player4 = new Player("Liam", List(TokenFactory.createToken("NumToken", 7, Color.GREEN), TokenFactory.createToken("NumToken", 8, Color.BLACK)))
      val playingField = new PlayingField(4, List(player1, player2, player3, player4))
      val updatedField = playingField.updatePlayingField()

      updatedField.amountOfPlayers should be(4)
      updatedField.innerField34Players should not be null
    }

    "update the playing field correctly for 4 players with more than 24 tokens on board" in {
      var stack = TokenStack()
      val player1 = new Player("Emilia", stack.drawMultipleTokens(25))
      val player2 = new Player("Noah", stack.drawMultipleTokens(25))
      val player3 = new Player("Sophia", stack.drawMultipleTokens(25))
      val player4 = new Player("Liam", stack.drawMultipleTokens(25))
      val playingField = new PlayingField(4, List(player1, player2, player3, player4))
      val updatedField = playingField.updatePlayingField()

      updatedField.amountOfPlayers should be(4)
      updatedField.innerField34Players should not be null
    }

    "generate the correct string representation for 2 players" in {
      val player1 = new Player("Emilia")
      val player2 = new Player("Noah")
      val playingField = new PlayingField(2, List(player1, player2))

      val playingFieldString = playingField.toString
      playingFieldString should include("Emilia")
      playingFieldString should include("Noah")
    }

    "generate the correct string representation for 3 players" in {
      val player1 = new Player("Emilia")
      val player2 = new Player("Noah")
      val player3 = new Player("Sophia")
      val playingField = new PlayingField(3, List(player1, player2, player3))

      val playingFieldString = playingField.toString
      playingFieldString should include("Emilia")
      playingFieldString should include("Noah")
      playingFieldString should include("Sophia")
    }

    "generate the correct string representation for 4 players" in {
      val player1 = new Player("Emilia")
      val player2 = new Player("Noah")
      val player3 = new Player("Sophia")
      val player4 = new Player("Liam")
      val playingField = new PlayingField(4, List(player1, player2, player3, player4))

      val playingFieldString = playingField.toString
      playingFieldString should include("Emilia")
      playingFieldString should include("Noah")
      playingFieldString should include("Sophia")
      playingFieldString should include("Liam")
    }

    "create a simple line" in {
      val player1 = new Player("Emilia")
      val player2 = new Player("Noah")
      val playingField = new PlayingField(2, List(player1, player2))

      val line = playingField.simpleLine("-", 10)
      line should be ("----------")
    }
  }
}