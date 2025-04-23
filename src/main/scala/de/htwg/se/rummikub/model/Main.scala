package de.htwg.se.rummikub.model

@main def run(): Unit = 
  val sf : PlayingField = new PlayingField(2, List(new Player("Max"), new Player("Moritz")))

  println(sf.toString())