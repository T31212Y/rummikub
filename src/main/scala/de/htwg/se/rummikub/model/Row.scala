package de.htwg.se.rummikub.model
 
case class Row(row: List[String]) {

    var rowTokens: List[Token | Joker] = changeStringListToTokenList(row)

    def changeStringListToTokenList(row: List[String]): List[Token | Joker] = {
        row.map { tokenString =>
            val tokenParts = tokenString.split(":")

            if (tokenParts(0) == "J") {
                tokenParts(1) match {
                    case "red" => Joker(Color.RED)
                    case "blue" => Joker(Color.BLUE)
                    case "green" => Joker(Color.GREEN)
                    case "black" => Joker(Color.BLACK)
                }
            } else  {
                tokenParts(1) match {
                    case "red" => Token(tokenParts(0).toInt, Color.RED)
                    case "blue" => Token(tokenParts(0).toInt, Color.BLUE)
                    case "green" => Token(tokenParts(0).toInt, Color.GREEN)
                    case "black" => Token(tokenParts(0).toInt, Color.BLACK)
                }
            }
        }
    }

    def isValid(): Boolean = {
        if (rowTokens.isEmpty || rowTokens.size < 3) {
            return false
        }

        val firstToken = rowTokens.head
        val isSameColor = rowTokens.forall {
            case token: Token => token.color == firstToken.asInstanceOf[Token].color
            case joker: Joker => joker.color == firstToken.asInstanceOf[Joker].color
        }
              
        isSameColor
    }

    def addToken(token: Token | Joker): Unit = {
        rowTokens = rowTokens :+ token
    }
    
    def removeToken(token: Token| Joker): Unit = {
        rowTokens = rowTokens.filterNot(_.equals(token))
    }
    
    def getTokens: List[Token| Joker] = rowTokens
    
    override def toString: String = {
        rowTokens.map(_.toString).mkString(" ")
    }
}