import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.pow
import kotlin.time.measureTime

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

fun Int.pow(n: Int) = toDouble().pow(n).toInt()

inline fun <T> Collection<T>.partitionBy(predicate: (T) -> Boolean): List<List<T>> {
    return fold(mutableListOf(mutableListOf<T>())) { acc, it ->
        when {
            predicate(it) -> acc.add(mutableListOf())
            else -> acc.last() += it
        }
        acc
    }
}

inline fun <T> measureAndPrintResult(crossinline block: () -> T) {
    measureTime {
        println(block())
    }.also { println("Took $it") }
}

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

fun lcm(a: Long, b: Long) = a * b / gcd(a, b)
fun gcd(a: Long, b: Long): Long {
    var num1 = a
    var num2 = b

    while (num2 != 0L) {
        val temp = num2
        num2 = num1 % num2
        num1 = temp
    }

    return num1
}
