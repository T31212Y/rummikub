package de.htwg.se.rummikub.model

case class ThreePlayerMode(playerNames: List[String]) extends GameModeTemplate(playerNames) {

    override def createPlayingField(players: List[Player]): PlayingField = {
        val builder = new StandardPlayingFieldBuilder
        val director = new ThreePlayerFieldDirector(builder)

        director.construct(players)
    }

    override def updatePlayingField(playingField: PlayingField): PlayingField = {
        val player1 = playingField.players(0)
        val player2 = playingField.players(1)
        val player3 = playingField.players(2)

        val boardP13 = playingField.boards(0)
        val boardP2 = playingField.boards(1)

        val updatedBoardP13 = updateBoardMultiPlayer(List(player1, player3), boardP13).fold(boardP13)(identity)
        val updatedBoardP2 = updateBoardSinglePlayer(player2, boardP2.copy(maxLen = 116)).fold(boardP2)(identity)

        val updatedBoards = List(
            updatedBoardP13,
            updatedBoardP2
        )

        val updatedInnerField = new Table(cntRows - 4, boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)) - 2, playingField.innerField.tokensOnTable)

        playingField.copy(boards = updatedBoards, innerField = updatedInnerField)
    }

    override def renderPlayingField(playingField: PlayingField): String = {
        val player1 = playingField.players(0)
        val player2 = playingField.players(1)
        val player3 = playingField.players(2)

        val boardP13 = playingField.boards(0)
        val boardP2 = playingField.boards(1)

        val innerField = playingField.innerField

        val edgeUp = lineWith2PlayerNames("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player1.name, player3.name).fold("")(identity) + "\n"
        val edgeDown = lineWithPlayerName("*", boardP13.size(boardP13.wrapBoardRowDouble(boardP13.boardELRP12_1, boardP13.boardELRP34_1)), player2.name).fold("")(identity) + "\n"

        s"$edgeUp${boardP13.toString()}${innerField.toString()}${boardP2.toString()}$edgeDown\n".replace("x", " ").replace("y", " ")
    }
}