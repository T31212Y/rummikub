package de.htwg.se.rummikub.model.playingFieldComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait TableInterface {
    def getCntRows: Int
    def getLength: Int
    def getTokensOnTable: List[List[TokenInterface]]

    def deleteColorCodes(tableRow: String): String
    def add(e: List[TokenInterface]): TableInterface
    def remove(tokensToRemove: List[TokenInterface]): TableInterface

    def getRow(index: Int): Option[List[TokenInterface]]
    def getGroup(index: Int): Option[List[TokenInterface]]

    override def toString: String

    def updated(newTokensOnTable: List[List[TokenInterface]]): TableInterface
}