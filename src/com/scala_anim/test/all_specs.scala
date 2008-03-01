package com.scala_anim.test

import org.specs.runner.ConsoleRunner
import org.specs.Specification

object allSpecsRunner extends ConsoleRunner(allSpecs)

object allSpecs extends Specification {
  "Events and Event Dispatching" isSpecifiedBy (eventSpecs)
  "Canvases" isSpecifiedBy (canvasSpecs)
  "Scene Tree Behaviour" isSpecifiedBy (sceneTreeSpecs)
  "Frame and Time-based Animation" isSpecifiedBy (animationSpecs)
  "Invalidation behaviour" isSpecifiedBy (invalidationSpecs)
  "Geometric utilities" isSpecifiedBy (geomSpecs)
}

