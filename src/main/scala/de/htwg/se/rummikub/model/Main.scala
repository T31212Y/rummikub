package de.htwg.se.rummikub.model

@main def run(): Unit = 
  val sf : PlayingField = new PlayingField(2, List(new Player("Azra"), new Player("Moritz")))

  println(sf.toString().replace("x", " ").replace("y", " "))