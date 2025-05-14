package de.htwg.se.rummikub.model

case class TwoPlayerMode(playerNames: List[String]) extends GameModeTemplate {

    val cntRows: Int = 20
    val cntTokens: Int = 24
    val cntEdgeSpaces: Int = 15

    override def createPlayers(): List[Player] = {
        playerNames.map(name => Player(name))
    }

    override def createPlayingField(players: List[Player]): PlayingField = {

        val boardP1 = new Board(cntEdgeSpaces, cntTokens, 2, 1, "up")
        val boardP2 = new Board(cntEdgeSpaces, cntTokens, 2, 1, "down")

        val innerField = new Table(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2)

        PlayingField(players, List(boardP1, boardP2), innerField)
    }

    override def updatePlayingField(playingField: PlayingField): PlayingField = {
        val player1 = playingField.players(0)
        val player2 = playingField.players(1)

        val boardP1 = playingField.boards(0)
        val boardP2 = playingField.boards(1)

        val updatedBoards = List(
            updateBoardSinglePlayer(player1, boardP1),
            updateBoardSinglePlayer(player2, boardP2)
        )

        val updatedInnerField = new Table(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2, playingField.innerField.tokensOnTable)

        playingField.copy(boards = updatedBoards, innerField = updatedInnerField)
    }

    override def updateBoardSinglePlayer(player: Player, board: Board): Board = {
        if (player.tokens.size <= cntTokens) {
          board.boardELRP12_1 = board.formatBoardRow(player.tokens)
          board.boardELRP12_2 = board.formatEmptyBoardRow(board.size(board.boardELRP12_1) - 4)
          board.boardEUD = board.createBoardFrameSingle(player.tokens)
        } else {
          board.boardELRP12_1 = board.formatBoardRow(player.tokens.take(cntTokens))
          board.boardELRP12_2 = board.formatBoardRow(player.tokens.drop(cntTokens))
          board.boardEUD = board.createBoardFrameSingle(player.tokens.take(cntTokens))
        }
        board
    }

    override def updateBoardMultiPlayer(players: List[Player], board: Board): Option[Board] = None

    override def renderPlayingField(playingField: PlayingField): String = {
        val player1 = playingField.players(0)
        val player2 = playingField.players(1)

        val boardP1 = playingField.boards(0)
        val boardP2 = playingField.boards(1)

        val innerField = playingField.innerField

        val edgeUp = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player1.name).fold("")(identity) + "\n"
        val edgeDown = lineWithPlayerName("*", boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)), player2.name).fold("")(identity) + "\n"

        s"$edgeUp${boardP1.toString()}${innerField.toString()}${boardP2.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")
    }

    override def lineWithPlayerName(char: String, length: Int, player: String): Option[String] = {
        val len = length - player.length - 5
        Some(char.repeat(5) + player + char * len)
    }

    override def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): Option[String] = None
}