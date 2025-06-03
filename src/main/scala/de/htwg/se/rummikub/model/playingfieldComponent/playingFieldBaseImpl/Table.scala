package de.htwg.se.rummikub.model.playingfieldComponent.playingFieldBaseImpl

import de.htwg.se.rummikub.model.playingfieldComponent.TableInterface
import de.htwg.se.rummikub.model.tokenStructureComponent.TokenStructureInterface

case class Table(cntRows: Int, length: Int, tokensOnTable: List[List[TokenStructureInterface]] = List()) extends TableInterface {

  val emptyRow = "|" + (" " * length) + "|\n"

  def deleteColorCodes(tableRow: String): String =
    tableRow.replaceAll("\u001B\\[[;\\d]*m", "")

  def add(e: List[TokenStructureInterface]): TableInterface =
    this.copy(tokensOnTable = this.tokensOnTable :+ e)

  def remove(tokensToRemove: List[TokenStructureInterface]): TableInterface = {
    val removalBuffer = scala.collection.mutable.ListBuffer.from(tokensToRemove)

    val updatedTokensOnTable = tokensOnTable.map { row =>
      row.foldLeft(List.empty[TokenStructureInterface]) { (acc, token) =>
        val indexOpt = removalBuffer.indexWhere(t => t.getTokens == token.getTokens)

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

  def getRow(index: Int): Option[List[TokenStructureInterface]] =
    tokensOnTable.lift(index)

  def updateRow(index: Int, newTokens: List[TokenStructureInterface]): TableInterface = {
    val updated =
      if (index < tokensOnTable.length) {
        tokensOnTable.updated(index, newTokens)
      } else {
        tokensOnTable ++ List.fill(index - tokensOnTable.length)(List.empty[TokenStructureInterface]) :+ newTokens
      }
    this.copy(tokensOnTable = updated)
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
}
