package de.htwg.se.rummikub.model.playerComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

trait PlayerInterface {
    def getName: String
    def getTokens: List[TokenInterface]
    def getCommandHistory: List[String]
    def getFirstMoveTokens: List[TokenInterface]
    def getHasCompletedFirstMove: Boolean

    override def toString: String

    def validateFirstMove: Boolean
    def addToFirstMoveTokens(newTokens: List[TokenInterface]): PlayerInterface
    def deepCopy: PlayerInterface

    def clusterTokens(tokens: List[TokenInterface]): List[TokenStructureInterface]

    def updated(newTokens: List[TokenInterface], newCommandHistory: List[String], newHasCompletedFirstMove: Boolean): PlayerInterface
}