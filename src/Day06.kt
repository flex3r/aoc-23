import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int {
    val (times, distances) = parse(input)
    return (times zip distances).fold(1) { acc, (time, distance) ->
        acc * calculate(time, distance) // count(time, distance)
    }
}

private fun part2(input: List<String>): Int {
    val (times, distances) = parse(input)
    val time = times.joinToString(separator = "").toLong()
    val distance = distances.joinToString(separator = "").toLong()
    return calculate(time, distance) // count(time, distance)
}

private fun calculate(time: Long, distance: Long): Int {
    // x * (time - x) = distance
    // -x^2 + time * x - distance = 0
    val root = sqrt(time * time - 4.0 * distance)
    val lower = floor((-time + root) / -2).toInt()
    val upper = ceil((-time - root) / -2).toInt()
    return upper - lower - 1
}

private fun count(time: Long, distance: Long) = (1..time).count { remaining ->
    remaining * (time - remaining) > distance
}

private fun parse(input: List<String>): Pair<List<Long>, List<Long>> {
    val times = input[0].split(" ").mapNotNull(String::toLongOrNull)
    val distances = input[1].split(" ").mapNotNull(String::toLongOrNull)
    return times to distances
}
