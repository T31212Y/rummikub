package de.htwg.se.rummikub.model

@main def run(): Unit = 
  val gameMode = GameModeFactory.createGameMode(2, List("Azra", "Moritz"))
  val players = gameMode.createPlayers()
  val playingField = gameMode.createPlayingField(players)
  
  println(gameMode.renderPlayingField(playingField))