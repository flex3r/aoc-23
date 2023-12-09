fun main() {
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int = input.sumOf {
    it.split(" ").map(String::toInt)
        .extrapolateValues()
        .sumOf(List<Int>::last)
}

private fun part2(input: List<String>): Int = input.sumOf {
    it.split(" ").map(String::toInt)
        .extrapolateValues()
        .foldRight<_, Int>(0) { s, acc -> s.first() - acc }
}

private fun List<Int>.extrapolateValues(): List<List<Int>> = generateSequence(this) {
    it.windowed(2).map { (a, b) -> b - a }
}.takeWhile { s -> s.any { it != 0 } }.toList()
