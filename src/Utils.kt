import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.ArrayDeque
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

fun <T> bfs(start: T, neighbors: (T) -> List<T>) = sequence {
    val queue = ArrayDeque(listOf(start.withIndex(index = 0)))
    val seen = mutableSetOf(start)
    while (queue.isNotEmpty()) {
        val (index, current) = queue.removeFirst().also { yield(it) }
        neighbors(current).forEach { neighbor ->
            if (seen.add(neighbor)) {
                queue.add(neighbor.withIndex(index = index + 1))
            }
        }
    }
}

fun <T> T.withIndex(index: Int) = IndexedValue(index, value = this)

fun <T> combinations(values: List<T>, m: Int) = sequence {
    val n = values.size
    val result = MutableList(m) { values[0] }
    val stack = Stack<Int>()
    stack.push(0)
    while (stack.isNotEmpty()) {
        var resIndex = stack.lastIndex
        var arrIndex = stack.pop()

        while (arrIndex < n) {
            result[resIndex++] = values[arrIndex++]
            stack.push(arrIndex)

            if (resIndex == m) {
                yield(result.toList())
                break
            }
        }
    }
}

fun List<String>.transposeLines(): List<String> = this[0].indices.map { x -> indices.map { y -> this[y][x] }.joinToString(separator = "") }
