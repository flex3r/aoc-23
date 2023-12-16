fun main() {
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>) = findMirrors(input.partitionBy(String::isEmpty))
private fun part2(input: List<String>) = findMirrors(input.partitionBy(String::isEmpty), smudges = 1)

private fun findMirror(pattern: List<String>, smudges: Int): Int? {
    return (0..<pattern.lastIndex).find { y ->
        (0..y.coerceAtMost(maximumValue = pattern.lastIndex - y - 1)).sumOf { offset ->
            val k = y - offset
            val l = y + 1 + offset
            pattern[y].indices.count { x -> pattern[k][x] != pattern[l][x] }
        } == smudges
    }?.inc()
}

private fun findMirrors(patterns: List<List<String>>, smudges: Int = 0) = patterns.sumOf { pattern ->
    findMirror(pattern, smudges)?.let { n -> 100 * n }
        ?: findMirror(pattern.transposeLines(), smudges)
        ?: error("forsen")
}
