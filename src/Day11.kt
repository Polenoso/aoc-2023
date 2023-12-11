import kotlin.math.abs
data class Galaxy(val x: Long, val y: Long) {
    fun findShortPath(toGalaxy: Galaxy): Long {
        return abs(toGalaxy.x - x) + abs(toGalaxy.y - y)
    }
}
fun main() {
    fun part1(input: List<String>): Long {
        val emptyRows = emptySet<Int>().toMutableSet()
        val filledColumns = emptySet<Int>().toMutableSet()
        val galaxies = emptySet<Galaxy>().toMutableSet()
        input.forEachIndexed {rowIndex, row ->
            var isRowEmpty = true
            row.forEachIndexed { columnIndex, column ->
                if (isRowEmpty && column != '.') {
                    isRowEmpty = false
                }
                if (column == '#') {
                    filledColumns.add(columnIndex)
                }
            }
            if (isRowEmpty) emptyRows.add(rowIndex)
        }
        val emptyColumns = (0..<input.first().count()).filter { !filledColumns.contains(it) }.toList()
        // Expand Columns
        val expandedSpace = input.map { it ->
            val row = it.toMutableList()
            emptyColumns.forEachIndexed {index, i ->
                row.add(i+index, '.')
            }
            row.joinToString("")
        }.toMutableList()

        // Expand Rows
        emptyRows.forEachIndexed { index, i ->
            expandedSpace.add(i+index, expandedSpace[i+index])
        }

        expandedSpace.forEachIndexed { row, s ->
            s.forEachIndexed { column, c ->
                if (c == '#') {
                    galaxies.add(Galaxy(row.toLong(), column.toLong()))
                }
            }
        }
        val gal = galaxies.toList()
        var sum: Long = 0
        for (i in 0..<gal.count()) {
            for (j in i+1 ..< gal.count()) {
                sum += gal[i].findShortPath(gal[j])
            }
        }

        return sum
    }
    fun findGalaxies(input: List<String>): List<Galaxy> {
        val galaxies = emptySet<Galaxy>().toMutableSet()
        input.forEachIndexed { row, s ->
            s.forEachIndexed { column, c ->
                if (c == '#') {
                    galaxies.add(Galaxy(row.toLong(), column.toLong()))
                }
            }
        }
        return galaxies.toList()
    }

    fun part2(input: List<String>): Long {
        val galaxies = findGalaxies(input)
        val emptyRows = emptySet<Int>().toMutableSet()
        val filledColumns = emptySet<Int>().toMutableSet()
        input.forEachIndexed {rowIndex, row ->
            var isRowEmpty = true
            row.forEachIndexed { columnIndex, column ->
                if (isRowEmpty && column != '.') {
                    isRowEmpty = false
                }
                if (column == '#') {
                    filledColumns.add(columnIndex)
                }
            }
            if (isRowEmpty) emptyRows.add(rowIndex)
        }
        val emptyColumns = (0..<input.first().count()).filter { !filledColumns.contains(it) }.toList()
        val factor = 999999L
        // Expand Factor
        val gal = galaxies.map {
            var x = it.x
            var y = it.y
            val numberOfER = emptyRows.count { ro -> ro <= x }
            val numberOfEC = emptyColumns.count { co -> co <= y }
            if (numberOfER > 0) {
                x += ((factor) * numberOfER)
            }
            if (numberOfEC > 0) {
                y += ((factor) * numberOfEC)
            }
            Galaxy(x, y)
        }
        var sum: Long = 0
        for (i in 0..<gal.count()) {
            for (j in i+1 ..< gal.count()) {
                sum += gal[i].findShortPath(gal[j])
            }
        }

        return sum
    }

    // Tests
    val testInput = readInput("Day11_tests")
    part1(testInput).println()
    check(part1(testInput) == 374.toLong())
    part2(testInput).println()

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()

}

/*
--- Day 11: Cosmic Expansion ---
You continue following signs for "Hot Springs" and eventually come across an observatory. The Elf within turns out to be a researcher studying cosmic expansion using the giant telescope here.

He doesn't know anything about the missing machine parts; he's only visiting for this research project. However, he confirms that the hot springs are the next-closest area likely to have people; he'll even take you straight there once he's done with today's observation analysis.

Maybe you can help him with the analysis to speed things up?

The researcher has collected a bunch of data and compiled the data into a single giant image (your puzzle input). The image includes empty space (.) and galaxies (#). For example:

...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
The researcher is trying to figure out the sum of the lengths of the shortest path between every pair of galaxies. However, there's a catch: the universe expanded in the time it took the light from those galaxies to reach the observatory.

Due to something involving gravitational effects, only some space expands. In fact, the result is that any rows or columns that contain no galaxies should all actually be twice as big.

In the above example, three columns and two rows contain no galaxies:

   v  v  v
 ...#......
 .......#..
 #.........
>..........<
 ......#...
 .#........
 .........#
>..........<
 .......#..
 #...#.....
   ^  ^  ^
These rows and columns need to be twice as big; the result of cosmic expansion therefore looks like this:

....#........
.........#...
#............
.............
.............
........#....
.#...........
............#
.............
.............
.........#...
#....#.......
Equipped with this expanded universe, the shortest path between every pair of galaxies can be found. It can help to assign every galaxy a unique number:

....1........
.........2...
3............
.............
.............
........4....
.5...........
............6
.............
.............
.........7...
8....9.......
In these 9 galaxies, there are 36 pairs. Only count each pair once; order within the pair doesn't matter. For each pair, find any shortest path between the two galaxies using only steps that move up, down, left, or right exactly one . or # at a time. (The shortest path between two galaxies is allowed to pass through another galaxy.)

For example, here is one of the shortest paths between galaxies 5 and 9:

....1........
.........2...
3............
.............
.............
........4....
.5...........
.##.........6
..##.........
...##........
....##...7...
8....9.......
This path has length 9 because it takes a minimum of nine steps to get from galaxy 5 to galaxy 9 (the eight locations marked # plus the step onto galaxy 9 itself). Here are some other example shortest path lengths:

Between galaxy 1 and galaxy 7: 15
Between galaxy 3 and galaxy 6: 17
Between galaxy 8 and galaxy 9: 5
In this example, after expanding the universe, the sum of the shortest path between all 36 pairs of galaxies is 374.

Expand the universe, then find the length of the shortest path between every pair of galaxies. What is the sum of these lengths?

--- Part Two ---
The galaxies are much older (and thus much farther apart) than the researcher initially estimated.

Now, instead of the expansion you did before, make each empty row or column one million times larger. That is, each empty row should be replaced with 1000000 empty rows, and each empty column should be replaced with 1000000 empty columns.

(In the example above, if each empty row or column were merely 10 times larger, the sum of the shortest paths between every pair of galaxies would be 1030. If each empty row or column were merely 100 times larger, the sum of the shortest paths between every pair of galaxies would be 8410. However, your universe will need to expand far beyond these values.)

Starting with the same initial image, expand the universe according to these new rules, then find the length of the shortest path between every pair of galaxies. What is the sum of these lengths?
* */