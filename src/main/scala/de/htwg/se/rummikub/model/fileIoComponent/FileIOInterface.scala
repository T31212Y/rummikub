package de.htwg.se.rummikub.model.fileIoComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait FileIOInterface {
    def to(tokens: List[TokenInterface]): Unit
    def from: List[TokenInterface]
}