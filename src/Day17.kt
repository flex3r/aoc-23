import java.util.PriorityQueue

fun main() {
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 102)
    check(part2(testInput) == 94)

    val input = readInput("Day17")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>) = dijkstra(
    grid = input.map { it.map(Char::digitToInt) },
    start = listOf(
        Step(Point(0, 0), Direction.East, prevStepsInDir = 0),
        Step(Point(0, 0), Direction.South, prevStepsInDir = 0),
    ),
)
private fun part2(input: List<String>) = dijkstra(
    grid = input.map { it.map(Char::digitToInt) },
    start = listOf(
        Step(Point(0, 0), Direction.East, prevStepsInDir = 0),
        Step(Point(0, 0), Direction.South, prevStepsInDir = 0),
    ),
    limits = 4..10,
)

private fun dijkstra(grid: List<List<Int>>, start: List<Step>, limits: IntRange = 0..3): Int {
    val end = Point(grid.first().lastIndex, grid.lastIndex)
    val best = mutableMapOf<Step, Int>().withDefault { Int.MAX_VALUE }
    val queue = PriorityQueue<Cost>()

    start.forEach {
        best[it] = 0
        queue += it.toCost(0)
    }

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        if (current.step.pos == end) {
            return current.cost
        }

        current.step.move(limits)
            .filter { grid.isInBounds(it.pos) }
            .forEach {
                val new = current.cost + grid[it.pos.y][it.pos.x]
                if  (new < best.getValue(it)) {
                    best[it] = new
                    queue += it.toCost(new)
                }
            }
    }

    error("forsen")
}

data class Step(val pos: Point, val direction: Direction, val prevStepsInDir: Int) {
    fun move(limits: IntRange) = buildList {
        if (prevStepsInDir < limits.first) {
            add(copy(pos = pos + direction, prevStepsInDir = prevStepsInDir + 1))
            return@buildList
        }

        val left = Direction.entries[(direction.ordinal + 1).mod(Direction.entries.size)]
        val right = Direction.entries[(direction.ordinal - 1).mod(Direction.entries.size)]
        add(copy(pos = pos + left, direction = left, prevStepsInDir = 1))
        add(copy(pos = pos + right, direction = right, prevStepsInDir = 1))

        if (prevStepsInDir < limits.last) {
            add(copy(pos = pos + direction, prevStepsInDir = prevStepsInDir + 1))
        }
    }

    fun toCost(value: Int) = Cost(this, value)
}

data class Cost(val step: Step, val cost: Int): Comparable<Cost> {
    override fun compareTo(other: Cost): Int = cost.compareTo(other.cost)
}
