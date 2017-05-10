package ktx.box2d

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World

/**
 * [World] factory function.
 * @param gravity world's gravity applied to bodies on each step. Defaults to no gravity (0f, 0f).
 * @param allowSleep if true, inactive bodies will not be simulated. Improves performance. Defaults to true.
 * @return a new [World] instance with given parameters.
 */
fun createWorld(gravity: Vector2 = Vector2.Zero, allowSleep: Boolean = true) = World(gravity, allowSleep)

/**
 * Type-safe [Body] building DSl.
 * @param init inlined. Invoked on a [BodyDefinition] instance, which provides access to [Body] properties, as well as
 *    fixture building DSL.
 * @return a fully constructed [Body] instance with all defined fixtures.
 * @see BodyDefinition
 */
inline fun World.body(init: BodyDefinition.() -> Unit): Body {
  val bodyDefinition = BodyDefinition()
  bodyDefinition.init()
  val body = createBody(bodyDefinition)
  val dispose = bodyDefinition.disposeOfShapes
  for (fixture in bodyDefinition.fixtureDefinitions) {
    body.createFixture(fixture)
    if (dispose) fixture.shape.dispose()
  }
  return body
}

/**
 * Roughly matches Earth gravity of 9.80665 m/s^2. Moves bodies down on the Y axis.
 *
 * Note that [Vector2] class is mutable, so this vector can be modified. Use this property in read-only mode.
 *
 * Usage example:
 * val world = createWorld(gravity = earthGravity)
 * @see createWorld
 */
val earthGravity = Vector2(0f, -9.8f)
