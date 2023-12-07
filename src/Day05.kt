import kotlin.math.max
import kotlin.math.min

fun main() {
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Long {
    val (seeds, mappings) = parseGarden(input)
    return seeds.minOf { seed ->
        mappings.fold(seed) { acc, mapping ->
            mapping.firstNotNullOfOrNull {
                when (acc) {
                    in it.source..<it.source + it.length -> it.destination + (acc - it.source)
                    else -> null
                }
            } ?: acc
        }
    }
}

private fun part2(input: List<String>): Long {
    val (seeds, mappings) = parseGarden(input)
    val ranges = seeds.chunked(2).map { (seed, range) ->
        seed..<seed + range
    }

    return ranges.flatMap { seedRange ->
        mappings.fold(listOf(seedRange)) { ranges, mappings ->
            val queue = ArrayDeque(ranges)
            val mapped = mutableListOf<LongRange>()
            while (queue.isNotEmpty()) {
                val seed = queue.removeFirst()
                val validMapping = mappings.find { (_, start, length) ->
                    seed.first < start + length && seed.last > start
                }

                if (validMapping == null) {
                    mapped += seed
                    continue
                }

                val (dest, start, length) = validMapping
                val end = start + length
                val offset = max(seed.first, start) - start
                val mappedStart = dest + offset
                val mappedRange = min(seed.last, end) - max(seed.first, start)
                mapped += mappedStart..mappedStart + mappedRange
                if (seed.first < start) {
                    queue += seed.first..<start
                }
                if (seed.last > end) {
                    queue += end + 1..seed.last
                }
            }
            mapped
        }
    }.minOf(LongRange::first)
}

fun parseGarden(input: List<String>): Garden {
    val parts = input.partitionBy(String::isEmpty)
    val seedParts = parts[0][0].substringAfter("seeds: ").split(" ").map(String::toLong)
    val mappings = parts.drop(1).map { part ->
        part.drop(1).map { line ->
            val (dest, src, range) = line.split(" ").map(String::toLong)
            Mapping(dest, src, range)
        }

    }
    return Garden(seedParts, mappings)
}

data class Mapping(val destination: Long, val source: Long, val length: Long)
data class Garden(val seeds: List<Long>, val mappings: List<List<Mapping>>)
