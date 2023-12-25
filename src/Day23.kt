fun main() {
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("Day23")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int {
    val grid = input.map(String::toList)
    val start = input.first().indexOf('.').let { Point(it, 0) }
    val end = input.last().lastIndexOf('.').let { Point(it, input.lastIndex) }
    val seen = Array(grid.size) { BooleanArray(grid.first().size) }
    return dfs(start, end, seen) { point ->
        when (grid[point.y][point.x]) {
            '.' -> point.cardinal
            '^' -> listOf(point + Direction.North)
            '<' -> listOf(point + Direction.West)
            'v' -> listOf(point + Direction.South)
            '>' -> listOf(point + Direction.East)
            else -> error("forsen")
        }.filter { grid.isInBounds(it) && grid[it.y][it.x] != '#' }.map { it to 1 }
    }
}

private fun part2(input: List<String>): Int {
    val grid = input.map(String::toList)
    val start = input.first().indexOf('.').let { Point(it, 0) }
    val end = input.last().lastIndexOf('.').let { Point(it, input.lastIndex) }
    val junctions = mutableMapOf(
        start to mutableListOf<Pair<Point, Int>>(),
        end to mutableListOf(),
    )

    grid.indices.forEach { y ->
        grid[y].indices.forEach { x ->
            val point = Point(x, y)
            if (point.neighbours(grid).size > 2) {
                junctions[point] = mutableListOf()
            }
        }
    }

    junctions.forEach { (junction, path) ->
        val queue = ArrayDeque(listOf(junction.withIndex(1)))
        val seen = mutableSetOf(junction)
        while (queue.isNotEmpty()) {
            val (steps, current) = queue.removeFirst()
            current.neighbours(grid).filter { it !in seen }.forEach { p ->
                if (p in junctions) path += p to steps
                else {
                    queue += p.withIndex(steps + 1)
                    seen += p
                }
            }
        }
    }

    val seen = Array(grid.size) { BooleanArray(grid.first().size) }
    return dfs(start, end, seen) { point ->
        junctions.getValue(point)
    }
}

private fun Point.neighbours(grid: List<List<Char>>) = cardinal.filter { grid.isInBounds(it) && grid[it.y][it.x] != '#' }

private fun dfs(
    current: Point,
    end: Point,
    seen: Array<BooleanArray>,
    steps: Int = 0,
    neighbours: (Point) -> List<Pair<Point, Int>>,
): Int {
    if (current == end) {
        return steps
    }

    seen[current.y][current.x] = true
    val max = neighbours(current)
        .filter { (it, _) -> !seen[it.y][it.x] }
        .maxOfOrNull { (it, weight) -> dfs(it, end, seen, steps + weight, neighbours) }
    seen[current.y][current.x] = false
    return max ?: 0
}
