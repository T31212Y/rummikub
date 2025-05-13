package de.htwg.se.rummikub.model
 
case class Row(row: List[String]) {

    var rowTokens: List[Token] = changeStringListToTokenList(row)

    def changeStringListToTokenList(row: List[String]): List[Token] = {
        row.map { tokenString =>
            val tokenParts = tokenString.split(":")

            if (tokenParts(0) == "J") {
                tokenParts(1) match {
                    case "red" => TokenFactory.createToken("Joker", 0, Color.RED)
                    case "black" => TokenFactory.createToken("Joker", 0, Color.BLACK)
                }
            } else  {
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