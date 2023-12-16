import Shape.Cubed
import Shape.Empty
import Shape.Round

fun main() {
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("Day14")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>) = parse(input).tilt(Direction.North).sum()

private fun part2(input: List<String>): Int {
    var platform = parse(input).cycle()
    val cache = mutableListOf(platform)
    var currentCycle = 1
    while (true) {
        platform = platform.cycle()
        val seen = cache.indexOfFirst { it == platform }
        if (seen == -1) {
            cache += platform
            currentCycle++
            continue
        }

        val cycleLength = currentCycle - seen
        val rem = 999999999 - currentCycle
        val offset = rem % cycleLength
        return cache[seen + offset].sum()
    }
}

private fun parse(input: List<String>) = input.map { line ->
    line.map { c ->
        when (c) {
            '.' -> Empty
            '#' -> Cubed
            'O' -> Round
            else -> error("forsen")
        }
    }
}

private fun List<List<Shape>>.cycle() = Direction.entries.fold(initial = this) { acc, direction -> acc.tilt(direction) }

private fun List<List<Shape>>.tilt(direction: Direction): List<List<Shape>> {
    val width = first().lastIndex
    val height = lastIndex
    val xRange = if (direction == Direction.East) width downTo 0 else 0..width
    val yRange = if (direction == Direction.South) height downTo 0 else 0..height
    val copy = map { it.toMutableList() }.toMutableList()
    yRange.forEach yLoop@{ y ->
        xRange.forEach xLoop@{ x ->
            if (copy[y][x] != Round) return@xLoop
            val (endX, endY) = copy.roll(x, y, direction)
            copy[y][x] = Empty
            copy[endY][endX] = Round
        }
    }

    return copy
}

private fun List<List<Shape>>.roll(x: Int, y: Int, direction: Direction): Pair<Int, Int> {
    val width = first().lastIndex
    val height = lastIndex
    return when (direction) {
        Direction.West -> (x downTo 1).find { this[y][it - 1] != Empty } ?: 0
        Direction.East -> (x..<width).find { this[y][it + 1] != Empty } ?: width
        else -> x
    } to when (direction) {
        Direction.South -> (y..<height).find { this[it + 1][x] != Empty } ?: height
        Direction.North -> (y downTo 1).find { this[it - 1][x] != Empty } ?: 0
        else -> y
    }
}

private fun List<List<Shape>>.sum() = indices.sumOf { y -> (size - y) * this[y].count { it == Round } }

private enum class Shape { Round, Cubed, Empty }
private enum class Direction { North, West, South, East }
