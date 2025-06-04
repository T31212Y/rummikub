package de.htwg.se.rummikub.model.tokenStructureComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait TokenStructureInterface {
    def getTokens: List[TokenInterface]
    def addToken(token: TokenInterface): Unit
    def removeToken(token: TokenInterface): Unit

    override def toString: String

    def isValid: Boolean
    def points: Int
}