package de.htwg.se.rummikub.model.builderComponent

trait FieldDirectorFactoryInterface {
  def create(amtPlayers: Int): FieldDirectorInterface
}