fun main() {
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 7)

    val input = readInput("Day22")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int {
    val bricks = parse(input)
    val (_, supported) = simulate(bricks)
    val safe = supported.values
        .filter { it.size == 1 }
        .reduce(Set<Int>::plus)
        .size
    return bricks.size - safe
}

private fun part2(input: List<String>): Int {
    val bricks = parse(input)
    val (supporting, supported) = simulate(bricks)
    return bricks.sumOf { brick ->
        val falling = mutableSetOf(brick.idx)
        val queue = ArrayDeque(supporting[brick.idx].orEmpty())
        while(queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if ((supported.getValue(next) - falling).isEmpty()) {
                falling += next
                queue.addAll(supporting[next].orEmpty())
            }
        }

        falling.size - 1
    }
}

private fun simulate(bricks: List<Brick>): Simulation {
    val maxMap = mutableMapOf<Point, IndexedValue<Int>>().withDefault { IndexedValue(-1, 0) }
    val supporting = mutableMapOf<Int, MutableSet<Int>>()
    val supported = mutableMapOf<Int, MutableSet<Int>>()
    bricks.forEach { brick ->
        val points = brick.points
        val max = points.map { maxMap.getValue(it) }.maxOf { it.value }
        brick.z = max + 1..<max + 1 + brick.z.size
        points.forEach { point ->
            val (idx, z) = maxMap.getValue(point)
            if (z == max && idx != -1) {
                supporting.getOrPut(idx) { mutableSetOf() } += brick.idx
                supported.getOrPut(brick.idx) { mutableSetOf() } += idx
            }
            maxMap[point] = brick.z.last.withIndex(brick.idx)
        }
    }

    return Simulation(supporting, supported)
}

private data class Simulation(val supporting: Map<Int, Set<Int>>, val supported: Map<Int, Set<Int>>)

private data class Brick(val idx: Int, val x: IntRange, val y: IntRange, var z: IntRange) {
    val points = x.flatMap { x -> y.map { y -> Point(x, y) } }
}

private fun parse(input: List<String>): List<Brick> = input.mapIndexed { idx, line ->
    line.split('~', limit = 2)
        .map { it.split(',', limit = 3).map(String::toInt) }
        .let { (a, b) ->
            val (x1, y1, z1) = a
            val (x2, y2, z2) = b
            Brick(idx, x = x1..x2, y = y1..y2, z = z1..z2)
        }
}.sortedBy { it.z.first }
