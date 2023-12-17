import java.awt.geom.Point2D
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()
fun readInputAsString(name: String) = Path("src/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

data class Coordinate2D(var x: Int, var y: Int): Point2D() {
    override fun getX(): kotlin.Double = x.toDouble()

    override fun getY(): kotlin.Double = y.toDouble()

    override fun setLocation(x: kotlin.Double, y: kotlin.Double) {
        this.x = x.toInt()
        this.y = y.toInt()
    }

    fun plus(other: Coordinate2D): Coordinate2D {
        return Coordinate2D(this.x + other.x, this.y + other.y)
    }
}