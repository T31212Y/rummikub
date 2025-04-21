package de.htwg.se.rummikub.model

@main def run(): Unit = 
  val sf : PlayingField = new PlayingField(2, List(new Player("Max", TokenStack().drawMultipleTokens(14)), new Player("Moritz", TokenStack().drawMultipleTokens(14))))

  println(sf.toString())