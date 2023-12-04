
fun main() {
    fun Char.isValidSymbol(): Boolean {
        return this != '.' && !this.isDigit()
    }
    fun hasAnySymbolCloseby(input: List<String>, currentRow: Int, columns: List<Int>): Boolean {
        columns.forEach {
            var symbolRight = '.'
            var symbolLeft = '.'
            var symbolUp = '.'
            var symbolDown = '.'
            var symbolUpLeft = '.'
            var symbolUpRight = '.'
            var symbolDownLeft = '.'
            var symbolDownRight = '.'
            if (it < input[currentRow].lastIndex) {
                symbolRight = input[currentRow][it + 1]
            }
            if (it > 0) {
                symbolLeft = input[currentRow][it - 1]
            }
            if (currentRow > 0) {
                symbolUp = input[currentRow-1][it]
            }
            if (currentRow < input.lastIndex) {
                symbolDown = input[currentRow+1][it]
            }
            if (currentRow > 0 && it+1 < input[currentRow - 1].lastIndex) {
                symbolUpRight = input[currentRow - 1][it + 1]
            }
            if (currentRow > 0 && it-1 > 0) {
                symbolUpLeft = input[currentRow - 1][it - 1]
            }
            if (currentRow + 1 < input.lastIndex && it+1 < input[currentRow + 1].lastIndex) {
                symbolDownRight = input[currentRow + 1][it + 1]
            }
            if (currentRow + 1 < input.size && it-1 > 0) {
                symbolDownLeft = input[currentRow + 1][it - 1]
            }
            if (symbolUp.isValidSymbol()
                || symbolDown.isValidSymbol()
                || symbolRight.isValidSymbol()
                || symbolLeft.isValidSymbol()
                || symbolDownLeft.isValidSymbol()
                || symbolDownRight.isValidSymbol()
                || symbolUpRight.isValidSymbol()
                || symbolUpLeft.isValidSymbol())
                return true
        }
        return false
    }
    fun part1(input: List<String>): Int {
        var currentSum = 0
        input.forEachIndexed { row, s ->
            var currentRow = ""
            s.forEachIndexed { column, character ->
                if (character != '.' && character.isDigit()) {
                    currentRow += character
                }
                if (currentRow.isNotEmpty()
                    && ((column+1 < s.length
                    && !s[column + 1].isDigit())
                            || (column == s.lastIndex && character.isDigit()))
                    ) {
                    val value = currentRow.toInt()
                    val columns = currentRow.mapIndexed { index, _ ->
                        column - (index)
                    }
                    if (hasAnySymbolCloseby(input, row, columns)) {
                        currentSum += value
                        currentRow = ""
                    } else {
                        currentRow = ""
                    }
                }
            }
        }


        return currentSum
    }

    data class Number(val value: Int, val row: Int, val columnInit: Int, val columnEnd: Int)

    fun part2(input: List<String>): Int {
        val indexes: MutableList<Pair<Int, Int>> = emptyList<Pair<Int, Int>>().toMutableList()
        // Find all * in matrix
        input.forEachIndexed { row, s ->
            s.forEachIndexed { column, c ->
                if (c == '*') {
                    indexes.add(Pair(row, column))
                }
            }
        }
        val numbers: List<Sequence<Number>> = input.mapIndexed { index, row ->
            Regex("[0-9]+").findAll(row)
                .map {
                    Number(it.value.toInt(), index, it.range.start, it.range.endInclusive)
                }
        }

        val flat = numbers.flatMap { it }

        return indexes.map { symbol ->
            val collideNumbers = flat.filter { number ->
                symbol.first in number.row - 1 .. number.row + 1 && symbol.second in number.columnInit - 1 .. number.columnEnd + 1
            }
            if (collideNumbers.size == 2) {
                collideNumbers.map { it.value }.reduce { acc, i -> acc * i }
            } else {
                0
            }
        }.reduce { acc, i -> acc + i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_tests")
   // part1(testInput).println()
    check(part1(testInput) == 4361)
    part2(testInput).println()
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

/*
--- Day 3: Gear Ratios ---
You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source, but this is as far as he can bring you. You go inside.

It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.

"Aaah!"

You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.

The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one. If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.

The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)

Here is an example engine schematic:

467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.

Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?
--- Part Two ---
The engineer finds the missing part and installs it in the engine! As the engine springs to life, you jump in the closest gondola, finally ready to ascend to the water source.

You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the gondola has a phone labeled "help", so you pick it up and the engineer answers.

Before you can explain the situation, she suggests that you look out the window. There stands the engineer, holding a phone in one hand and waving with the other. You're going so slowly that you haven't even left the station. You exit the gondola.

The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.

This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.

Consider the same engine schematic again:

467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.

What is the sum of all of the gear ratios in your engine schematic?


* */