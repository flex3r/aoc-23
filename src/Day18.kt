import Direction.East
import Direction.North
import Direction.South
import Direction.West

fun main() {
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62L)
    check(part2(testInput) == 952408144115L)

    val input = readInput("Day18")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>) = input.parse().followPlan().area()

private fun part2(input: List<String>) = input.parse(swapped = true).followPlan().area()

private fun List<String>.parse(swapped: Boolean = false) = map {
    val (dirPart, depthPart, color) = it.split(' ')
    val direction = when (if (swapped) color[7] else dirPart) {
        '3', "U" -> North
        '1', "D" -> South
        '2', "L" -> West
        '0', "R" -> East
        else -> error("forsen")
    }
    val depth = when {
        swapped -> color.substring(2..6).toInt(radix = 16)
        else -> depthPart.toInt()
    }
    direction to depth
}

private fun List<Pair<Direction, Int>>.followPlan() = runningFold(Point(0, 0)) { acc, (dir, depth) -> acc + dir.asPoint() * depth }
