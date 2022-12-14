package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day8Test {

    private val dayEight = Day8()

    @Test
    fun testPartOne() {
        val partOne = dayEight.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(1546))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayEight.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(519064))
    }
}
