package de.htwg.se.rummikub.model.playingFieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.util.TokenUtils.tokensMatch

import de.htwg.se.rummikub.model.tokenComponent.TokenInterface
import de.htwg.se.rummikub.model.playingFieldComponent.TableInterface

case class Table(cntRows: Int, length: Int, tokensOnTable: List[List[TokenInterface]] = List()) extends TableInterface {

    val emptyRow = "|" + (" " * length) + "|\n"

    override def getCntRows: Int = cntRows
    override def getLength: Int = length
    override def getTokensOnTable: List[List[TokenInterface]] = tokensOnTable

    override def deleteColorCodes(tableRow: String): String = {
        tableRow.replaceAll("\u001B\\[[;\\d]*m", "")
    }

    override def add(e: List[TokenInterface]): TableInterface = this.copy(tokensOnTable = this.tokensOnTable :+ e)

    override def remove(tokensToRemove: List[TokenInterface]): TableInterface = {
        val removalBuffer = scala.collection.mutable.ListBuffer.from(tokensToRemove)

        val updatedTokensOnTable = tokensOnTable.map { row =>
            row.foldLeft(List.empty[TokenInterface]) { (acc, token) =>
                val indexOpt = removalBuffer.indexWhere(t => tokensMatch(t, token))

                if (indexOpt != -1) {
                    removalBuffer.remove(indexOpt)
                    acc
                } else {
                    acc :+ token
                }
            }
        }.filter(_.nonEmpty)

        this.copy(tokensOnTable = updatedTokensOnTable)
    }

    override def getRow(index: Int): Option[List[TokenInterface]] = {
        tokensOnTable.lift(index)
    }

    override def getGroup(index: Int): Option[List[TokenInterface]] = {
        tokensOnTable.lift(index)
    }

    override def toString: String = {
        if (tokensOnTable.isEmpty) {
            emptyRow.repeat(cntRows)
        } else {
            val formattedRows = tokensOnTable.map { row =>
                val rawRow = row.map(_.toString).mkString(" ")
                val rawRowLength = deleteColorCodes(rawRow).length
                val padding = math.max(0, length - rawRowLength - 1)
                val formattedRow = " " + rawRow + " " * padding
                "|" + formattedRow + "|"
            }.mkString("\n")

            if (tokensOnTable.size < cntRows) {
                val emptyRows = cntRows - tokensOnTable.size
                formattedRows + "\n" + emptyRow.repeat(emptyRows)
            } else {
                formattedRows
            }
        }
    }

    override def updated(newTokensOnTable: List[List[TokenInterface]]): TableInterface = {
        copy(tokensOnTable = newTokensOnTable)
    }
}