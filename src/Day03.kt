
fun main() {
    fun Char.isValidSymbol(): Boolean {
        return this != '.' && !this.isDigit()
    }
    fun hasAnySymbolCloseby(input: List<String>, currentRow: Int, columns: List<Int>): Boolean {
        columns.forEach {
            var symbolRight: Char = '.'
            var symbolLeft: Char = '.'
            var symbolUp: Char = '.'
            var symbolDown: Char = '.'
            var symbolUpLeft: Char = '.'
            var symbolUpRight: Char = '.'
            var symbolDownLeft: Char = '.'
            var symbolDownRight: Char = '.'
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

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_tests")
   // part1(testInput).println()
    //check(part1(testInput) == 4361)
    //check(part2(testInput) == 2286)

    val input = readInput("Day03")
    part1(input).println()
    //part2(input).println()
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



* */