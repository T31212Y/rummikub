package de.htwg.se.rummikub.model

case class Group(group: List[String]) {

  var groupTokens: List[Token | Joker] = changeStringListToTokenList(group)

  def changeStringListToTokenList(group: List[String]): List[Token | Joker] = {
    group.map { tokenString =>
      val tokenParts = tokenString.split(":")

      if (tokenParts(0) == "J") {
        tokenParts(1) match {
          case "red" => Joker(Color.RED)
          case "black" => Joker(Color.BLACK)
        }
      } else {
        tokenParts(1) match {
          case "red" => Token(tokenParts(0).toInt, Color.RED)
          case "blue" => Token(tokenParts(0).toInt, Color.BLUE)
          case "green" => Token(tokenParts(0).toInt, Color.GREEN)
          case "black" => Token(tokenParts(0).toInt, Color.BLACK)
        }
      }
    }
  }

  def addToken(token: Token | Joker): Unit = {
    groupTokens = groupTokens :+ token
  }

  def removeToken(token: Token): Unit = {
    groupTokens = groupTokens.filterNot(_.equals(token))
  }

  def getTokens: List[Token | Joker] = groupTokens

  override def toString: String = {
    groupTokens.map(_.toString).mkString(" ")
  }
}