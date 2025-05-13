package de.htwg.se.rummikub.model

case class Group(group: List[String]) {

  var groupTokens: List[Token] = changeStringListToTokenList(group)

  def changeStringListToTokenList(group: List[String]): List[Token] = {
    group.map { tokenString =>
      val tokenParts = tokenString.split(":")

      if (tokenParts(0) == "J") {
        tokenParts(1) match {
          case "red" => TokenFactory.createToken("Joker", 0, Color.RED)
          case "black" => TokenFactory.createToken("Joker", 0, Color.BLACK)
        }
      } else {
        tokenParts(1) match {
          case "red" => TokenFactory.createToken("NumToken", tokenParts(0).toInt, Color.RED)
          case "blue" => TokenFactory.createToken("NumToken", tokenParts(0).toInt, Color.BLUE)
          case "green" => TokenFactory.createToken("NumToken", tokenParts(0).toInt, Color.GREEN)
          case "black" => TokenFactory.createToken("NumToken", tokenParts(0).toInt, Color.BLACK)
        }
      }
    }
  }

  def addToken(token: Token): Unit = {
    groupTokens = groupTokens :+ token
  }

  def removeToken(token: Token): Unit = {
    groupTokens = groupTokens.filterNot(_.equals(token))
  }

  def getTokens: List[Token] = groupTokens

  override def toString: String = {
    groupTokens.map(_.toString).mkString(" ")
  }
}