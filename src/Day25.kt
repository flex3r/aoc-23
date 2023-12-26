import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph

fun main() {
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 54)

    val input = readInput("Day25")
    measureAndPrintResult {
        part1(input)
    }

}

private fun part1(input: List<String>): Int {
    val graph = DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
    input.forEach { line ->
        val (from, others) = line.split(": ")
        graph.addVertex(from)
        others.split(" ").forEach { other ->
            graph.addVertex(other)
            graph.addEdge(from, other)
        }
    }

    val group1 = StoerWagnerMinimumCut(graph).minCut().size
    return group1 * (graph.vertexSet().size - group1)
}
