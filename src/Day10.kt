fun main() {
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 4)

    val input = readInput("Day10")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int = findPipeLoop(findStart(input), input)
    .maxOf(IndexedValue<Pair<Int, Int>>::index)

private fun part2(input: List<String>): Int {
    val start = findStart(input)
    val pipes = findPipeLoop(start, input)
        .map(IndexedValue<Pair<Int, Int>>::value)
        .toSet()

    // if top of S is vertical section, S is vertical as well
    val validVerticalSections = when (input[start.first - 1][start.second]) {
        in listOf('|', '7', 'F') -> listOf('|', 'J', 'L', 'S')
        else -> listOf('|', 'J', 'L')
    }

    // even-odd rule for vertical pipes
    return input.indices.sumOf { y ->
        input.first().indices.count { x ->
            if (Pair(x, y) in pipes) return@count false
            (x downTo 0).count {
                Pair(it, y) in pipes && input[y][it] in validVerticalSections
            } % 2 == 1
        }
    }
}

private fun findStart(input: List<String>) = input.withIndex().firstNotNullOf { (y, line) ->
    val x = line.indexOf('S').takeUnless { it == -1 } ?: return@firstNotNullOf null
    x to y
}

private fun findPipeLoop(start: Pair<Int, Int>, input: List<String>) = bfs(start) { (x, y) ->
    when (input[y][x]) {
        '|' -> listOf(x to y - 1, x to y + 1)
        '-' -> listOf(x - 1 to y, x + 1 to y)
        'L' -> listOf(x to y - 1, x + 1 to y)
        'J' -> listOf(x to y - 1, x - 1 to y)
        '7' -> listOf(x to y + 1, x - 1 to y)
        'F' -> listOf(x to y + 1, x + 1 to y)
        'S' -> listOf(x to y + 1, x to y - 1, x - 1 to y, x + 1 to y)
            .filter { (x, y) -> y in input.indices && x in input.first().indices }

        else -> emptyList()
    }
}
