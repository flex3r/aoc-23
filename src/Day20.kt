fun main() {
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 32000000L)
    check(part1(readInput("Day20_test2")) == 11687500L)

    val input = readInput("Day20")
    measureAndPrintResult {
        part1(input)
    }
    measureAndPrintResult {
        part2(input)
    }
}

private fun part1(input: List<String>): Long {
    val modules = parseModules(input)
    var l = 0L
    var h = 0L
    repeat(1000) {
        pressButton(modules) { _, pulse, _ ->
            if (pulse) h++ else l++
        }
    }
    return l * h
}

private fun part2(input: List<String>): Long {
    val modules = parseModules(input)
    val sus = mutableMapOf("js" to 0L, "zb" to 0L, "bs" to 0L, "rr" to 0L)
    var presses = 0L
    while (sus.values.any { it == 0L }) {
        presses++
        pressButton(modules) { module, _, output ->
            if (module.name in sus && output == true) {
                sus[module.name] = presses
            }
        }
    }

    return sus.values.reduce(::lcm)
}

private inline fun pressButton(modules: Map<String, Module>, onPulse: (Module, Boolean, Boolean?) -> Unit) {
    val queue = ArrayDeque(listOf(modules.getValue("broadcaster") to false))
    while (queue.isNotEmpty()) {
        val (module, pulse) = queue.removeFirst()
        val output = module.output(pulse)
        onPulse(module, pulse, output)

        if (output == null) {
            continue
        }

        module.destinations.forEach {
            val destinationModule = modules.getValue(it)
//                println("${module.name} ${if (output) "-high" else "-low"}->$it")
            destinationModule.receive(module.name, output)
            queue += destinationModule to output
        }
    }
}

private sealed interface Module {
    val name: String
    val destinations: List<String>
    fun receive(from: String, pulse: Boolean) = Unit
    fun output(pulse: Boolean): Boolean? = pulse

    data class Broadcast(override val name: String, override val destinations: List<String>) : Module
    data class Unknown(override val name: String, override val destinations: List<String> = emptyList()) : Module {
        override fun output(pulse: Boolean) = null
    }

    data class FlipFlop(
        override val name: String,
        override val destinations: List<String>,
        var state: Boolean = false,
    ) : Module {
        override fun receive(from: String, pulse: Boolean) {
            if (!pulse) state = !state
        }

        override fun output(pulse: Boolean) = if (pulse) null else state
    }

    data class Conjunction(
        override val name: String,
        override val destinations: List<String>,
        val pulses: MutableMap<String, Boolean> = mutableMapOf(),
    ) : Module {
        override fun receive(from: String, pulse: Boolean) {
            pulses[from] = pulse
        }

        override fun output(pulse: Boolean) = !pulses.keys.all { pulses.getValue(it) }
    }
}

private fun parseModules(input: List<String>) = input.map { line ->
    val (name, destinationPart) = line.split(" -> ", limit = 2)
    val destinations = destinationPart.split(", ")
    when (name.first()) {
        '%' -> Module.FlipFlop(name = name.drop(1), destinations = destinations)
        '&' -> Module.Conjunction(name = name.drop(1), destinations = destinations)
        else -> Module.Broadcast(name = name, destinations = destinations)
    }
}.associateBy(Module::name).withDefault { Module.Unknown(it) }.also { modules ->
    modules.values.forEach { module ->
        module.destinations.forEach {
            val destination = modules.getValue(it) as? Module.Conjunction
            destination?.pulses?.set(module.name, false)
        }
    }
}
