data class MetalPipe(val char: Char, val x: Int, val y: Int) {
    var distance: Int = Int.MAX_VALUE
    fun neighbors(): Pair<Pair<Int, Int>, Pair<Int,Int>>? {
        return when (char) {
            '.' -> null
            '|' -> Pair(Pair(x-1, y), Pair(x+1, y))
            '-' -> Pair(Pair(x, y-1), Pair(x, y+1))
            'J' -> Pair(Pair(x-1, y), Pair(x, y-1))
            '7' -> Pair(Pair(x, y-1), Pair(x+1, y))
            'L' -> Pair(Pair(x, y+1), Pair(x-1, y))
            'F' -> Pair(Pair(x, y+1), Pair(x+1, y))
            else -> {
                assert(false, { "Wrong value input" })
                return null
            }
        }
    }

    fun next(): Pair<Int, Int> {
        return Pair(x, y)
    }
}

fun parseInput(input: List<String>): List<MetalPipe> {
    return input.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, pipeChar ->
            MetalPipe(pipeChar, rowIndex, columnIndex)
        }
    }.flatten()
}
fun main() {
    fun part1(input: List<String>): Int {
        val pipeMap = parseInput(input)
        val start = pipeMap.first { it.char == 'S' }
        val startNeighbors = listOf<Pair<Int, Int>>(Pair(start.x - 1, start.y), Pair(start.x, start.y - 1), Pair(start.x + 1, start.y), Pair(start.x, start.y + 1))
        val validNeighbors = emptyList<MetalPipe>().toMutableList()
        for (neigh in startNeighbors) {
            if (neigh.first < 0 || neigh.second < 0) { continue }
            val pipe = pipeMap.first { it.x == neigh.first && it.y == neigh.second }
            if (pipe.neighbors()?.first == Pair(start.x, start.y) || pipe.neighbors()?.second == Pair(start.x, start.y)) {
                validNeighbors.add(pipe)
            }
        }
        val validCoordinates = validNeighbors.map { Pair(it.x, it.y) }
        val startPipe = listOf('-', '|', '7', 'F', 'J', 'L').firstNotNullOf {
            val pipe = MetalPipe(it, start.x, start.y)
            if (listOf(pipe.neighbors()!!.first, pipe.neighbors()!!.second).containsAll(validCoordinates)) {
                pipe
            } else {
                null
            }
        }
        val visitedNodes = emptyList<MetalPipe>().toMutableList()
        var nodes = emptyList<MetalPipe>().toMutableList()
        nodes.add(startPipe)
        var step = 0
        while (nodes.isNotEmpty()) {
            val node = nodes.first()
            nodes = nodes.drop(1).toMutableList()
            val neighbors = pipeMap.filter {
                val adj = node.neighbors() ?: return@filter false
                val pair = Pair(it.x, it.y)
                adj.first == pair || adj.second == pair
            }
            visitedNodes.add(node)
            for (neighbor in neighbors) {
                if (!visitedNodes.contains(neighbor)) {
                    if (node.distance == Int.MAX_VALUE) {
                        neighbor.distance = 1
                    } else {
                        neighbor.distance = node.distance + 1
                    }
                    nodes.add(neighbor)
                }
            }
            step++
        }
        return visitedNodes.filter { it.distance != Int.MAX_VALUE }.maxOf { it.distance }
    }


    // Tests
    val testInput = readInput("Day10_tests")
    val testInput2 = readInput("Day10_tests2")
    part1(testInput).println()
    check(part1(testInput) == 4)
    part1(testInput2).println()
    check(part1(testInput2) == 8)

    val input = readInput("Day10")
    part1(input).println()
}

/*
--- Day 10: Pipe Maze ---
You use the hang glider to ride the hot air from Desert Island all the way up to the floating metal island. This island is surprisingly cold and there definitely aren't any thermals to glide on, so you leave your hang glider behind.

You wander around for a while, but you don't find any people or animals. However, you do occasionally find signposts labeled "Hot Springs" pointing in a seemingly consistent direction; maybe you can find someone at the hot springs and ask them where the desert-machine parts are made.

The landscape here is alien; even the flowers and trees are made of metal. As you stop to admire some metal grass, you notice something metallic scurry away in your peripheral vision and jump into a big pipe! It didn't look like any animal you've ever seen; if you want a better look, you'll need to get ahead of it.

Scanning the area, you discover that the entire field you're standing on is densely packed with pipes; it was hard to tell at first because they're the same metallic silver color as the "ground". You make a quick sketch of all of the surface pipes you can see (your puzzle input).

The pipes are arranged in a two-dimensional grid of tiles:

| is a vertical pipe connecting north and south.
- is a horizontal pipe connecting east and west.
L is a 90-degree bend connecting north and east.
J is a 90-degree bend connecting north and west.
7 is a 90-degree bend connecting south and west.
F is a 90-degree bend connecting south and east.
. is ground; there is no pipe in this tile.
S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
Based on the acoustics of the animal's scurrying, you're confident the pipe that contains the animal is one large, continuous loop.

For example, here is a square loop of pipe:

.....
.F-7.
.|.|.
.L-J.
.....
If the animal had entered this loop in the northwest corner, the sketch would instead look like this:

.....
.S-7.
.|.|.
.L-J.
.....
In the above diagram, the S tile is still a 90-degree F bend: you can tell because of how the adjacent pipes connect to it.

Unfortunately, there are also many pipes that aren't connected to the loop! This sketch shows the same loop as above:

-L|F7
7S-7|
L|7||
-L-J|
L|-JF
In the above diagram, you can still figure out which pipes form the main loop: they're the ones connected to S, pipes those pipes connect to, pipes those pipes connect to, and so on. Every pipe in the main loop connects to its two neighbors (including S, which will have exactly two pipes connecting to it, and which is assumed to connect back to those two pipes).

Here is a sketch that contains a slightly more complex main loop:

..F7.
.FJ|.
SJ.L7
|F--J
LJ...
Here's the same example sketch with the extra, non-main-loop pipe tiles also shown:

7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ
If you want to get out ahead of the animal, you should find the tile in the loop that is farthest from the starting position. Because the animal is in the pipe, it doesn't make sense to measure this by direct distance. Instead, you need to find the tile that would take the longest number of steps along the loop to reach from the starting point - regardless of which way around the loop the animal went.

In the first example with the square loop:

.....
.S-7.
.|.|.
.L-J.
.....
You can count the distance each tile in the loop is from the starting point like this:

.....
.012.
.1.3.
.234.
.....
In this example, the farthest point from the start is 4 steps away.

Here's the more complex loop again:

..F7.
.FJ|.
SJ.L7
|F--J
LJ...
Here are the distances for each tile on that loop:

..45.
.236.
01.78
14567
23...
Find the single giant loop starting at S. How many steps along the loop does it take to get from the starting position to the point farthest from the starting position?

* */