fun main() {
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private fun part1(input: List<String>): Int = input.sumOfChars { x, y, c ->
    when {
        c.isDigit() || c == '.' -> 0
        else -> getAdjacentPartNumbers(x, y, input).sum()
    }
}

private fun part2(input: List<String>): Int = input.sumOfChars { x, y, c ->
    when (c) {
        '*' -> getAdjacentPartNumbers(x, y, input)
            .takeIf { it.size == 2 }
            ?.product() ?: 0

        else -> 0
    }
}

private fun getAdjacentPartNumbers(partX: Int, partY: Int, input: List<String>): Set<Int> {
    val numbers = mutableSetOf<Int>()
    (partX - 1..partX + 1).forEach { x ->
        (partY - 1..partY + 1).forEach line@{ y ->
            if (x == partX && y == partY) return@line
            numbers += getAdjacentPartNumber(x, y, input) ?: return@line
        }
    }

    return numbers
}

private fun getAdjacentPartNumber(x: Int, y: Int, input: List<String>): Int? {
    val line = input.getOrNull(y) ?: return null
    if (line.getOrNull(x)?.isDigit() == false) {
        return null
    }

    val before = line.substring(0..<x).reversed().takeWhile(Char::isDigit).reversed()
    val after = line.substring(startIndex = x).takeWhile(Char::isDigit)

    return (before + after).toInt()
}
