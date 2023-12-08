
fun Pair<String, String>.applyRule(char: Char): String {
    return if (char == 'L') {
        first
    } else {
        second
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input.first()
        val nodes = input.filter { it.isNotEmpty() }.subList(1, input.size - 1).fold(
            emptyMap<String, Pair<String, String>>().toMutableMap()
        ) { acc, it ->
            val split = it.split(" = ")
            val node = split.first().toString()
            val values = split.last().toString().removePrefix("(").removeSuffix(")").split(", ")
            val leftNode = values.first()
            val rightNode = values.last()
            acc.put(node, Pair(leftNode, rightNode))
            return@fold acc
        }
        var count = 0
        var current = nodes["AAA"]
        var node = "AAA"
        while (node != "ZZZ") {
            val movement = instructions.get(count.mod(instructions.length))
            node = current!!.applyRule(movement)
            current = nodes[node]
            count++
        }
        return count
    }

    val testInput = readInput("Day08_tests")
    part1(testInput).println()
    check(part1(testInput) == 2)

    val testInput2 = readInput("Day08_tests2")
    part1(testInput2).println()
    check(part1(testInput2) == 6)

    println(part1(readInput("Day08")))
}