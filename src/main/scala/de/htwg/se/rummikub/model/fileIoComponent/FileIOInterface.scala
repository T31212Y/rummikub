package de.htwg.se.rummikub.model.fileIoComponent

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

trait FileIOInterface {
    def to(token: TokenInterface): Unit
    def from: TokenInterface
}