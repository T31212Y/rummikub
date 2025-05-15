package de.htwg.se.rummikub.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.rummikub.model._
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class ControllerSpec extends AnyWordSpec with Matchers {
    "A Controller" should {

        "detect win if player has no tokens" in {
            val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")))
            controller.winGame() should be(true)
        }

        "not detect win when no player won yet" in {
            val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")))
            controller.winGame() should be(false)
        }

        "pass turn to next player" in {
            val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")))
            val currentPlayer = controller.playingField.players.head
            val nextPlayer = controller.passTurn(currentPlayer)
            nextPlayer.name should be("Noah")
        }

        /*"set a new playing field" in {
            val initialField = new PlayingField(2, List(new Player("Emilia"), new Player("Noah")))
            val updatedField = new PlayingField(3, List(new Player("Liam"), new Player("Sophia"), new Player("Mia")))
            val controller = new Controller(initialField)
            controller.setPlayingField(updatedField)
            controller.playingField.amountOfPlayers should be(3)
            controller.playingField.players.map(_.name) should contain allOf("Liam", "Sophia", "Mia")
        }*/

        "convert the playing field to a string" in {
            val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")))
            controller.playingfieldToString should include("Emilia")
            controller.playingfieldToString should include("Noah")
        }

        "add a token to a player" in {
            val stack = TokenStack()
            val player1 = new Player("Emilia")
            val player2 = new Player("Noah")
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
            controller.addTokenToPlayer(player1, stack)
            controller.playingField.players.head.tokens.size should be(1)
            stack.tokens.size should be(105)
        }

        "remove a token from a player" in {
            val tokenFactory = new StandardTokenFactory
            val token = tokenFactory.createNumToken(1, Color.RED)
            val player1 = new Player("Emilia", List(token))
            val player2 = new Player("Noah")
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
            controller.removeTokenFromPlayer(player1, token)
            controller.playingField.players.head.tokens should not contain token
        }

        "add multiple tokens to a player" in {
            val stack = TokenStack()
            val player1 = new Player("Emilia")
            val player2 = new Player("Noah")
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
            controller.addMultipleTokensToPlayer(player1, stack, 2)
            controller.playingField.players.head.tokens.size should be(2)
            stack.tokens.size should be(104)
        }

        /*"add a row to the table" in {
            val row = Row(List("1:red", "2:red", "3:red"))
            val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")))
            val addedTokens = controller.addRowToTable(row)
            addedTokens should contain allOf(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 2, Color.RED), TokenFactory.createToken("NumToken", 3, Color.RED))
            controller.playingField.innerField2Players.tokensOnTable should contain (List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 2, Color.RED), TokenFactory.createToken("NumToken", 3, Color.RED)))
        }

        "add a row to the table for more than 2 players" in {
            val row = Row(List("1:red", "2:red", "3:red"))
            val controller = new Controller(GameModeFactory.createGameMode(3, List("Emilia", "Noah", "Sophia")))
            val addedTokens = controller.addRowToTable(row)
            addedTokens should contain allOf(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 2, Color.RED), TokenFactory.createToken("NumToken", 3, Color.RED))
            controller.playingField.innerField34Players.tokensOnTable should contain (List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 2, Color.RED), TokenFactory.createToken("NumToken", 3, Color.RED)))
        }

        "add a group to the table" in {
            val group = Group(List("1:red", "1:blue", "1:black"))
            val controller = new Controller(GameModeFactory.createGameMode(2, List("Emilia", "Noah")))
            val addedTokens = controller.addGroupToTable(group)
            addedTokens should contain allOf(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 1, Color.BLUE),TokenFactory.createToken("NumToken", 1, Color.BLACK))
            controller.playingField.innerField2Players.tokensOnTable should contain (List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 1, Color.BLUE), TokenFactory.createToken("NumToken", 1, Color.BLACK)))
        }

        "add a group to the table for more than 2 players" in {
            val group = Group(List("1:red", "1:blue", "1:black"))
            val controller = new Controller(GameModeFactory.createGameMode(3, List("Emilia", "Noah", "Sophia")))
            val addedTokens = controller.addGroupToTable(group)
            addedTokens should contain allOf(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 1, Color.BLUE), TokenFactory.createToken("NumToken", 1, Color.BLACK))
            controller.playingField.innerField34Players.tokensOnTable should contain (List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 1, Color.BLUE), TokenFactory.createToken("NumToken", 1, Color.BLACK)))
        }*/

        "process game input for 'draw'" in {
            val stack = TokenStack()
            val player1 = new Player("Emilia")
            val player2 = new Player("Noah")
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
            val nextPlayer = controller.processGameInput("draw", player1, stack)
            nextPlayer.name should be(player2.name)
            controller.playingField.players.head.tokens.size should be(1)
            stack.tokens.size should be(105)
        }

        "process game input for 'pass'" in {
            val tokenFactory = new StandardTokenFactory
            val player = new Player("Emilia", List(tokenFactory.createNumToken(1, Color.RED)), List("group", "row"))
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player.name, "Noah")))
            val nextPlayer = controller.processGameInput("pass", player, TokenStack())
            nextPlayer.name should be("Noah")
            
        }
        "process game input for invalid 'pass'" in {
            val stack = TokenStack()
            val player1 = new Player("Emilia")
            val player2 = new Player("Noah")
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))

            val outStream = new ByteArrayOutputStream()
            Console.withOut(new PrintStream(outStream)) {
                val nextPlayer = controller.processGameInput("pass", player1, stack)
                nextPlayer.name should be(player1.name)
            }
            outStream.toString should include("You cannot pass your turn without playing a token.")
        }
        /*"process game input for 'group'" in {
            val in = new ByteArrayInputStream("1:red, 1:blue, 1:black\n".getBytes)
            Console.withIn(in) {
                val stack = TokenStack()
                val player1 = new Player("Emilia", List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 1, Color.BLUE), TokenFactory.createToken("NumToken", 1, Color.BLACK)))
                val player2 = new Player("Noah")
                val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
                controller.processGameInput("group", player1, stack)
                controller.playingField.innerField2Players.tokensOnTable should contain(List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 1, Color.BLUE), TokenFactory.createToken("NumToken", 1, Color.BLACK)))
            }
        }
        "process game input for 'row'" in {
            val in = new ByteArrayInputStream("1:red, 2:red, 3:red\n".getBytes)
                Console.withIn(in) {
                val stack = TokenStack()
                val player1 = new Player("Emilia", List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 2, Color.RED), TokenFactory.createToken("NumToken", 3, Color.RED)))
                val player2 = new Player("Noah")
                val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
                controller.processGameInput("row", player1, stack)
                controller.playingField.innerField2Players.tokensOnTable should contain (List(TokenFactory.createToken("NumToken", 1, Color.RED), TokenFactory.createToken("NumToken", 2, Color.RED), TokenFactory.createToken("NumToken", 3, Color.RED)))
            }
        }*/

        "process invalid game input" in {
            val stack = TokenStack()
            val player1 = new Player("Emilia")
            val player2 = new Player("Noah")
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
            val nextPlayer = controller.processGameInput("invalid", player1, stack)
            nextPlayer.name should be(player1.name) 
        }

        "setup a new game" in {
            val controller = new Controller(GameModeFactory.createGameMode(2, List("Anki", "Imma")))
            controller.setupNewGame(2, List("Emilia", "Noah"))
            controller.gameMode should be (GameModeFactory.createGameMode(2, List("Emilia", "Noah")))
            controller.playingField.players.map(_.name) should be (List("Emilia", "Noah"))
        }

        "set the next player" in {
            val player1 = new Player("Emilia")
            val player2 = new Player("Noah")
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
            val nextPlayer = controller.setNextPlayer(player1)
            nextPlayer.name should be("Noah")
        }

        "set the first player after reaching the last player in the Players List" in {
            val player1 = new Player("Emilia")
            val player2 = new Player("Noah")
            val controller = new Controller(GameModeFactory.createGameMode(2, List(player1.name, player2.name)))
            val nextPlayer = controller.setNextPlayer(player2)
            nextPlayer.name should be("Emilia")
        }
    }
}
