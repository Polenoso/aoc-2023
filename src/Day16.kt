enum class Direction {
    RIGHT, LEFT, UP, DOWN;

    val next: Coordinate2D
        get() =
        when (this) {
            RIGHT -> Coordinate2D(0, 1)
            LEFT -> Coordinate2D(0, -1)
            UP -> Coordinate2D(-1, 0)
            DOWN -> Coordinate2D(1, 0)
        }

    fun calculateDirection(char: Char): List<Direction> {
        if (char == '.') return listOf(this)
        return when (Pair(this, char)) {
            Pair(RIGHT, '/') -> listOf(UP)
            Pair(RIGHT, '\\') -> listOf(DOWN)
            Pair(RIGHT, '-') -> listOf(RIGHT)
            Pair(RIGHT, '|') -> listOf(UP, DOWN)
            Pair(LEFT, '/') -> listOf(DOWN)
            Pair(LEFT, '\\') -> listOf(UP)
            Pair(LEFT, '-') -> listOf(LEFT)
            Pair(LEFT, '|') -> listOf(UP, DOWN)
            Pair(UP, '/') -> listOf(RIGHT)
            Pair(UP, '\\') -> listOf(LEFT)
            Pair(UP, '-') -> listOf(RIGHT, LEFT)
            Pair(UP, '|') -> listOf(UP)
            Pair(DOWN, '/') -> listOf(LEFT)
            Pair(DOWN, '\\') -> listOf(RIGHT)
            Pair(DOWN, '-') -> listOf(RIGHT, LEFT)
            Pair(DOWN, '|') -> listOf(DOWN)
            else -> {
                assert(false) { "Should not reach this point ever, did you miss some cases? $this $char" }
                emptyList<Direction>()
            }
        }
    }
}

typealias Beam = Pair<Coordinate2D, Direction>
data class Tile(val char: Char, val coordinate2D: Coordinate2D) {
    var visited = false

