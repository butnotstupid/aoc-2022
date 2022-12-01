package days

class Day1 : Day(1) {

    override fun partOne(): Any {
        return getElfSnacks().maxOf { it.sum() }
    }

    override fun partTwo(): Any {
        return getElfSnacks()
            .map { it.sum() }
            .sortedDescending()
            .take(3)
            .sum()
    }

    private fun getElfSnacks(): List<List<Long>> {
        val snacks = mutableListOf<MutableList<Long>>().also {
            it.add(mutableListOf())
        }
        inputList.forEach {
            if (it.isNotEmpty()) {
                snacks.last().add(it.toLong())
            } else {
                snacks.add(mutableListOf())
            }
        }

        return snacks
    }
}
