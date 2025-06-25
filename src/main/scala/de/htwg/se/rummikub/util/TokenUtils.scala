package de.htwg.se.rummikub.util

import de.htwg.se.rummikub.model._

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.NumToken
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.Joker
import de.htwg.se.rummikub.model.tokenStructureComponent.{TokenStructureFactoryInterface, TokenStructureInterface}


object TokenUtils {
    def tokensMatch(token1: TokenInterface, token2: TokenInterface): Boolean = (token1, token2) match {
        case (NumToken(n1, c1), NumToken(n2, c2)) => n1 == n2 && c1 == c2
        case (_: Joker, _: Joker) => true
        case _ => false
    }

    def isSortedAndContinuous(tokens: List[TokenInterface]): Boolean = {
        val numbers = tokens.collect { case t if t.isNumToken => t.getNumber.get }
        if (numbers.isEmpty) return false
        val sorted = numbers.sorted
        sorted == numbers && sorted.sliding(2).forall {
            case List(a, b) => b == a + 1
            case _ => true
        }
    }

    def isSortedAndContinuousWithJoker(tokens: List[TokenInterface]): Boolean = {
        val (jokers, nonJokers) = tokens.partition(_.isJoker)
        val numbers = nonJokers.flatMap(_.getNumber).distinct.sorted

        val totalLength = tokens.size

        (1 to 13).exists { start =>
            val sequence = LazyList.iterate(start)(_ % 13 + 1).take(totalLength).toList
            val missing = sequence.diff(numbers)
            missing.size <= jokers.size
        }
    }



    def isValidTable(
        table: List[List[TokenInterface]],
        tokenStructureFactory: TokenStructureFactoryInterface
    ): Boolean = {
        table.filter(_.nonEmpty).forall { row =>
            val group = tokenStructureFactory.createGroup(row)
            val sequence = tokenStructureFactory.createRow(row)
            group.isValid || (sequence.isValid && isSortedAndContinuousWithJoker(row))
        }
    }
}