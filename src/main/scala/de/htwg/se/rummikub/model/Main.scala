package de.htwg.se.rummikub.model

@main def run(): Unit = 
  val gameModeTry = GameModeFactory.createGameMode(2, List("Azra", "Moritz"))
  gameModeTry match
    case scala.util.Success(gameMode) =>
      val players = gameMode.createPlayers()
      val playingField = gameMode.createPlayingField(players)
      println(gameMode.renderPlayingField(playingField))
    case scala.util.Failure(exception) =>
      println(s"Failed to create game mode: ${exception.getMessage}")