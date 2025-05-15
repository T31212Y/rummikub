package de.htwg.se.rummikub.model

case class Group(group: List[String]) {

  var groupTokens: List[Token] = changeStringListToTokenList(group)

  def changeStringListToTokenList(group: List[String]): List[Token] = {
    group.map { tokenString =>
      val tokenParts = tokenString.split(":")
      val tokenFactory = new StandardTokenFactory

      if (tokenParts(0) == "J") {
        tokenParts(1) match {
          case "red" => tokenFactory.createJoker(Color.RED)
          case "black" => tokenFactory.createJoker(Color.BLACK)
        }
      } else {
        tokenParts(1) match {
          case "red" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.RED)
          case "blue" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.BLUE)
          case "green" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.GREEN)
          case "black" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.BLACK)
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