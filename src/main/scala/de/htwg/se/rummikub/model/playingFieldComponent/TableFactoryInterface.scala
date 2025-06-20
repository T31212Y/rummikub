package de.htwg.se.rummikub.model.playingFieldComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait TableFactoryInterface {
    def createTable(cntRows: Int, length: Int, tokensOnTable: List[List[TokenInterface]]): TableInterface
}