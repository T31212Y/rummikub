package de.htwg.se.rummikub.util

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface

object TokenUtils {
    def tokensMatch(token1: TokenInterface, token2: TokenInterface): Boolean = {
        if(token1.isNumToken && token2.isNumToken) {
            token1.number == token2.number && token1.color == token2.color
        } else if (token1.isJoker && token2.isJoker) {
            true
        } else {
            false
        }
    }
}