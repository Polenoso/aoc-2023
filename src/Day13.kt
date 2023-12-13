fun List<String>.transpose(): List<String> {
    val rows = this.size
    val columns = this[0].length
    val transpose = List<String>(columns){""}.toMutableList()
    for (i in 0 until rows) {
        for (j in 0 until columns) {
            transpose[j] += this[i][j].toString()
        }
    }
    return transpose
}

fun List<String>.mirrorIndex(): Long? {
    val mirrorPositions = emptyList<Int>().toMutableList()
    for (i in indices) {
        if (i == lastIndex) continue
        if (get(i) == get(i + 1)) mirrorPositions.add(i+1)
    }
    for (indexPosition in mirrorPositions) {
        val steps = kotlin.math.min(indexPosition, size - (indexPosition))
        var valid = true
        for (i in 0 until steps) {
            val left = get(indexPosition + i)
            val right = get(indexPosition - i - 1)
            if (left != right) {
                valid = false
                break
            }
        }
        if (valid) return indexPosition.toLong()
    }
    return null
}

fun main() {
    fun part1(input: List<String>): Long {
        val patterns: List<List<String>> = input.joinToString("\n").split("\n\n").map { it.split("\n") }
        var sum = 0L
        patterns.forEach { pattern ->
            sum += pattern.mirrorIndex()?.times(100) ?: pattern.transpose().mirrorIndex()?: 0
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        return input.size.toLong()
    }

    // Tests
    val testInput = readInput("Day13_tests")
     part1(testInput).println()
     check(part1(testInput) == 405L)
    // part2(testInput).println()

    val input = readInput("Day13")
    part1(input).println()
    //part2(input).println()
}

/*
--- Day 13: Point of Incidence ---
With your help, the hot springs team locates an appropriate spring which launches you neatly and precisely up to the edge of Lava Island.

There's just one problem: you don't see any lava.

You do see a lot of ash and igneous rock; there are even what look like gray mountains scattered around. After a while, you make your way to a nearby cluster of mountains only to discover that the valley between them is completely full of large mirrors. Most of the mirrors seem to be aligned in a consistent way; perhaps you should head in that direction?

As you move through the valley of mirrors, you find that several of them have fallen from the large metal frames keeping them in place. The mirrors are extremely flat and shiny, and many of the fallen mirrors have lodged into the ash at strange angles. Because the terrain is all one color, it's hard to tell where it's safe to walk or where you're about to run into a mirror.

You note down the patterns of ash (.) and rocks (#) that you see as you walk (your puzzle input); perhaps by carefully analyzing these patterns, you can figure out where the mirrors are!

For example:

#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
To find the reflection in each pattern, you need to find a perfect reflection across either a horizontal line between two rows or across a vertical line between two columns.

In the first pattern, the reflection is across a vertical line between two columns; arrows on each of the two columns point at the line between the columns:

123456789
    ><
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.
    ><
123456789
In this pattern, the line of reflection is the vertical line between columns 5 and 6. Because the vertical line is not perfectly in the middle of the pattern, part of the pattern (column 1) has nowhere to reflect onto and can be ignored; every other column has a reflected column within the pattern and must match exactly: column 2 matches column 9, column 3 matches 8, 4 matches 7, and 5 matches 6.

The second pattern reflects across a horizontal line instead:

1 #...##..# 1
2 #....#..# 2
3 ..##..### 3
4v#####.##.v4
5^#####.##.^5
6 ..##..### 6
7 #....#..# 7
This pattern reflects across the horizontal line between rows 4 and 5. Row 1 would reflect with a hypothetical row 8, but since that's not in the pattern, row 1 doesn't need to match anything. The remaining rows match: row 2 matches row 7, row 3 matches row 6, and row 4 matches row 5.

To summarize your pattern notes, add up the number of columns to the left of each vertical line of reflection; to that, also add 100 multiplied by the number of rows above each horizontal line of reflection. In the above example, the first pattern's vertical line has 5 columns to its left and the second pattern's horizontal line has 4 rows above it, a total of 405.

Find the line of reflection in each of the patterns in your notes. What number do you get after summarizing all of your notes?


* */