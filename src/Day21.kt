import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.get

fun main() {
    val testInput = readInput("Day21_test")
    check(part1(testInput, steps = 6) == 16)

    val input = readInput("Day21")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>, steps: Int = 64) = parse(input).let { (grid, start) -> grid.walk(start, steps) }

private fun part2(input: List<String>, steps: Int = 26501365): Long {
    val (grid, start) = parse(input, expansion = 5)
    val target = (steps - 65) / 131L
    val y0 = grid.walk(start, steps = 65)
    val y1 = grid.walk(start, steps = 65 + 131)
    val y2 = grid.walk(start, steps = 65 + 131 * 2)
    val y = mk.ndarray(listOf(y0, y1, y2))
    val vandermonde = mk.ndarray(listOf(listOf(1, 0, 0), listOf(1, 1, 1), listOf(1, 2, 4)))
    val x = mk.linalg.solve(vandermonde, y).asType<Long>()
    return x[2] * target * target + x[1] * target + x[0]
}

private fun List<List<Char>>.walk(start: Point, steps: Int): Int {
    val queue = ArrayDeque(listOf(start.withIndex(index = 0)))
    val seen = mutableSetOf<Point>()
    while (queue.isNotEmpty()) {
        val (step, current) = queue.removeFirst()
        if (step > steps || !seen.add(current)) continue
        current.cardinal
            .filter { isInBounds(it) && get(it.y)[it.x] != '#' }
            .forEach {
                queue += it.withIndex(index = step + 1)
            }
    }
    return seen.filter { (x, y) -> (x + y) % 2 == steps % 2 }.size
}

private fun parse(input: List<String>, expansion: Int = 1): Pair<List<List<Char>>, Point> {
    val grid = (0..<expansion)
        .flatMap { _ -> input.map { line -> line.repeat(expansion) } }
        .map(String::toList)
    val start = Point(grid.size / 2, grid.size / 2)
    return grid to start
}