    fun nextBeam(currentDirection: Direction): List<Beam> {
        visited = true
        if (char == '.')  {
            return listOf(Pair(coordinate2D.plus(currentDirection.next), currentDirection))
        }
        val nextDirections = currentDirection.calculateDirection(char)
        return nextDirections.map {
            Pair(coordinate2D.plus(it.next), it)
        }
    }
}
fun main() {
    fun findVisitedTiles(layout: List<List<Tile>>, startingAt: Beam): Long {
        val copyLayout = layout.map { it.map { it.copy() } }
        var beams = listOf(startingAt).toMutableSet()
        val visited = emptyList<Beam>().toMutableList()
        while (beams.isNotEmpty()) {
            val beam = beams.first()
            beams = beams.drop(1).toMutableSet()
            if (beam in visited) continue
            visited.add(beam)
            if (beam.first.x < 0 || beam.first.x >= copyLayout.size || beam.first.y < 0 || beam.first.y >= copyLayout.first().size) continue
            val tile = copyLayout[beam.first.x][beam.first.y]
            val newBeams = tile.nextBeam(beam.second)
            beams.addAll(newBeams)
        }

        return copyLayout.flatten().count { it.visited }.toLong()
    }
    fun part1(input: List<String>): Long {
        val layout = input.mapIndexed { row, s ->
            s.mapIndexed { col, c -> Tile(c, Coordinate2D(row, col)) }
        }
        return findVisitedTiles(layout, Pair(Coordinate2D(0, 0), Direction.RIGHT))
    }

    fun part2(input: List<String>): Long {
        val layout = input.mapIndexed { row, s ->
            s.mapIndexed { col, c -> Tile(c, Coordinate2D(row, col)) }
        }
        val topRow = layout.first().indices.map { Pair(Coordinate2D(0, it), Direction.DOWN) }
        val bottomRow = layout.first().indices.map { Pair(Coordinate2D(layout.lastIndex, it), Direction.UP) }
        val leftCol = layout.indices.map { Pair(Coordinate2D(it, 0), Direction.RIGHT) }
        val rightCol = layout.indices.map { Pair(Coordinate2D(it, layout.first().lastIndex), Direction.LEFT) }

        return listOf(topRow, bottomRow, leftCol, rightCol).flatten().maxOfOrNull { findVisitedTiles(layout, it) } ?: 0
    }

    // Tests
    val testInput = readInput("Day16_tests")
    part1(testInput).println()
    check(part1(testInput) == 46L)
    part2(testInput).println()
    check(part2(testInput) == 51L)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

/*
--- Day 16: The Floor Will Be Lava ---
With the beam of light completely focused somewhere, the reindeer leads you deeper still into the Lava Production Facility. At some point, you realize that the steel facility walls have been replaced with cave, and the doorways are just cave, and the floor is cave, and you're pretty sure this is actually just a giant cave.

Finally, as you approach what must be the heart of the mountain, you see a bright light in a cavern up ahead. There, you discover that the beam of light you so carefully focused is emerging from the cavern wall closest to the facility and pouring all of its energy into a contraption on the opposite side.

Upon closer inspection, the contraption appears to be a flat, two-dimensional square grid containing empty space (.), mirrors (/ and \), and splitters (| and -).

The contraption is aligned so that most of the beam bounces around the grid, but each tile on the grid converts some of the beam's light into heat to melt the rock in the cavern.

You note the layout of the contraption (your puzzle input). For example:

.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
The beam enters in the top-left corner from the left and heading to the right. Then, its behavior depends on what it encounters as it moves:

If the beam encounters empty space (.), it continues in the same direction.
If the beam encounters a mirror (/ or \), the beam is reflected 90 degrees depending on the angle of the mirror. For instance, a rightward-moving beam that encounters a / mirror would continue upward in the mirror's column, while a rightward-moving beam that encounters a \ mirror would continue downward from the mirror's column.
If the beam encounters the pointy end of a splitter (| or -), the beam passes through the splitter as if the splitter were empty space. For instance, a rightward-moving beam that encounters a - splitter would continue in the same direction.
If the beam encounters the flat side of a splitter (| or -), the beam is split into two beams going in each of the two directions the splitter's pointy ends are pointing. For instance, a rightward-moving beam that encounters a | splitter would split into two beams: one that continues upward from the splitter's column and one that continues downward from the splitter's column.
Beams do not interact with other beams; a tile can have many beams passing through it at the same time. A tile is energized if that tile has at least one beam pass through it, reflect in it, or split in it.

In the above example, here is how the beam of light bounces around the contraption:

>|<<<\....
|v-.\^....
.v...|->>>
.v...v^.|.
.v...v^...
.v...v^..\
.v../2\\..
<->-/vv|..
.|<<<2-|.\
.v//.|.v..
Beams are only shown on empty tiles; arrows indicate the direction of the beams. If a tile contains beams moving in multiple directions, the number of distinct directions is shown instead. Here is the same diagram but instead only showing whether a tile is energized (#) or not (.):

######....
.#...#....
.#...#####
.#...##...
.#...##...
.#...##...
.#..####..
########..
.#######..
.#...#.#..
Ultimately, in this example, 46 tiles become energized.

The light isn't energizing enough tiles to produce lava; to debug the contraption, you need to start by analyzing the current situation. With the beam starting in the top-left heading right, how many tiles end up being energized?

Your puzzle answer was 8116.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
As you try to work out what might be wrong, the reindeer tugs on your shirt and leads you to a nearby control panel. There, a collection of buttons lets you align the contraption so that the beam enters from any edge tile and heading away from that edge. (You can choose either of two directions for the beam if it starts on a corner; for instance, if the beam starts in the bottom-right corner, it can start heading either left or upward.)

So, the beam could start on any tile in the top row (heading downward), any tile in the bottom row (heading upward), any tile in the leftmost column (heading right), or any tile in the rightmost column (heading left). To produce lava, you need to find the configuration that energizes as many tiles as possible.

In the above example, this can be achieved by starting the beam in the fourth tile from the left in the top row:

.|<2<\....
|v-v\^....
.v.v.|->>>
.v.v.v^.|.
.v.v.v^...
.v.v.v^..\
.v.v/2\\..
<-2-/vv|..
.|<<<2-|.\
.v//.|.v..
Using this configuration, 51 tiles are energized:

.#####....
.#.#.#....
.#.#.#####
.#.#.##...
.#.#.##...
.#.#.##...
.#.#####..
########..
.#######..
.#...#.#..
Find the initial beam configuration that energizes the largest number of tiles; how many tiles are energized in that configuration?
* */