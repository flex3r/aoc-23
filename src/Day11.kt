import kotlin.math.abs

fun main() {
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(part2(testInput, factor = 100) == 8410L)

    val input = readInput("Day11")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Long = solve(input)
private fun part2(input: List<String>, factor: Int = 1_000_000): Long = solve(input, factor)

private fun solve(input: List<String>, factor: Int = 2): Long {
    val galaxies = input.findGalaxies().expand(factor)
    return combinations(galaxies, 2).sumOf { (a, b) -> a.manhatten(b).toLong() }
}

private fun List<Pair<Int, Int>>.expand(factor: Int = 2): List<Pair<Int, Int>> {
    val xOffsets = offsetsBy(Pair<Int, Int>::first)
    val yOffsets = offsetsBy(Pair<Int, Int>::second)
    return map { (x, y) ->
        Pair(
            first = x + (xOffsets[x] * (factor - 1)),
            second = y + (yOffsets[y] * (factor - 1)),
        )
    }
}

private inline fun List<Pair<Int, Int>>.offsetsBy(selector: (Pair<Int, Int>) -> Int): List<Int> {
    val values = map(selector).toSet()
    val start = if (0 in values) 0 else 1
    return (0..values.max()).scan(start) { acc, idx -> acc + if (idx !in values) 1 else 0 }
}

private fun List<String>.findGalaxies() = flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, c ->
        when (c) {
            '#' -> x to y
            else -> null
        }
    }
}

private fun Pair<Int, Int>.manhatten(other: Pair<Int, Int>) = abs(first - other.first) + abs(second - other.second)
