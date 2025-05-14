package de.htwg.se.rummikub.model

abstract class GameModeTemplate {
    def createPlayers(): List[Player]
    def createPlayingField(players: List[Player]): PlayingField
    def updatePlayingField(playingField: PlayingField): PlayingField
    def updateBoardSinglePlayer(player: Player, board: Board): Board
    def updateBoardMultiPlayer(players: List[Player], board: Board): Option[Board]
    def renderPlayingField(playingField: PlayingField): String
    def lineWithPlayerName(char: String, length: Int, player: String): Option[String]
    def lineWith2PlayerNames(char: String, length: Int, player1: String, player2: String): Option[String]
}