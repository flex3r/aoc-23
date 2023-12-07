fun main() {
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>) = input
    .map { it.split(" ") }
    .map { (cardString, bid) ->
        val cards = parseCards(cardString)
        val groups = parseGroups(cards)
        Hand(cards, groups, bid.toInt())
    }
    .evaluateHands()

private fun part2(input: List<String>) = input
    .map { it.split(" ") }
    .map { (cardString, bid) ->
        val cards = parseCards(cardString, allowedValues = listOf('T', 'Q', 'K', 'A'))
        val groups = (2..13)
            .map { replacement -> parseGroups(cards) { c -> c.takeUnless { it == 1 } ?: replacement } }
            .maxWith(compareBy({ it[0] }, { it.getOrNull(1) }))
        Hand(cards, groups, bid.toInt())
    }
    .evaluateHands()

private fun parseGroups(cards: List<Int>, transform: (Int) -> Int = { it }) = cards.map(transform)
    .groupBy { it }
    .map { (_, v) -> v.size }
    .sortedDescending()

private fun parseCards(input: String, allowedValues: List<Char> = listOf('T', 'J', 'Q', 'K', 'A')) = input.map {
    when (val idx = allowedValues.indexOf(it)) {
        -1 -> it.digitToIntOrNull() ?: 1 // Part 2: Joker has lowest value
        else -> idx + 10
    }
}

private fun List<Hand>.evaluateHands() = sortedWith(handsComparator)
    .withIndex()
    .sumOf { (idx, hand) -> (idx + 1) * hand.bid }

private val handsComparator: Comparator<Hand> = compareBy(
    { it.groups[0] },
    { it.groups.getOrNull(1) },
    { it.cards[0] },
    { it.cards[1] },
    { it.cards[2] },
    { it.cards[3] },
    { it.cards[4] },
)

data class Hand(val cards: List<Int>, val groups: List<Int>, val bid: Int)
