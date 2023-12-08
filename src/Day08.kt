
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

    fun part2(input: List<String>): Long {
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
        val startingNodes = nodes.filter { it.key.last() == 'A' }.map { it.key }
        val steps = emptyList<Int>().toMutableList()
        for (node in startingNodes) {
            var step = 0
            var current = node
            while (!current.endsWith('Z')) {
                val move = instructions.get(step.mod(instructions.length))
                current = nodes[current]!!.applyRule(move)
                step++
            }
            steps.add(step)
        }
        return  steps.map { it.toBigInteger() }.reduce { acc, bigInteger -> acc * bigInteger / acc.gcd(bigInteger) }.toLong()
    }

    val testInput = readInput("Day08_tests")
    part1(testInput).println()
    check(part1(testInput) == 2)

    val testInput2 = readInput("Day08_tests2")
    part1(testInput2).println()
    check(part1(testInput2) == 6)

    val testInputPart2 = readInput("Day08Part2_tests")
    part2(testInputPart2).println()
    check(part2(testInputPart2) == 6.toLong())

    println(part1(readInput("Day08")))
    println(part2(readInput("Day08")))
}

/*
--- Day 8: Haunted Wasteland ---
You're still riding a camel across Desert Island when you spot a sandstorm quickly approaching. When you turn to warn the Elf, she disappears before your eyes! To be fair, she had just finished warning you about ghosts a few minutes ago.

One of the camel's pouches is labeled "maps" - sure enough, it's full of documents (your puzzle input) about how to navigate the desert. At least, you're pretty sure that's what they are; one of the documents contains a list of left/right instructions, and the rest of the documents seem to describe some kind of network of labeled nodes.

It seems like you're meant to use the left/right instructions to navigate the network. Perhaps if you have the camel follow the same instructions, you can escape the haunted wasteland!

After examining the maps for a bit, two nodes stick out: AAA and ZZZ. You feel like AAA is where you are now, and you have to follow the left/right instructions until you reach ZZZ.

This format defines each node of the network individually. For example:

RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
Starting with AAA, you need to look up the next element based on the next left/right instruction in your input. In this example, start with AAA and go right (R) by choosing the right element of AAA, CCC. Then, L means to choose the left element of CCC, ZZZ. By following the left/right instructions, you reach ZZZ in 2 steps.

Of course, you might not find ZZZ right away. If you run out of left/right instructions, repeat the whole sequence of instructions as necessary: RL really means RLRLRLRLRLRLRLRL... and so on. For example, here is a situation that takes 6 steps to reach ZZZ:

LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
Starting at AAA, follow the left/right instructions. How many steps are required to reach ZZZ?

Your puzzle answer was 19099.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
The sandstorm is upon you and you aren't any closer to escaping the wasteland. You had the camel follow the instructions, but you've barely left your starting position. It's going to take significantly more steps to escape!

What if the map isn't for people - what if the map is for ghosts? Are ghosts even bound by the laws of spacetime? Only one way to find out.

After examining the maps a bit longer, your attention is drawn to a curious fact: the number of nodes with names ending in A is equal to the number ending in Z! If you were a ghost, you'd probably just start at every node that ends with A and follow all of the paths at the same time until they all simultaneously end up at nodes that end with Z.

For example:

LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
Here, there are two starting nodes, 11A and 22A (because they both end with A). As you follow each left/right instruction, use that instruction to simultaneously navigate away from both nodes you're currently on. Repeat this process until all of the nodes you're currently on end with Z. (If only some of the nodes you're on end with Z, they act like any other node and you continue as normal.) In this example, you would proceed as follows:

Step 0: You are at 11A and 22A.
Step 1: You choose all of the left paths, leading you to 11B and 22B.
Step 2: You choose all of the right paths, leading you to 11Z and 22C.
Step 3: You choose all of the left paths, leading you to 11B and 22Z.
Step 4: You choose all of the right paths, leading you to 11Z and 22B.
Step 5: You choose all of the left paths, leading you to 11B and 22C.
Step 6: You choose all of the right paths, leading you to 11Z and 22Z.
So, in this example, you end up entirely on nodes that end in Z after 6 steps.

Simultaneously start on every node that ends with A. How many steps does it take before you're only on nodes that end with Z?

Answer
 */