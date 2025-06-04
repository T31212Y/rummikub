package de.htwg.se.rummikub.model.builderComponent.builderBaseImpl

import de.htwg.se.rummikub.model.builderComponent.{PlayingFieldBuilderInterface, BuilderFactoryInterface}
import de.htwg.se.rummikub.model.playingfieldComponent.PlayingFieldFactoryInterface

class StandardBuilderFactory extends BuilderFactoryInterface {

  override def createBuilder(factory: PlayingFieldFactoryInterface): PlayingFieldBuilderInterface = {
    new StandardPlayingFieldBuilder(factory)
  }
}