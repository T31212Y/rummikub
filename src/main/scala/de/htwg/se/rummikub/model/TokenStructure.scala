package de.htwg.se.rummikub.model
import tokenComponent.Token

abstract class TokenStructure(initial: List[Token]) {

    var tokens: List[Token] = initial

    def getTokens: List[Token] = tokens
    def addToken(token: Token): Unit = tokens = tokens :+ token
    def removeToken(token: Token): Unit = tokens = tokens.filterNot(_.equals(token))

    override def toString: String = {
        tokens.map(_.toString).mkString(" ")
    }

    def isValid: Boolean
    def points: Int
}