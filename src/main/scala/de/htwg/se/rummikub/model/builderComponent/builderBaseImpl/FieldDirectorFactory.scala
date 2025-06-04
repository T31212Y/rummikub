package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, FieldDirectorInterface, FieldDirectorFactoryInterface}
import de.htwg.se.rummikub.model.playingfieldComponent.{BoardFactoryInterface, TableFactoryInterface, TokenStackFactoryInterface}


class FieldDirectorFactory(
  builder: PlayingFieldBuilderInterface,
  boardFactory: BoardFactoryInterface,
  tableFactory: TableFactoryInterface,
  stackFactory: TokenStackFactoryInterface
) extends FieldDirectorFactoryInterface {

  override def create(amtPlayers: Int): FieldDirectorInterface = amtPlayers match {
    case 2 => new TwoPlayerFieldDirector(builder, boardFactory, tableFactory, stackFactory)
    case 3 => new ThreePlayerFieldDirector(builder, boardFactory, tableFactory, stackFactory)
    case 4 => new FourPlayerFieldDirector(builder, boardFactory, tableFactory, stackFactory)
    case _ => throw new IllegalArgumentException(s"Unsupported number of players: $amtPlayers")
  }
}