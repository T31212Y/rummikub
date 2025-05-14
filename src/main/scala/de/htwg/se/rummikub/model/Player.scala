package de.htwg.se.rummikub.model

case class Player(name: String, tokens: List[Token] = List(), commandHistory: List[String] = List()) {
  override def toString: String = {
    s"Player: $name"
  }

  def validateFirstMove(): Boolean = {
    val totalPoints = commandHistory.collect {
      case command if command.startsWith("row:") || command.startsWith("group:") =>
        val tokens = command.split(":")(1).split(",").map(_.trim)
        tokens.collect {
          case tokenString if tokenString.contains(":") =>
            val parts = tokenString.split(":")
            if (parts(0).forall(_.isDigit)) parts(0).toInt else 0
        }.sum
    }.sum

    if (totalPoints < 30) {
      println(s"$name's first move must have a total of at least 30 points.")
      false
    } else {
      true
    }
  }
}