package de.htwg.se.rummikub.model

case class TwoPlayerMode(playerNames: List[String]) extends GameModeTemplate(playerNames) {

    override def createPlayingField(players: List[Player]): PlayingField = {
        val builder = new StandardPlayingFieldBuilder
        val director = new TwoPlayerFieldDirector(builder)

        director.construct(players)
    }

    override def updatePlayingField(playingField: PlayingField): PlayingField = {
        val player1 = playingField.players(0)
        val player2 = playingField.players(1)

        val boardP1 = playingField.boards(0)
        val boardP2 = playingField.boards(1)

        val updatedBoards = List(
            updateBoardSinglePlayer(player1, boardP1).fold(boardP1)(identity),
            updateBoardSinglePlayer(player2, boardP2).fold(boardP2)(identity)
        )

        val updatedInnerField = new Table(cntRows - 4, boardP1.size(boardP1.wrapBoardRowSingle(boardP1.boardELRP12_1)) - 2, playingField.innerField.tokensOnTable)

        playingField.copy(boards = updatedBoards, innerField = updatedInnerField)
    }

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

    override def updateBoardMultiPlayer(players: List[Player], board: Board): Option[Board] = None

    override def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): Option[String] = None
}