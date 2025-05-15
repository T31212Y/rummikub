package de.htwg.se.rummikub.model
 
case class Row(row: List[String]) {

    var rowTokens: List[Token] = changeStringListToTokenList(row)

    def changeStringListToTokenList(row: List[String]): List[Token] = {
        row.map { tokenString =>
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

    def addToken(token: Token): Unit = {
        rowTokens = rowTokens :+ token
    }
    
    def removeToken(token: Token): Unit = {
        rowTokens = rowTokens.filterNot(_.equals(token))
    }
    
    def getTokens: List[Token] = rowTokens
    
    override def toString: String = {
        rowTokens.map(_.toString).mkString(" ")
    }
}