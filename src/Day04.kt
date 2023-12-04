fun main() {
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int {
    return calculateCardWins(input)
        .filter { it > 0 }
        .sumOf { 2.pow(it - 1) }
}

private fun part2(input: List<String>): Int {
    val cardWins = calculateCardWins(input)
    val numCards = MutableList(cardWins.size) { 0 }
    cardWins.forEachIndexed { idx, wins ->
        val new = ++numCards[idx]
        for (it in 1..wins) {
            numCards[idx + it] += new
        }
    }
    return numCards.sum()
}

private fun calculateCardWins(input: List<String>) = input.map {
    val (wins, numbers) = parseCard(it)
    wins.intersect(numbers).size
}

private fun parseCard(input: String): List<Set<Int>> = input
    .substringAfter(": ")
    .split(" | ")
    .map { list ->
        list.split(" ")
            .filter(String::isNotEmpty)
            .map { it.trim().toInt() }
            .toSet()
    }
