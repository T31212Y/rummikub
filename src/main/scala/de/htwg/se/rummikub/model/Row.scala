package de.htwg.se.rummikub.model
 
case class Row(var tokens: List[Token| Joker]) {
    def addToken(token: Token| Joker): Unit = {
        tokens = tokens :+ token
    }
    
    def removeToken(token: Token| Joker): Unit = {
        tokens = tokens.filterNot(_.equals(token))
    }
    
    def getTokens: List[Token| Joker] = tokens
    
    override def toString: String = {
        tokens.map(_.toString).mkString(", ")
    }
}