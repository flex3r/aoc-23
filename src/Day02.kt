import kotlin.math.max

fun main() {
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int = input.sumOf { line ->
    val (id, game) = parseIdAndGame(line)
    val (red, green, blue) = getMaxCubes(game)
    when {
        red > 12 || green > 13 || blue > 14 -> 0
        else -> id
    }
}

private fun part2(input: List<String>): Int = input.sumOf { line ->
    val (_, game) = parseIdAndGame(line)
    val (red, green, blue) = getMaxCubes(game)
    red * green * blue
}

private fun parseIdAndGame(line: String): Pair<Int, String> = line
    .substringAfter("Game ")
    .split(": ")
    .let { (id, line) -> id.toInt() to line }

private fun getMaxCubes(input: String): Triple<Int, Int, Int> {
    var red = 0
    var green = 0
    var blue = 0
    input.split("; ").forEach { sets ->
        sets.split(", ").forEach { set ->
            val (amount, color) = set.split(" ")
            when (color) {
                "red" -> red = max(red, amount.toInt())
                "green" -> green = max(green, amount.toInt())
                "blue" -> blue = max(blue, amount.toInt())
            }
        }
    }

    return Triple(red, green, blue)
}
