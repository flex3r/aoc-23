fun main() {
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Long = solve(parse(input))
private fun part2(input: List<String>): Long = solve(parse(input, repetitions = 5))

private fun parse(input: List<String>, repetitions: Int = 1): List<Record> = input.map { line ->
    val (conditions, groups) = line.split(" ").let { (a, b) ->
        val repeatedSprings = List(repetitions) { a }.joinToString(separator = "?").toList()
        val repeatedGroups = List(repetitions) { b }.joinToString(separator = ",").split(",").map(String::toInt)
        repeatedSprings to repeatedGroups
    }
    Record(conditions, groups)
}

private fun solve(records: List<Record>): Long {
    val cache = mutableMapOf<Record, Long>()
    return records.sumOf { it.variants(cache) }
}

data class Record(val springs: List<Char>, val groups: List<Int>) {
    fun variants(cache: MutableMap<Record, Long>): Long {
        val cached = cache[this]
        if (cached != null) {
            return cached
        }

        if (springs.isEmpty()) {
            return if (groups.isEmpty()) 1 else 0
        }
        if (groups.isEmpty()) {
            return if ('#' in springs) 0 else 1
        }

        val current = springs.first()
        val expected = groups.first()

        val variantsIfOperational = when {
            current != '#' -> Record(springs.drop(1), groups).variants(cache)
            else -> 0
        }
        val variantsIfDamaged = when {
            current != '.' && expected <= springs.size && '.' !in springs.take(expected) && (springs.size == expected || springs[expected] != '#') -> {
                Record(springs.drop(expected + 1), groups.drop(1)).variants(cache)
            }
            else -> 0
        }

        val res = variantsIfOperational + variantsIfDamaged
        cache[this] = res
        return res
    }
}
