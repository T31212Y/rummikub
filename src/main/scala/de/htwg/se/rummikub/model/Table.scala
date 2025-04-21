package de.htwg.se.rummikub.model

case class Table(var tokens: List[List[Token| Joker]]) {
    def addToken(token: Token | Joker): Unit = {
        tokens = tokens :+ List(token)
    }
    
    def removeToken(token: Token | Joker): Unit = {
        tokens = tokens.filterNot(_.contains(token))
    }
    
    def getTokens: List[List[Token| Joker]] = tokens
    
    override def toString: String = {
        tokens.map(_.mkString(", ")).mkString("\n")
    }


}
