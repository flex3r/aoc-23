import Direction.East
import Direction.North
import Direction.South
import Direction.West

fun main() {
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>) = solve(input.map(String::toList))
private fun part2(input: List<String>): Int {
    val grid = input.map(String::toList)
    val lastX = grid.first().lastIndex
    val lastY = grid.lastIndex
    return listOf(
        grid.indices.map { Point(0, it) to East },
        grid.indices.map { Point(lastX, it) to West },
        grid.first().indices.map { Point(it, 0) to South },
        grid.first().indices.map { Point(it, lastY) to North },
    ).flatten().maxOf { solve(grid, it.first, it.second) }
}

private fun solve(
    grid: List<List<Char>>,
    start: Point = Point(0, 0),
    startDir: Direction = East,
) = bfs(start to startDir) { (p, dir) -> p.travel(grid, dir) }
    .map { it.value.first }
    .distinct()
    .count()

private fun Point.travel(grid: List<List<Char>>, direction: Direction): List<Pair<Point, Direction>> {
    return when (grid[y][x] to direction) {
        '-' to North -> listOf(East, West)
        '-' to South -> listOf(East, West)
        '|' to East -> listOf(North, South)
        '|' to West -> listOf(North, South)
        '\\' to North -> listOf(West)
        '\\' to South -> listOf(East)
        '\\' to East -> listOf(South)
        '\\' to West -> listOf(North)
        '/' to North -> listOf(East)
        '/' to South -> listOf(West)
        '/' to East -> listOf(North)
        '/' to West -> listOf(South)
        else -> listOf(direction)
    }.map { this + it to it }.filter { grid.isInBounds(it.first) }
}
