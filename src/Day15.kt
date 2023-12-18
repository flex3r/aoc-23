fun main() {
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>) = input.first().split(',').sumOf(String::hash)
private fun part2(input: List<String>): Int {
    val boxes = List(256) { mutableMapOf<String, Int>() }
    input.first().split(',').forEach { step ->
        val op = step.first { !it.isLetter() }
        val label = step.substringBefore(op)
        when (op) {
            '-' -> boxes[label.hash()].remove(label)
            '=' -> boxes[label.hash()][label] = step.substringAfter('=').toInt()
        }
    }

    return boxes.withIndex().sumOf { (idx, box) ->
        box.values.withIndex().sumOf { (slot, lens) ->
            (idx + 1) * (slot + 1) * lens
        }
    }
}

fun String.hash(): Int = fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
