package days

class Day8 : Day(8) {

    private val dirs = sequenceOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)

    override fun partOne(): Any {
        val trees = inputList.map { it.map { it.code - '0'.code } }
        val size = trees.size
        val visible = Array(size) { Array(size) { false } }
        for (row in 0 until size) {
            for (col in 0 until size) {
                visible[row][col] = dirs.any { (dr, dc) ->
                    var k = 1
                    while (!away(row + k * dr, col + k * dc, size)
                        && trees[row][col] > trees[row + k * dr][col + k * dc]
                    ) k++
                    away(row + k * dr, col + k * dc, size)
                }
            }
        }

        return visible.sumOf { it.count { it } }
    }

    override fun partTwo(): Any {
        val trees = inputList.map { it.map { it.code - '0'.code } }
        val size = trees.size
        val view = Array(size) { Array(size) { 0 } }
        for (row in 0 until size) {
            for (col in 0 until size) {
                view[row][col] =
                    dirs.map { (dr, dc) ->
                        var k = 1
                        while (!away(row + k * dr, col + k * dc, size)
                            && trees[row][col] > trees[row + k * dr][col + k * dc]
                        ) k++
                        if (!away(row + k * dr, col + k * dc, size)) k else k - 1
                    }.reduce { acc, next -> acc * next }
            }
        }

        return view.maxOf { it.maxOf { it } }
    }

    private fun away(row: Int, col: Int, size: Int) =
        row < 0 || row >= size || col < 0 || col >= size
}

