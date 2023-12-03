import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

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

inline fun <R : Any> CharSequence.lastNotNullOf(transform: (Char) -> R?): R {
    return lastNotNullOfOrNull(transform) ?: throw NoSuchElementException("No element of the char sequence was transformed to a non-null value.")
}

inline fun <R : Any> CharSequence.lastNotNullOfOrNull(transform: (Char) -> R?): R? {
    for (index in this.indices.reversed()) {
        val element = this[index]
        val result = transform(element)
        if (result != null) {
            return result
        }
    }
    return null
}

fun Iterable<Int>.product(): Int = reduce { acc, i -> acc * i }

inline fun List<String>.sumOfChars(selector: (x: Int, y: Int, c: Char) -> Int): Int {
    return withIndex().sumOf { (y, line) ->
        line.withIndex().sumOf { (x, char) ->
            selector(x, y, char)
        }
    }
}
