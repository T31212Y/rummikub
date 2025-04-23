package de.htwg.se.rummikub.model

case class Table(cntRows: Int, length: Int) {

    val emptyRow = "|" + (" " * length) + "|\n"
    var tokensOnTable = List[List[Token | Joker]]()

    def deleteColorCodes(tableRow: String): String = {
        tableRow.replaceAll("\u001B\\[[;\\d]*m", "")
    }

    override def toString: String = {
        var table = ""
        if (tokensOnTable.isEmpty) {
            table = emptyRow.repeat(cntRows)
        } else {
            val formattedRows = tokensOnTable.map { row =>
                var formattedRow = row.map(_.toString).mkString(" ")
                if (formattedRow.length < length) {
                    val padding = length - deleteColorCodes(formattedRow).length - 1
                    formattedRow = " " + formattedRow + " " * padding
                } 
                "|" + formattedRow + "|"
            }.mkString("\n")

            if (tokensOnTable.size < cntRows) {
                val emptyRows = cntRows - tokensOnTable.size
                table = formattedRows + "\n" + emptyRow.repeat(emptyRows)
            }
        }
        table
    }
}