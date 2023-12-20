fun main() {
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000L)

    val input = readInput("Day19")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int {
    val (parts, workflows) = parse(input)
    return parts.filter { part ->
        var state = "in"
        while (state != "A" && state != "R") {
            val flow = workflows.getValue(state)
            state = flow.rules.find { rule ->
                when (rule.op) {
                    '>' -> part.getValue(rule.attr) > rule.n
                    '<' -> part.getValue(rule.attr) < rule.n
                    else -> error("forsen")
                }
            }?.next ?: flow.fallback
        }
        state == "A"
    }.sumOf { it.values.sum() }
}

private fun part2(input: List<String>) = parse(input).second.findAcceptedRanges(ranges = "xmas".associate { it to 1..4000 }, name = "in")

private fun Map<String, Workflow>.findAcceptedRanges(ranges: Map<Char, IntRange>, name: String): Long {
    if (name == "R") return 0L
    if (name == "A") {
        return ranges.values.fold(1L) { acc, range -> acc * range.size }
    }

    val flow = getValue(name)
    var total = 0L
    val rejectedRanges = flow.rules.fold(ranges) { acc, rule ->
        val range = acc.getValue(rule.attr)
        val accepted = if (rule.op == '>') rule.n + 1..range.last else range.first..<rule.n
        val rejected = if (rule.op == '>') range.first..rule.n else rule.n..range.last

        if (!accepted.isEmpty()) {
            val acceptedRanges = acc + (rule.attr to accepted)
            total += findAcceptedRanges(acceptedRanges, rule.next)
        }
        if (rejected.isEmpty()) return 0L

        acc + (rule.attr to rejected)
    }
    return total + findAcceptedRanges(rejectedRanges, flow.fallback)
}

private fun parse(input: List<String>): Pair<List<Map<Char, Int>>, Map<String, Workflow>> {
    val split = input.partitionBy(String::isEmpty)
    val workflows = split.first().map { workflow ->
        val name = workflow.substringBefore('{')
        val (rules, fallback) = workflow.substringAfter('{').dropLast(1).split(',').let {
            it.dropLast(1).map { rule ->
                val (condition, to) = rule.split(':', limit = 2)
                val attr = condition.first()
                val op = condition[1]
                val value = condition.substring(2).toInt()
                Rule(attr, op, value, to)
            } to it.last()
        }
        Workflow(name, rules, fallback)
    }.associateBy(Workflow::name)

    val parts = split.last().map { part ->
        part.drop(1).dropLast(1).split(',').associate {
            val partSplit = it.split('=', limit = 2)
            partSplit[0].single() to partSplit[1].toInt()
        }
    }

    return parts to workflows
}

private data class Workflow(val name: String, val rules: List<Rule>, val fallback: String)
private data class Rule(val attr: Char, val op: Char, val n: Int, val next: String)
