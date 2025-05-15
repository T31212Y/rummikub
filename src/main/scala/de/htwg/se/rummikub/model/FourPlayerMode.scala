package de.htwg.se.rummikub.model

case class FourPlayerMode(playerNames: List[String]) extends GameModeTemplate {

    val cntRows: Int = 20
    val cntTokens: Int = 24
    val cntEdgeSpaces: Int = 15

    override def createPlayers(): List[Player] = {
        playerNames.map(name => Player(name))
    }

    override def createPlayingField(players: List[Player]): PlayingField = {
        val builder = new StandardPlayingFieldBuilder
        val director = new FourPlayerFieldDirector(builder)

        director.construct(players)
    }

    override def updatePlayingField(playingField: PlayingField): PlayingField = {
        val player1 = playingField.players(0)
        val player2 = playingField.players(1)
        val player3 = playingField.players(2)
        val player4 = playingField.players(3)

        val boardP13 = playingField.boards(0)
        val boardP24 = playingField.boards(1)

        val updatedBoards = List(
            updateBoardMultiPlayer(List(player1, player3), boardP13).fold(boardP13)(identity),
            updateBoardMultiPlayer(List(player2, player4), boardP24).fold(boardP24)(identity)
        )

        val updatedInnerField = new Table(cntRows - 4, boardP24.size(boardP24.wrapBoardRowDouble(boardP24.boardELRP12_1, boardP24.boardELRP34_1)) - 2, playingField.innerField.tokensOnTable)

        playingField.copy(boards = updatedBoards, innerField = updatedInnerField)
    }

    override def updateBoardSinglePlayer(player: Player, board: Board): Option[Board] = None

    override def updateBoardMultiPlayer(players: List[Player], board: Board): Option[Board] = {
        if (players(0).tokens.size <= cntTokens) {
            board.boardELRP12_1 = board.formatBoardRow(players(0).tokens)
            board.boardELRP12_2 = board.formatEmptyBoardRow(board.size(board.boardELRP12_1) - 4)
        } else {
            board.boardELRP12_1 = board.formatBoardRow(players(0).tokens.take(cntTokens))
            board.boardELRP12_2 = board.formatBoardRow(players(0).tokens.drop(cntTokens))
        }
     
        if (players(1).tokens.size <= cntTokens) {
            board.boardELRP34_1 = board.formatBoardRow(players(1).tokens)
            board.boardELRP34_2 = board.formatEmptyBoardRow(board.size(board.boardELRP34_1) - 4)
        } else {
            board.boardELRP34_1 = board.formatBoardRow(players(1).tokens.take(cntTokens))
            board.boardELRP34_2 = board.formatBoardRow(players(1).tokens.drop(cntTokens))
        }

        board.boardEUD = board.createBoardFrameDouble(players(0).tokens.take(cntTokens), players(1).tokens.take(cntTokens))
        Some(board)
    }

    override def renderPlayingField(playingField: PlayingField): String = {
        val player1 = playingField.players(0)
        val player2 = playingField.players(1)
        val player3 = playingField.players(2)
        val player4 = playingField.players(3)

        val boardP13 = playingField.boards(0)
        val boardP24 = playingField.boards(1)

        val innerField = playingField.innerField

        val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player1.name, player3.name).fold("")(identity) + "\n"
        val edgeDown = lineWith2PlayerNames("*", boardP24.size(boardP24.wrapBoardRowDouble(boardP24.boardELRP12_1, boardP24.boardELRP34_1)), player2.name, player4.name).fold("")(identity) + "\n"

        s"$edgeUp${boardP13.toString()}${innerField.toString()}${boardP24.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")
    }

    override def lineWithPlayerName(char: String, length: Int, player: String): Option[String] = None

    override def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): Option[String] = {
        val len = length - player1.length - player2.length - 10
        Some(char.repeat(5) + player1 + char * len + player2 + char.repeat(5))
    }
}