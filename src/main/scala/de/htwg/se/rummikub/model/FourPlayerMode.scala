package de.htwg.se.rummikub.model

case class FourPlayerMode(playerNames: List[String]) extends GameModeTemplate(playerNames) {

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

    override def updateBoardSinglePlayer(player: Player, board: Board): Option[Board] = None

    override def lineWithPlayerName(char: String, length: Int, player: String): Option[String] = None
}