package de.htwg.se.rummikub.model.playerComponent

import de.htwg.se.rummikub.model._

trait PlayerInterface {
    def getName: String
    def getTokens: List[Token]
    def getCommandHistory: List[String]
    def getFirstMoveTokens: List[Token]
    def getHasCompletedFirstMove: Boolean

    override def toString: String

    def validateFirstMove: Boolean
    def addToFirstMoveTokens(newTokens: List[Token]): PlayerInterface
    def deepCopy: PlayerInterface

    def clusterTokens(tokens: List[Token]): List[TokenStructure]

    def updated(newTokens: List[Token], newCommandHistory: List[String], newHasCompletedFirstMove: Boolean): PlayerInterface
}