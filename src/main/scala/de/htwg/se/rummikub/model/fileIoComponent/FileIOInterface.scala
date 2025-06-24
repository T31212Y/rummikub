package de.htwg.se.rummikub.model.fileIoComponent

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}

trait FileIOInterface {
    def to(tokens: List[TokenInterface]): Unit
    def from: List[TokenInterface]
    def convertStringToColor(col: String): Color
}