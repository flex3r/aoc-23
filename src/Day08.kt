fun main() {
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2L)
    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Long {
    val (pattern, nodes) = parse(input)
    return solve(start = nodes.getValue("AAA"), pattern, nodes)
}

private fun part2(input: List<String>): Long {
    val (pattern, nodes) = parse(input)
    return nodes
        .filterKeys { it.endsWith('A') }
        .map { (_, node) -> solve(start = node, pattern, nodes) }
        .reduce(::lcm)
}

private fun parse(input: List<String>): Pair<Sequence<Char>, Map<String, Node>> {
    val pattern = input[0].asSequence()
    val nodes = input.drop(2).map {
        val (element, paths) = it.split(" = ")
        val (left, right) = paths.drop(1).dropLast(1).split(", ")
        Node(element, left, right)
    }.associateBy(Node::element)
    return pattern to nodes
}

private fun solve(start: Node, pattern: Sequence<Char>, nodes: Map<String, Node>): Long = pattern
    .repeat()
    .runningFold(start) { acc, c ->
        when (c) {
            'L' -> nodes.getValue(acc.left)
            else -> nodes.getValue(acc.right)
        }
    }
    .indexOfFirst { it.element.endsWith('Z') }.toLong()

data class Node(val element: String, val left: String, val right: String)
