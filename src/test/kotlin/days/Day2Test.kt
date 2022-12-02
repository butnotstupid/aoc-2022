package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day2Test {

    private val dayTwo = Day2()

    @Test
    fun testPartOne() {
        val partOne = dayTwo.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(15691))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayTwo.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(12989))
    }
}
