package de.htwg.se.rummikub.util

import de.htwg.se.rummikub.model._

object TokenUtils {
    def tokensMatch(token1: Token, token2: Token): Boolean = (token1, token2) match {
        case (NumToken(n1, c1), NumToken(n2, c2)) => n1 == n2 && c1 == c2
        case (_: Joker, _: Joker) => true
        case _ => false
    }
}