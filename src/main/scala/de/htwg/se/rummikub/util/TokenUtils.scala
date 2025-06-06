package de.htwg.se.rummikub.util

import de.htwg.se.rummikub.model._

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.Joker

object TokenUtils {
    def tokensMatch(token1: TokenInterface, token2: TokenInterface): Boolean = (token1, token2) match {
        case (NumToken(n1, c1), NumToken(n2, c2)) => n1 == n2 && c1 == c2
        case (_: Joker, _: Joker) => true
        case _ => false
    }
}