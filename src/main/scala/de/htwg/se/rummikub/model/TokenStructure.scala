package de.htwg.se.rummikub.model

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

abstract class TokenStructure(initial: List[TokenInterface]) {

    var tokens: List[TokenInterface] = initial

    def getTokens: List[TokenInterface] = tokens
    def addToken(token: TokenInterface): Unit = tokens = tokens :+ token
    def removeToken(token: TokenInterface): Unit = tokens = tokens.filterNot(_.equals(token))

    override def toString: String = {
        tokens.map(_.toString).mkString(" ")
    }

    def isValid: Boolean
    def points: Int
}