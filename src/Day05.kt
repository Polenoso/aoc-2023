typealias Seed = Long
data class LineMap(val minDestination: Long, val minSource: Long, val length: Long) {
    fun convert(value: Long): Long? {
        val sourceRange = minSource ..< (minSource+length)
        if (value in sourceRange) {
            return value + (minDestination - minSource)
        }
        return null
    }
}

data class Map(var converters: List<LineMap>) {
    fun convert(value: Long): Long {
        val converted = converters.mapNotNull { it.convert(value) }
        return if (converted.isEmpty()) value else converted.first()
    }
}

data class Category(val id: String, val destination: String, val map: Map) {
    fun associatedCategory(cats: List<Category>): Category {
        return cats.first { it.id == this.destination }
    }

    fun valueForInput(value: Long): Long {
        return map.convert(value)
    }
}

class Almanac(input: List<String>) {
    private val seeds: List<Seed>
    private val categories: List<Category>

    init {
        val cleanEmptyLines = input.filter { it.isNotEmpty() }
        seeds = cleanEmptyLines.first { it.contains("seeds:") }.split(" ").mapNotNull { it.toLongOrNull() }
        val mapsLines = cleanEmptyLines.filter { !it.contains("seeds:") }
        val mutableCategories = emptyList<Category>().toMutableList()
        var source: String? = null
        var destination: String? = null
        var converters = emptyList<LineMap>().toMutableList()
        mapsLines.forEachIndexed { index, it ->
            if (it.contains("map:")) {
                if (!destination.isNullOrEmpty() && !source.isNullOrEmpty()) {
                    val newCategory = Category(source!!, destination!!, Map(converters))
                    mutableCategories.add(newCategory)
                    converters = emptyList<LineMap>().toMutableList()
                }
                val sourceAndDest = it.removeSuffix(" map:").split("-to-")
                destination = sourceAndDest.last()
                source = sourceAndDest.first()
            } else {
              val lineValues = it.split(" ").mapNotNull { it.toLongOrNull() }
              val newLineMap = LineMap(lineValues.first(), lineValues[1], lineValues.last())
              converters.add(newLineMap)
            }
            if (index == mapsLines.lastIndex) {
                val newCategory = Category(source!!, destination!!, Map(converters))
                mutableCategories.add(newCategory)
            }
        }
        categories = mutableCategories
    }

    fun findMinLocation(): Long {
        val category = categories.first { it.id == "seed" }
        val locationCategory = categories.first { it.destination == "location" }
        val locations = seeds.map {
            var seedCat = category
            var value = seedCat.valueForInput(it)
            while (seedCat.destination != "location") {
                seedCat = seedCat.associatedCategory(categories)
                value = seedCat.valueForInput(value)
            }
            value
        }
        return  locations.min()
    }
}
fun main() {
    fun part1(input: List<String>): Long {
        return Almanac(input).findMinLocation()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_tests")
    //part1(testInput).println()
    // part2(testInput).println()
     check(part1(testInput) == 35.toLong())
    // check(part2(testInput) == 30)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

/*
--- Day 5: If You Give A Seed A Fertilizer ---
You take the boat and find the gardener right where you were told he would be: managing a giant "garden" that looks more to you like a farm.

"A water source? Island Island is the water source!" You point out that Snow Island isn't receiving any water.

"Oh, we had to stop the water because we ran out of sand to filter it with! Can't make snow with dirty water. Don't worry, I'm sure we'll get more sand soon; we only turned off the water a few days... weeks... oh no." His face sinks into a look of horrified realization.

"I've been so busy making sure everyone here has food that I completely forgot to check why we stopped getting more sand! There's a ferry leaving soon that is headed over in that direction - it's much faster than your boat. Could you please go check it out?"

You barely have time to agree to this request when he brings up another. "While you wait for the ferry, maybe you can help us with our food production problem. The latest Island Island Almanac just arrived and we're having trouble making sense of it."

The almanac (your puzzle input) lists all of the seeds that need to be planted. It also lists what type of soil to use with each kind of seed, what type of fertilizer to use with each kind of soil, what type of water to use with each kind of fertilizer, and so on. Every type of seed, soil, fertilizer and so on is identified with a number, but numbers are reused by each category - that is, soil 123 and fertilizer 123 aren't necessarily related to each other.

For example:

seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
The almanac starts by listing which seeds need to be planted: seeds 79, 14, 55, and 13.

The rest of the almanac contains a list of maps which describe how to convert numbers from a source category into numbers in a destination category. That is, the section that starts with seed-to-soil map: describes how to convert a seed number (the source) to a soil number (the destination). This lets the gardener and his team know which soil to use with which seeds, which water to use with which fertilizer, and so on.

Rather than list every source number and its corresponding destination number one by one, the maps describe entire ranges of numbers that can be converted. Each line within a map contains three numbers: the destination range start, the source range start, and the range length.

Consider again the example seed-to-soil map:

50 98 2
52 50 48
The first line has a destination range start of 50, a source range start of 98, and a range length of 2. This line means that the source range starts at 98 and contains two values: 98 and 99. The destination range is the same length, but it starts at 50, so its two values are 50 and 51. With this information, you know that seed number 98 corresponds to soil number 50 and that seed number 99 corresponds to soil number 51.

The second line means that the source range starts at 50 and contains 48 values: 50, 51, ..., 96, 97. This corresponds to a destination range starting at 52 and also containing 48 values: 52, 53, ..., 98, 99. So, seed number 53 corresponds to soil number 55.

Any source numbers that aren't mapped correspond to the same destination number. So, seed number 10 corresponds to soil number 10.

So, the entire list of seed numbers and their corresponding soil numbers looks like this:

seed  soil
0     0
1     1
...   ...
48    48
49    49
50    52
51    53
...   ...
96    98
97    99
98    50
99    51
With this map, you can look up the soil number required for each initial seed number:

Seed number 79 corresponds to soil number 81.
Seed number 14 corresponds to soil number 14.
Seed number 55 corresponds to soil number 57.
Seed number 13 corresponds to soil number 13.
The gardener and his team want to get started as soon as possible, so they'd like to know the closest location that needs a seed. Using these maps, find the lowest location number that corresponds to any of the initial seeds. To do this, you'll need to convert each seed number through other categories until you can find its corresponding location number. In this example, the corresponding types are:

Seed 79, soil 81, fertilizer 81, water 81, light 74, temperature 78, humidity 78, location 82.
Seed 14, soil 14, fertilizer 53, water 49, light 42, temperature 42, humidity 43, location 43.
Seed 55, soil 57, fertilizer 57, water 53, light 46, temperature 82, humidity 82, location 86.
Seed 13, soil 13, fertilizer 52, water 41, light 34, temperature 34, humidity 35, location 35.
So, the lowest location number in this example is 35.

What is the lowest location number that corresponds to any of the initial seed numbers?

* */