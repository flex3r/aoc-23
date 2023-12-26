fun main() {
    val input = readInput("Day24")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Int {
    val hail = parse(input)
    return combinations(hail, m = 2).count { (a, b) -> intersectsInRange(a, b) }
}

private fun part2(input: List<String>): Long {
    val hail = parse(input)
    return z3 {
        val xt = int("x_t")
        val yt = int("y_t")
        val zt = int("z_t")
        val xvelt = int("xvel_t")
        val yvelt = int("yvel_t")
        val zvelt = int("zvel_t")
        val dt1 = int("dt1")
        val dt2 = int("dt2")
        val dt3 = int("dt3")

        val dt = listOf(dt1, dt2, dt3)

        val eqs = hail.take(3).flatMapIndexed { idx, hail ->
            listOf(
                (xt - hail.x.toLong()) eq (dt[idx] * (hail.dx.toLong() - xvelt)),
                (yt - hail.y.toLong()) eq (dt[idx] * (hail.dy.toLong() - yvelt)),
                (zt - hail.z.toLong()) eq (dt[idx] * (hail.dz.toLong() - zvelt)),
            )
        }

        solve(eqs)
        eval(xt + yt + zt).toLong()
    }
}

private fun intersectsInRange(a: Hail, b: Hail, range: ClosedRange<Double> = 200000000000000.0..400000000000000.0): Boolean {
    if (a.slope == b.slope) return false

    val cx = ((b.slope * b.x) - (a.slope * a.x) + a.y - b.y) / (b.slope - a.slope)
    val cy = (a.slope * (cx - a.x)) + a.y
    val valid = valid(a, cx, cy) && valid(b, cx, cy)

    return valid && cx in range && cy in range
}

private fun valid(h: Hail, cx: Double, cy: Double): Boolean {
    return !((h.dx < 0 && h.x < cx) || (h.dx > 0 && h.x > cx) || (h.dy < 0 && h.y < cy) || (h.dy > 0 && h.y > cy))
}

private fun parse(input: List<String>) = input.map { line ->
    val (x, y, z, dx, dy, dz) = line.split(" @ ", ", ").map { it.trim().toDouble() }
    Hail(x, y, z, dx, dy, dz)
}

private data class Hail(val x: Double, val y: Double, val z: Double, val dx: Double, val dy: Double, val dz: Double) {
    val slope get() = dy / dx
}

private operator fun <E> List<E>.component6() = this[5]
