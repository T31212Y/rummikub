package de.htwg.se.rummikub.model.builderComponent

import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldFactoryInterface

trait BuilderFactoryInterface {
  def createBuilder(factory: PlayingFieldFactoryInterface): PlayingFieldBuilderInterface
}