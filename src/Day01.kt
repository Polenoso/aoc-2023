import java.lang.Integer.max
import kotlin.math.min

fun main() {
    fun onlyNumbers(input: String): String {
        return input
            .filter {
                it.digitToIntOrNull() != null
            }
    }

    fun firstAndLastNumber(input: String): String {
        return input.first().toString() + input.last().toString()
    }

    fun part1(input: List<String>): Int {
        return input
            .map { onlyNumbers(it) }
            .map { firstAndLastNumber(it) }
            .sumOf { it.toInt() }
    }

    fun parseToNumber(input: String): String {
        val numberToDigitDict = HashMap<String, String>()
        numberToDigitDict["one"] = "1"
        numberToDigitDict["two"] = "2"
        numberToDigitDict["three"] = "3"
        numberToDigitDict["four"] = "4"
        numberToDigitDict["five"] = "5"
        numberToDigitDict["six"] = "6"
        numberToDigitDict["seven"] = "7"
        numberToDigitDict["eight"] = "8"
        numberToDigitDict["nine"] = "9"

        val max = input.count()
        val firstNumberOccurrence = input.findAnyOf(numberToDigitDict.keys)
        val firstDigitOccurrence = input.findAnyOf(numberToDigitDict.values)
        val firstIndex = min(firstNumberOccurrence?.first ?: run { max }, firstDigitOccurrence?.first ?: run { max })

        val lastNumberOccurrence = input.findLastAnyOf(numberToDigitDict.keys)
        val lastDigitOccurrence = input.findLastAnyOf(numberToDigitDict.values)
        val lastIndex = max(lastNumberOccurrence?.first ?: run { 0 }, lastDigitOccurrence?.first ?: run { 0 })

        val firstValue =
            (if (firstIndex == firstDigitOccurrence?.first) firstDigitOccurrence.second else numberToDigitDict[firstNumberOccurrence!!.second])!!
        val secondValue =
            (if (lastIndex == lastDigitOccurrence?.first) lastDigitOccurrence.second else numberToDigitDict[lastNumberOccurrence!!.second])!!
        return firstValue + secondValue
    }

    fun part2(input: List<String>): Int {
        return input
            .map { parseToNumber(it).toInt() }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_tests")
    check(part2(testInput) == 281)
//    part2(testInput).println()
//    check(parseToNumber("twone") == "21")
//    parseToNumber("4nineeightseven2").println()

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

/*
--- Day 1: Trebuchet?! ---
Something is wrong with global snow production, and you've been selected to take a look. The Elves have even given you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.

You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by December 25th.

Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

You try to ask why they can't just use a weather machine ("not powerful enough") and where they're even sending you ("the sky") and why your map looks mostly blank ("you sure ask a lot of questions") and hang on did you just say the sky ("of course, where do you think snow comes from") when you realize that the Elves are already loading you into a trebuchet ("please hold still, we need to strap you in").

As they're making the final adjustments, they discover that their calibration document (your puzzle input) has been amended by a very young Elf who was apparently just excited to show off her art skills. Consequently, the Elves are having trouble reading the values on the document.

The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.

For example:

1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.

Consider your entire calibration document. What is the sum of all of the calibration values?
* */