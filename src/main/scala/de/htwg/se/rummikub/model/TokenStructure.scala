package de.htwg.se.rummikub.model

abstract class TokenStructure(initial: List[String]) {

    var tokens: List[Token] = changeStringListToTokenList(initial)

    def changeStringListToTokenList(list: List[String]): List[Token] = { 
        list.map { tokenString =>
            val tokenParts = tokenString.split(":")
            val tokenFactory = new StandardTokenFactory

            if (tokenParts(0) == "J") {
                tokenParts(1) match {
                    case "red" => tokenFactory.createJoker(Color.RED)
                    case "black" => tokenFactory.createJoker(Color.BLACK)
                }
            } else  {
                tokenParts(1) match {
                    case "red" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.RED)
                    case "blue" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.BLUE)
                    case "green" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.GREEN)
                    case "black" => tokenFactory.createNumToken(tokenParts(0).toInt, Color.BLACK)
                }
            }
        }
    }

    def getTokens: List[Token] = tokens
    def addToken(token: Token): Unit = tokens = tokens :+ token
    def removeToken(token: Token): Unit = tokens = tokens.filterNot(_.equals(token))

    override def toString: String = {
        tokens.map(_.toString).mkString(" ")
    }
}