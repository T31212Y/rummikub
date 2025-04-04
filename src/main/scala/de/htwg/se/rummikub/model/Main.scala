package de.htwg.se.rummikub.model

@main def run(): Unit = 
  val sf : PlayingField = new PlayingField()

  println(sf.logo().mkString("\n") + "\n")
  println(sf.pFieldLayout().mkString("\n"))