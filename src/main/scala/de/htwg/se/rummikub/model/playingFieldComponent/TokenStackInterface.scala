package de.htwg.se.rummikub.model.playingFieldComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait TokenStackInterface {
    def getTokens: List[TokenInterface]

    def drawToken: (TokenInterface, TokenStackInterface)
    def drawMultipleTokens(n: Int): (List[TokenInterface], TokenStackInterface)
    def removeToken(token: TokenInterface): TokenStackInterface
    def removeMultipleTokens(n: Int): (List[TokenInterface], TokenStackInterface)
    def removeAllTokens: TokenStackInterface
    def isEmpty: Boolean
    def size: Int

    override def toString: String
}