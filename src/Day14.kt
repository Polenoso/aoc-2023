import java.awt.geom.Point2D
data class Coordinate2D(var x: Int, var y: Int): Point2D() {
    override fun getX(): kotlin.Double = x.toDouble()

    override fun getY(): kotlin.Double = y.toDouble()

    override fun setLocation(x: kotlin.Double, y: kotlin.Double) {
        this.x = x.toInt()
        this.y = y.toInt()
    }
}

data class Rock(val value: Char, var position: Point2D) {
    val sand: Boolean = value == '.'
    val canMove: Boolean = value == 'O'
}
fun main() {
    fun part1(input: List<String>): Long {
        val map = input.mapIndexed { row, s ->
            s.mapIndexed { column, c ->
                Rock(c, Coordinate2D(row, column))
            }
        }.flatten()
        val rocks = map.filter { !it.sand }.toMutableList()
        for (rock in rocks) {
            val current = rock.position.x
            val firstObstacle = rocks.filter { it.position.y == rock.position.y && it.position.x < current }.maxByOrNull { it.position.x }
            if (rock.canMove && rock.position != firstObstacle?.position) {
                rock.position.setLocation(((firstObstacle?.position?.x ?: -1).toDouble()) + 1, rock.position.y)
            } else if (rock.canMove){
                rock.position.setLocation(0.0, rock.position.y)
            }
        }
//        for (i in 0 until input.size) {
//            for (j in 0 until input.first().length) {
//                val rock = rocks.firstOrNull { it.position.x.toInt() == i && it.position.y.toInt()  == j }
//                if (rock != null) print(rock.value) else print('.')
//            }
//            "".println()
//        }
        val numberOfRows = input.size
        return rocks.filter { it.canMove }.sumOf { numberOfRows - it.position.x }.toLong()
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    // Tests
    val testInput = readInput("Day14_tests")
    part1(testInput).println()
    check(part1(testInput) == 136L)
     //part2(testInput).println()

    val input = readInput("Day14")
    part1(input).println()
    //part2(input).println()
}

/*
--- Day 14: Parabolic Reflector Dish ---
You reach the place where all of the mirrors were pointing: a massive parabolic reflector dish attached to the side of another large mountain.

The dish is made up of many small mirrors, but while the mirrors themselves are roughly in the shape of a parabolic reflector dish, each individual mirror seems to be pointing in slightly the wrong direction. If the dish is meant to focus light, all it's doing right now is sending it in a vague direction.

This system must be what provides the energy for the lava! If you focus the reflector dish, maybe you can go where it's pointing and use the light to fix the lava production.

Upon closer inspection, the individual mirrors each appear to be connected via an elaborate system of ropes and pulleys to a large metal platform below the dish. The platform is covered in large rocks of various shapes. Depending on their position, the weight of the rocks deforms the platform, and the shape of the platform controls which ropes move and ultimately the focus of the dish.

In short: if you move the rocks, you can focus the dish. The platform even has a control panel on the side that lets you tilt it in one of four directions! The rounded rocks (O) will roll when the platform is tilted, while the cube-shaped rocks (#) will stay in place. You note the positions of all of the empty spaces (.) and rocks (your puzzle input). For example:

O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
Start by tilting the lever so all of the rocks will slide north as far as they will go:

OOOO.#.O..
OO..#....#
OO..O##..O
O..#.OO...
........#.
..#....#.#
..O..#.O.O
..O.......
#....###..
#....#....
You notice that the support beams along the north side of the platform are damaged; to ensure the platform doesn't collapse, you should calculate the total load on the north support beams.

The amount of load caused by a single rounded rock (O) is equal to the number of rows from the rock to the south edge of the platform, including the row the rock is on. (Cube-shaped rocks (#) don't contribute to load.) So, the amount of load caused by each rock in each row is as follows:

OOOO.#.O.. 10
OO..#....#  9
OO..O##..O  8
O..#.OO...  7
........#.  6
..#....#.#  5
..O..#.O.O  4
..O.......  3
#....###..  2
#....#....  1
The total load is the sum of the load caused by all of the rounded rocks. In this example, the total load is 136.

Tilt the platform so that the rounded rocks all roll north. Afterward, what is the total load on the north support beams?

* */